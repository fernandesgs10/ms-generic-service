package br.com.generic.service.generate;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.function.Function;
import java.util.stream.Collectors;

public class Page<T> {


    protected org.springframework.data.domain.Page<T> convertPage(org.springframework.data.domain.Page<?> pagina, Function<Object, T> converter) {

        return new PageImpl<>(
                pagina.getContent().stream().map(converter).collect(Collectors.toList()),
                PageRequest.of(pagina.getNumber(), pagina.getSize()), pagina.getTotalElements());
    }
}
