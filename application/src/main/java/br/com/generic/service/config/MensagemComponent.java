package br.com.generic.service.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MensagemComponent {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String chaveMensagem) {
        return messageSource.getMessage(chaveMensagem, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String chaveMensagem, Object... ids) {
        return messageSource.getMessage(chaveMensagem, ids, LocaleContextHolder.getLocale());
    }
}
