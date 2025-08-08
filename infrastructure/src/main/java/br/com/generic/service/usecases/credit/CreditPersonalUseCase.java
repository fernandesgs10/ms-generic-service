package br.com.generic.service.usecases.credit;


import br.com.generic.service.account.ProposalRequest;
import br.com.generic.service.account.ProposalResponse;

public interface CreditPersonalUseCase {

    ProposalResponse execute(ProposalRequest proposalRequest);

}
