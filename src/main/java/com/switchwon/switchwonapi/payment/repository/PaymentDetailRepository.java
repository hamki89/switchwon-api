package com.switchwon.switchwonapi.payment.repository;

import com.switchwon.switchwonapi.payment.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
    Optional<PaymentDetail> findByPaymentId(String paymentId);
}
