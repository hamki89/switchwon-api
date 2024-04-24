package com.switchwon.switchwonapi.payment.service;

import com.switchwon.switchwonapi.common.config.PaymentConfig;
import com.switchwon.switchwonapi.common.exception.SwitchwonException;
import com.switchwon.switchwonapi.payment.dto.PaymentEstimateDto;
import com.switchwon.switchwonapi.payment.dto.PaymentEstimateRequestDto;
import com.switchwon.switchwonapi.payment.entity.Payment;
import com.switchwon.switchwonapi.payment.entity.PaymentDetail;
import com.switchwon.switchwonapi.payment.enums.PaymentStatus;
import com.switchwon.switchwonapi.payment.repository.PaymentDetailRepository;
import com.switchwon.switchwonapi.payment.repository.PaymentRepository;
import com.switchwon.switchwonapi.user.entity.User;
import com.switchwon.switchwonapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.switchwon.switchwonapi.common.exception.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class PaymentEstimateService {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final PaymentConfig paymentConfig;

    public PaymentEstimateDto run(PaymentEstimateRequestDto input) {
        User user = userRepository.findById(input.getUserId()).orElseThrow(() -> {
            throw new SwitchwonException(NOT_FOUND_USER);
        });

        Optional<Payment> optionalPayment = paymentRepository.findByMerchantIdAndUserIdAndCurrencyAndPaymentStatus(input.getMerchantId(), input.getUserId(), input.getCurrency(), PaymentStatus.WAIT.getStatus());
        Payment payment = null;
        if (optionalPayment.isPresent()) {
            payment = optionalPayment.get();
        } else {
            payment = Payment.builder()
                    .amount(input.getAmount())
                    .fees(paymentConfig.getFeesAmount(input.getAmount()))
                    .currency(input.getCurrency())
                    .merchantId(input.getMerchantId())
                    .user(user)
                    .paymentStatus(PaymentStatus.WAIT.getStatus())
                    .build();
            paymentRepository.save(payment);
            paymentDetailRepository.save(PaymentDetail.builder()
                    .payment(payment)
                    .build());
        }

        Double estimatedTotal = paymentConfig.convertAmount(payment.getAmount() + payment.getFees(), payment.getCurrency());

        return PaymentEstimateDto.builder()
                .estimatedTotal(estimatedTotal)
                .fees(payment.getFees())
                .currency(payment.getCurrency())
                .build();
    }
}
