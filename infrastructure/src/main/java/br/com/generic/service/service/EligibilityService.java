package br.com.generic.service.service;

import br.com.generic.service.account.EligibilityRequest;
import br.com.generic.service.account.EligibilityResult;

public interface EligibilityService {


    EligibilityResult checkEligibility(EligibilityRequest eligibilityRequest);
}
