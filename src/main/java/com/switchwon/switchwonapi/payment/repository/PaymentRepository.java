package com.switchwon.switchwonapi.payment.repository;

import com.switchwon.switchwonapi.payment.entity.Payment;
import com.switchwon.switchwonapi.payment.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByMerchantIdAndUserIdAndCurrencyAndPaymentStatus(String merchantId, String userId, Currency currency, String paymentStatus);
}
