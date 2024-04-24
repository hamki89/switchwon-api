package com.switchwon.switchwonapi.common.config;

import com.switchwon.switchwonapi.payment.enums.Currency;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    public static final double FEES_FORMULA = 0.03;

    public Double convertAmount(Double amount, Currency currency) {
        if (currency.equals(Currency.USD)) {
            return Double.parseDouble(String.format("%.2f", amount));
        } else {
            return Math.floor(amount);
        }
    }

    public Double getFeesAmount(Double amount) {
        return amount * FEES_FORMULA;
    }
}
