package com.switchwon.switchwonapi.payment.enums;

import com.switchwon.switchwonapi.common.exception.SwitchwonException;
import lombok.Getter;

import static com.switchwon.switchwonapi.common.exception.ErrorCode.NOT_FOUND_PAYMENT_METHOD;

@Getter
public enum PaymentMethod {
    CREDIT_CARD("creditCard");

    private final String methodName;

    PaymentMethod(String paymentMethod) {
        this.methodName = paymentMethod;
    }

    public static PaymentMethod fromString(String methodName) {
        PaymentMethod returnMethod = null;
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.methodName.equalsIgnoreCase(methodName)) {
                returnMethod = method;
            }
        }
        if (returnMethod == null) {
            throw new SwitchwonException(NOT_FOUND_PAYMENT_METHOD);
        }
        return returnMethod;
    }
}
