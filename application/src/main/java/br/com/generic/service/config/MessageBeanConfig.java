package br.com.generic.service.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SuppressWarnings("unused")
@Configuration
public class MessageBeanConfig {

    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {

        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

}
