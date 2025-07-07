package br.com.generic.service.dto;

import lombok.Data;

@Data
public class TokenDto {

    private String token;

    private String expiresIn;

    public String getToken() {
        return token;
    }

}