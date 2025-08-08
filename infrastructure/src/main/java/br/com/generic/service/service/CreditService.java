package br.com.generic.service.service;


import br.com.generic.service.account.FormalizationRequest;
import br.com.generic.service.account.ProposalRequest;
import br.com.generic.service.account.ProposalResponse;
import br.com.generic.service.account.SimulationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CreditService {

    Page<SimulationResponse> simulateCredit(String cpf, Double income, Double requestedAmount, Pageable pageable) throws IllegalAccessException;

    ProposalResponse createProposal(ProposalRequest request);

    List<ProposalResponse> listProposals(PageRequest paging);

    Optional<ProposalResponse> getProposalById(String proposalId);

    ProposalResponse formalizeProposal(String proposalId, FormalizationRequest request);
}


