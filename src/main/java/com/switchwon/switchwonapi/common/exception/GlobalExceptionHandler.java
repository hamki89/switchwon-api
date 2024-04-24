package com.switchwon.switchwonapi.common.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SwitchwonException.class)
    public ResponseEntity<ErrorDto> switchwonExceptionHandler(SwitchwonException ex) {
        return new ResponseEntity<>(ErrorDto.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getErrorCode().getErrorMessage())
                .build(), HttpStatusCode.valueOf(ex.getErrorCode().getStatusCode()));
    }
}
