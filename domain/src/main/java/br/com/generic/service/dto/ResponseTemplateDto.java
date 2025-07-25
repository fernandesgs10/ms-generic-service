package br.com.generic.service.dto;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class ResponseTemplateDto <T>{

    public static ResponseDto createResponse(Object data, HttpStatus status) {
        return createResponseTemplate(data, status, null);
    }

    public static ResponseDto createResponse(Page<?> page, HttpStatus status) {
        return createResponseTemplate(null, status, page);
    }

    private static ResponseDto createResponseTemplate(Object data, HttpStatus status, Page<?> page) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setHttpStatus(status);
        responseDto.setDate(new Date());

        if (page != null) {
            responseDto.setDatas(page);
        } else {
            responseDto.setData(data);
        }

        return responseDto;
    }


}
