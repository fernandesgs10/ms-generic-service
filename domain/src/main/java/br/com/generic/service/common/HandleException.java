package br.com.generic.service.common;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

public class HandleException {

    public static void exceptions(Throwable ex) {
        if (ex.getCause() != null) {
            Throwable cause = ex.getCause();
        } else {
                handleUnknownException(ex);
            }
    }

    private static void handleBadRequest(HttpClientErrorException.BadRequest ex) {
        throw ex;
    }

    private static void handleServerError(HttpServerErrorException ex) {
        throw ex;
    }

    private static void handleUnknownException(Throwable ex) {
        throw new RuntimeException("Unhandled exception", ex);
    }
}
