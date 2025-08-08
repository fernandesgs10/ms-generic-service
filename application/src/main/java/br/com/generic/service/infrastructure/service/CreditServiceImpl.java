package br.com.generic.service.infrastructure.service;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.generic.service.account.EligibilityRequest;
import br.com.generic.service.account.FormalizationRequest;
import br.com.generic.service.account.ProposalRequest;
import br.com.generic.service.account.ProposalResponse;
import br.com.generic.service.account.SimulationResponse;
import br.com.generic.service.config.NotEligibleException;
import br.com.generic.service.service.CreditService;
import br.com.generic.service.usecases.credit.CreditPersonalUseCase;
import br.com.generic.service.usecases.credit.EligibilityUseCase;
import br.com.generic.service.usecases.credit.SimulationUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final SimulationUseCase simulationUseCase;
    private final EligibilityUseCase eligibilityUseCase;
    private final CreditPersonalUseCase creditPersonalUseCase;

    @Override
    public Page<SimulationResponse> simulateCredit(String cpf, Double income, Double requestedAmount, Pageable pageable) {
        CPFValidator cpfValidator = new CPFValidator();
        cpfValidator.assertValid(cpf);

        EligibilityRequest eligibilityRequest =
                new EligibilityRequest(cpf, requestedAmount);

        var isEligibility = eligibilityUseCase.execute(eligibilityRequest);
        if(isEligibility.getEligible()) {
            return simulationUseCase.execute(cpf, income, requestedAmount, 100, pageable);

        }

        log.info("no eligibility{}", isEligibility.getEligible());
        throw new NotEligibleException(isEligibility);
    }

    @Override
    public ProposalResponse createProposal(ProposalRequest request) {
        creditPersonalUseCase.execute(request);
        return null;
    }

    @Override
    public List<ProposalResponse> listProposals(PageRequest paging) {
        return null;
    }

    @Override
    public Optional<ProposalResponse> getProposalById(String proposalId) {
        return Optional.empty();
    }

    @Override
    public ProposalResponse formalizeProposal(String proposalId, FormalizationRequest request) {
        return null;
    }
}
