package br.com.generic.service.infrastructure.usecase.credit;

import br.com.generic.service.account.ProposalRequest;
import br.com.generic.service.account.ProposalResponse;
import br.com.generic.service.usecases.credit.CreditPersonalUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CreditPersonalUseCaseImpl implements CreditPersonalUseCase {
    @Override
    public ProposalResponse execute(ProposalRequest proposalRequest) {
        return null;
    }
}
