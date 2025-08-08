package br.com.generic.service.config;

import br.com.generic.service.account.EligibilityResult;

public class NotEligibleException extends RuntimeException {
    public NotEligibleException(String message) {
        super(message);
    }

        public NotEligibleException(EligibilityResult result) {
            super("Cliente não elegível para o crédito. Motivos: " + String.join(", ", result.getReasons()));
        }
}
