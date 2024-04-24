package com.switchwon.switchwonapi.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    WAIT("wait"),
    APPROVE("approved");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
