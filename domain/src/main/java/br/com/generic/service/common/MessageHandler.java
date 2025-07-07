package br.com.generic.service.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageHandler {

    private final Integer status;
    private final String error;
    private final String message;
    private final String path;
}

