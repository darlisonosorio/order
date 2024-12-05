package br.com.darlison.order.common.handler;

import br.com.darlison.order.common.exception.APIException;
import br.com.darlison.order.common.exception.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;

import java.security.SignatureException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<APIException> handleException(Throwable throwable) {
        APIException failedResponse = APIException.builder()
                .code("ERROR-500")
                .message("Não foi possível atender a requisição. Erro Interno.")
                .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), throwable.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failedResponse);
    }

    @ExceptionHandler
    public ResponseEntity<APIException> handlerServerInputException(ServerWebInputException e) {
        APIException failedResponse = APIException.builder()
                .message(e.getMessage())
                .code("SERV-EXC-00")
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), failedResponse.getMessage()));
        return ResponseEntity.badRequest().body(failedResponse);
    }

    @ExceptionHandler
    public ResponseEntity<APIException> handlerServerInputException(BadRequestException e) {
        log.error(String.format("Code: %s Message: %s ", e.getFailedResponse().getCode(), e.getFailedResponse().getMessage()));
        return ResponseEntity.badRequest().body(e.getFailedResponse());
    }

    @ExceptionHandler
    public ResponseEntity<APIException> handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        APIException failedResponse = APIException.builder()
                .message(e.getMessage())
                .code("HTTP-EXC-00")
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), failedResponse.getMessage()));
        return ResponseEntity.badRequest().body(failedResponse);
    }

    @ExceptionHandler
    public ResponseEntity<APIException> handlerMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        APIException failedResponse = APIException.builder()
                .message(e.getMessage())
                .code("MISS-EXC-00")
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), failedResponse.getMessage()));
        return ResponseEntity.badRequest().body(failedResponse);
    }

    @ExceptionHandler
    public ResponseEntity<APIException> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        APIException failedResponse = APIException.builder()
                .message(e.getMessage())
                .code("METH-EXC-00")
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), failedResponse.getMessage()));
        return ResponseEntity.badRequest().body(failedResponse);

    }

    @ExceptionHandler
    public ResponseEntity<APIException> handleJsonProcessingException(JsonProcessingException e) {
        APIException failedResponse = APIException.builder()
                .message(e.getMessage())
                .code("JSON-EXC-00")
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), failedResponse.getMessage()));
        return ResponseEntity.badRequest().body(failedResponse);
    }

    @ExceptionHandler
    public ResponseEntity<APIException> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        APIException failedResponse = APIException.builder()
                .message(e.getMessage())
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("HTTP-EXC-00")
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), failedResponse.getMessage()));
        return ResponseEntity.badRequest().body(failedResponse);
    }

    @ExceptionHandler
    public ResponseEntity<APIException> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        APIException failedResponse = APIException.builder()
                .message(e.getMessage())
                .code("METH-EXC-00")
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), failedResponse.getMessage()));
        return ResponseEntity.badRequest().body(failedResponse);
    }

    @ExceptionHandler
    public ResponseEntity<APIException> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("");
        APIException failedResponse = APIException.builder()
                .code("CONS-EXC-00")
                .message(message)
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), failedResponse.getMessage()));
        return ResponseEntity.badRequest().body(failedResponse);
    }

    @ExceptionHandler
    public ResponseEntity<APIException> SignatureException(SignatureException e) {
        APIException failedResponse = APIException.builder()
                .message(e.getMessage())
                .code("SING-EXC-00")
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), failedResponse.getMessage()));
        return ResponseEntity.badRequest().body(failedResponse);
    }

    @ExceptionHandler
    public ResponseEntity<APIException> SignatureBindException(BindException e) {
        List<APIException> errorsList = e.getFieldErrors().stream()
                .map(it -> APIException.builder()
                        .code("BIND-EXC-00")
                        .message(it.getDefaultMessage())
                        .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .build()).collect(Collectors.toList());

        APIException failedResponse = APIException.builder()
                .message("Existem campos com erro no formulário enviado.")
                .code("BIND-EXC-00")
                .errors(errorsList)
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
        log.error(String.format("Code: %s Message: %s ", failedResponse.getCode(), failedResponse.getMessage()));
        return ResponseEntity.badRequest().body(failedResponse);
    }
}
