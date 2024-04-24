package com.switchwon.switchwonapi.common.exception;

public enum ErrorCode {
    NOT_FOUND_USER(503, "존재하지 않는 사용자입니다."),
    NOT_FOUND_PAYMENT(503, "존재하지 않는 결제정보입니다."),
    NOT_FOUND_PAYMENT_METHOD(503, "존재하지 않는 결제타입니다.");

    private final int statusCode;
    private final String errorMessage;

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    ErrorCode(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }
}
