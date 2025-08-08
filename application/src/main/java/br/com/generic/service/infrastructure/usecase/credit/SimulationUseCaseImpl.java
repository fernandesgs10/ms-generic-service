package br.com.generic.service.infrastructure.usecase.credit;

import br.com.generic.service.account.SimulationRequest;
import br.com.generic.service.account.SimulationResponse;
import br.com.generic.service.entity.CreditRatePolicyEntity;
import br.com.generic.service.entity.SimulationEntity;
import br.com.generic.service.entity.SimulationItensEntity;
import br.com.generic.service.mapper.SimulationItensMapper;
import br.com.generic.service.repository.CreditRatePolicyRepository;
import br.com.generic.service.repository.SimulationItensRepository;
import br.com.generic.service.repository.SimulationRepository;
import br.com.generic.service.usecases.credit.SimulationUseCase;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimulationUseCaseImpl implements SimulationUseCase {

    private final CreditRatePolicyRepository creditRatePolicyRepository;
    private final SimulationRepository simulationRepository;
    private final SimulationItensMapper simulationItensMapper;
    private final SimulationItensRepository simulationItensRepository;

    @Override
    public Page<SimulationResponse> execute(String cpf, Double income, Double requestedAmount, Integer score, Pageable pageable) {

        List<Integer> terms = List.of(12, 24, 36);
        List<CreditRatePolicyEntity> policies = creditRatePolicyRepository.findMatchingPolicies(income, score);

        CreditRatePolicyEntity defaultPolicy = creditRatePolicyRepository.findDefaultPolicy()
                .orElseThrow(() -> new RuntimeException("Default policy not configured"));

        var resultItens = terms.stream()
                .map(term -> {
                    CreditRatePolicyEntity policy = policies.stream()
                            .filter(p -> term <= p.getMaxTermInMonths())
                            .findFirst()
                            .orElse(defaultPolicy);
                    return createSimulation(BigDecimal.valueOf(requestedAmount), policy, term);
                })
                .collect(Collectors.toList());

        SimulationRequest simulationRequest = new SimulationRequest();
        simulationRequest.cpf(cpf);
        simulationRequest.score(score);
        simulationRequest.setRequestedAmount(requestedAmount);
        simulationRequest.setIncome(income);

        return saveSimulation(simulationRequest, resultItens, pageable);

    }

    private SimulationResponse createSimulation(BigDecimal requestedAmount, CreditRatePolicyEntity policy, int termMonths) {
        BigDecimal interestRate = BigDecimal.valueOf(policy.getInterestRatePercent());
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal numerator = requestedAmount.multiply(monthlyRate).multiply((BigDecimal.ONE.add(monthlyRate)).pow(termMonths));
        BigDecimal denominator = (BigDecimal.ONE.add(monthlyRate)).pow(termMonths).subtract(BigDecimal.ONE);
        BigDecimal installment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        BigDecimal iof = requestedAmount.multiply(BigDecimal.valueOf(policy.getIofPercent() / 100)).setScale(2, RoundingMode.HALF_UP);

        BigDecimal tac = requestedAmount.multiply(BigDecimal.valueOf(policy.getOpeningFeePercent() / 100)).setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalPayable = installment.multiply(BigDecimal.valueOf(termMonths)).add(iof).add(tac).setScale(2, RoundingMode.HALF_UP);

        SimulationResponse response = new SimulationResponse();
        response.setTermMonths(termMonths);
        response.setMonthlyInstallment(installment.doubleValue());
        response.setInterestRatePercent(interestRate.doubleValue());
        response.setIof(iof.doubleValue());
        response.setTac(tac.doubleValue());
        response.setTotalPayable(totalPayable.doubleValue());

        return response;
    }

    private Page<SimulationResponse> saveSimulation(SimulationRequest simulationRequest, List<SimulationResponse> simulationResponse, Pageable pageable) {
        Optional<SimulationEntity> existingSimulationOpt = simulationRepository
                .findByCpf(simulationRequest.getCpf());

        SimulationEntity simulationEntity = existingSimulationOpt.orElseGet(SimulationEntity::new);
        simulationEntity.setCpf(simulationRequest.getCpf());
        simulationEntity.setIncome(simulationRequest.getIncome());
        simulationEntity.setScore(simulationRequest.getScore());
        simulationEntity.setRequestedAmount(simulationRequest.getRequestedAmount());

        Optional.ofNullable(simulationEntity.getItens())
                .ifPresentOrElse(
                        List::clear,
                        () -> simulationEntity.setItens(new ArrayList<>())
                );

        List<SimulationItensEntity> simulationItensEntities = simulationResponse.stream()
                .map(response -> {
                    SimulationItensEntity item = simulationItensMapper.converterToEntity(response);
                    item.setSimulation(simulationEntity);
                    return item;
                })
                .toList();

        simulationEntity.getItens().addAll(simulationItensEntities);

        simulationRepository.save(simulationEntity);

        Page<SimulationItensEntity> pageSimulation = simulationItensRepository.findBySimulationId(simulationEntity.getId(), pageable);

        return pageSimulation.map(simulationItensMapper::converterToResponse);



    }

}
