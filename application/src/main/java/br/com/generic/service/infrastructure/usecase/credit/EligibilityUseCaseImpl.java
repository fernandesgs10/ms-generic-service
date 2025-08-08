package br.com.generic.service.infrastructure.usecase.credit;

import br.com.generic.service.account.EligibilityRequest;
import br.com.generic.service.account.EligibilityResult;
import br.com.generic.service.usecases.credit.EligibilityUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EligibilityUseCaseImpl implements EligibilityUseCase {

    @Override
    public EligibilityResult execute(EligibilityRequest eligibilityRequest) {
        EligibilityResult eligibilityResult = new EligibilityResult();
        eligibilityResult.setEligible(Boolean.TRUE);
        //eligibilityResult.setReasons(List.of("protesto serasa"));

        return eligibilityResult;
    }
}
