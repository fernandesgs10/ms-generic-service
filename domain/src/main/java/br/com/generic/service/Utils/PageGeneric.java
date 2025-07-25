package br.com.generic.service.Utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.function.Function;
import java.util.stream.Collectors;

public class PageGeneric<T> {

    protected Page<T> convertPage(Page<?> pagina, Function<Object, T> converter) {

        return new PageImpl<>(
                pagina.getContent().stream().map(converter).collect(Collectors.toList()),
                PageRequest.of(pagina.getNumber(), pagina.getSize()), pagina.getTotalElements());
    }
}
