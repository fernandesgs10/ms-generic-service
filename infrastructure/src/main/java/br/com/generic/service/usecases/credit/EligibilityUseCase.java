package br.com.generic.service.usecases.credit;

import br.com.generic.service.account.EligibilityRequest;
import br.com.generic.service.account.EligibilityResult;

public interface EligibilityUseCase {

    EligibilityResult execute(EligibilityRequest eligibilityRequest);


}
