package br.com.generic.service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("unused")
@Configuration
public class ConverterConfig {

    @Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}




}
