package br.com.generic.service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractController<T> {

    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Page<T> convertEntityPage(Page<?> entidades, Function<Object, T> converter) {
        return new PageImpl<>(
                entidades.getContent().stream().map(converter).collect(Collectors.toList()),
                PageRequest.of(entidades.getNumber(), entidades.getSize()), entidades.getTotalElements());
    }
}
