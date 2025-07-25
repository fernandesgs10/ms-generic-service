package br.com.generic.service.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@ControllerAdvice
public class HandlerExceptionConfig {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception, WebRequest request) {
        ErrorDto errorDto = createErrorDto(HttpStatus.BAD_REQUEST, exception, request);
        errorDto.setDetailedMessages(exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()));

        log.error("ConstraintViolationException: {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, WebRequest request) {
        ErrorDto errorDto = createErrorDto(HttpStatus.BAD_REQUEST, "Validation error", request);

        List<String> validationMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("Campo '%s': %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        errorDto.setDetailedMessages(validationMessages);

        log.error("MethodArgumentNotValidException: {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception exception, WebRequest request) {
        ErrorDto errorDto = createErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
        log.error("Exception: {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception, WebRequest request) {
        ErrorDto errorDto = createErrorDto(HttpStatus.BAD_REQUEST, exception, request);
        log.error("IllegalStateException: {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException exception, WebRequest request) {
        ErrorDto errorDto = createErrorDto(HttpStatus.BAD_REQUEST, exception, request);
        log.error("IllegalStateException: {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<Object> handleInvalidDataAccessApiUsage(InvalidDataAccessApiUsageException exception, WebRequest request) {
        ErrorDto errorDto = createErrorDto(HttpStatus.BAD_REQUEST, exception, request);
        log.error("InvalidDataAccessApiUsageException: {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    private ErrorDto createErrorDto(HttpStatus status, Throwable exception, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        return new ErrorDto(status, exception.getMessage(), servletWebRequest.getRequest().getRequestURL().toString());
    }

    private ErrorDto createErrorDto(HttpStatus status, String message, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        return new ErrorDto(status, message, servletWebRequest.getRequest().getRequestURL().toString());
    }

    private String extractDetailFromMessage(String message, String detailKey) {
        String detail = "";
        String[] parts = message.split(detailKey + " ");
        if (parts.length > 1) {
            detail = parts[1].split(" ")[0];
        }
        return detail;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDto {
        private final int status;
        private final String error;
        private final String message;
        private final String path;
        private List<String> detailedMessages;

        public ErrorDto(HttpStatus status, String message, String path) {
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.message = message;
            this.path = path;
        }
    }
}
