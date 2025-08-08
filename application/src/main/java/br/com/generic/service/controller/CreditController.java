package br.com.generic.service.controller;

import br.com.generic.service.Utils.SortConverterUtil;
import br.com.generic.service.account.CreditsApi;
import br.com.generic.service.account.FormalizationRequest;
import br.com.generic.service.account.ProposalRequest;
import br.com.generic.service.account.ProposalResponse;
import br.com.generic.service.service.CreditService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class CreditController implements CreditsApi {

    private final CreditService creditService;

    @SneakyThrows
    @Override
    public ResponseEntity<Object> creditsSimulationsGet(
            @Parameter(name = "page", description = "page number", in = ParameterIn.QUERY) @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "size number", in = ParameterIn.QUERY) @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @Parameter(name = "sortBy", description = "sort number", in = ParameterIn.QUERY) @Valid @RequestParam(value = "sortBy", required = false) String sortBy,
            @Parameter(name = "cpf", description = "cpf", in = ParameterIn.QUERY) @Valid @RequestParam(value = "cpf", required = false) String cpf,
            @Parameter(name = "income", description = "income", in = ParameterIn.QUERY) @Valid @RequestParam(value = "income", required = false) Double income,
            @Parameter(name = "requestedAmount", description = "requestedAmount", in = ParameterIn.QUERY) @Valid @RequestParam(value = "requestedAmount", required = false) Double requestedAmount) {

        var sortConvert = SortConverterUtil.getOrdersFromString(sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortConvert));

        var simulations = creditService.simulateCredit(cpf, income, requestedAmount, pageable);
        log.info("Response: simulations count={}", simulations);
        return ResponseEntity.ok(simulations);
    }

    @Override
    public ResponseEntity<ProposalResponse> creditsProposalsPost(@Valid @RequestBody ProposalRequest request) {
        log.info("POST /credits/proposals - Request received: {}", request);
        ProposalResponse response = creditService.createProposal(request);
        log.info("Response: {}", response);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<List<ProposalResponse>> creditsProposalsGet(Integer page, Integer size, String sortBy) {
        log.info("GET /credits/proposals - page={}, size={}, sortBy={}", page, size, sortBy);
        var sortOrders = SortConverterUtil.getOrdersFromString(sortBy);
        PageRequest paging = PageRequest.of(page, size, Sort.by(sortOrders));
        List<ProposalResponse> proposals = creditService.listProposals(paging);
        log.info("Response: proposals count={}", proposals.size());
        return ResponseEntity.ok(proposals);
    }

    @Override
    public ResponseEntity<ProposalResponse> creditsProposalsProposalIdGet(String proposalId) {
        log.info("GET /credits/proposals/{} - Request received", proposalId);
        var proposalOpt = creditService.getProposalById(proposalId);
        return proposalOpt
                .map(proposal -> {
                    log.info("Proposal found: {}", proposal);
                    return ResponseEntity.ok(proposal);
                })
                .orElseGet(() -> {
                    log.warn("Proposal not found: {}", proposalId);
                    return ResponseEntity.notFound().build();
                });
    }

    @Override
    public ResponseEntity<ProposalResponse> creditsProposalsProposalIdFormalizePost(String proposalId, @Valid @RequestBody FormalizationRequest request) {
        log.info("POST /credits/proposals/{}/formalize - Request received: {}", proposalId, request);
        ProposalResponse response = creditService.formalizeProposal(proposalId, request);
        log.info("Response: {}", response);
        return ResponseEntity.ok(response);
    }
}
