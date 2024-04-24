package com.switchwon.switchwonapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwitchwonException extends RuntimeException {
    private final ErrorCode errorCode;
}
