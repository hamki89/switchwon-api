package com.switchwon.switchwonapi.payment.service;

import com.switchwon.switchwonapi.common.config.PaymentConfig;
import com.switchwon.switchwonapi.common.exception.SwitchwonException;
import com.switchwon.switchwonapi.payment.dto.PaymentApprovalDto;
import com.switchwon.switchwonapi.payment.dto.PaymentApprovalRequestDto;
import com.switchwon.switchwonapi.payment.dto.PaymentDetailDto;
import com.switchwon.switchwonapi.payment.entity.Payment;
import com.switchwon.switchwonapi.payment.entity.PaymentDetail;
import com.switchwon.switchwonapi.payment.enums.PaymentMethod;
import com.switchwon.switchwonapi.payment.enums.PaymentStatus;
import com.switchwon.switchwonapi.payment.repository.PaymentDetailRepository;
import com.switchwon.switchwonapi.payment.repository.PaymentRepository;
import com.switchwon.switchwonapi.wallet.entity.Wallet;
import com.switchwon.switchwonapi.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.switchwon.switchwonapi.common.exception.ErrorCode.NOT_FOUND_PAYMENT;
import static com.switchwon.switchwonapi.common.exception.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class PaymentApprovalService {
    private final WalletRepository walletRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final PaymentConfig paymentConfig;

    @Transactional
    public PaymentApprovalDto run(PaymentApprovalRequestDto input) {
        Wallet wallet = walletRepository.findByUserId(input.getUserId()).orElseThrow(() -> {
            throw new SwitchwonException(NOT_FOUND_USER);
        });

        Payment payment = paymentRepository.findByMerchantIdAndUserIdAndCurrencyAndPaymentStatus(input.getMerchantId(), input.getUserId(), input.getCurrency(), PaymentStatus.WAIT.getStatus()).orElseThrow(() -> {
            throw new SwitchwonException(NOT_FOUND_PAYMENT);
        });

        Double amountTotal = payment.getAmount() + payment.getFees();

        // 결제완료 처리
        completePayment(payment, wallet, amountTotal, input);

        return PaymentApprovalDto.builder()
                .paymentId(payment.getId())
                .status(PaymentStatus.APPROVE.getStatus())
                .amountTotal(paymentConfig.convertAmount(amountTotal, payment.getCurrency()))
                .currency(payment.getCurrency())
                .timestamp(payment.getPaymentCompleteTime())
                .build();
    }

    private void completePayment(Payment payment, Wallet wallet, Double amountTotal, PaymentApprovalRequestDto input) {
        Double chargeAmount = getChargePoint(wallet.getBalance(), amountTotal);

        if (input.getPaymentDetails() != null) {
            PaymentDetail paymentDetail = paymentDetailRepository.findByPaymentId(payment.getId()).orElseThrow(() -> {
                throw new SwitchwonException(NOT_FOUND_PAYMENT);
            });

            PaymentDetailDto paymentDetailDto = input.getPaymentDetails();
            paymentDetail.setCardInfo(paymentDetailDto.getCardNumber(), paymentDetailDto.getExpiryDate(), paymentDetailDto.getCvv(), chargeAmount);
            paymentDetailRepository.save(paymentDetail);
        }

        payment.setPaymentCompleteTime(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.APPROVE.getStatus());
        payment.setPaymentMethod(PaymentMethod.fromString(input.getPaymentMethod()).getMethodName());
        paymentRepository.save(payment);

        // 포인트 차감
        wallet.setBalance(walletBalanceDecrementAndBalanceReturn(amountTotal, chargeAmount, wallet.getBalance()));
        walletRepository.save(wallet);
    }

    private Double getChargePoint(Double walletBalance, Double amountTotal) {
        if (walletBalance < amountTotal) {
            return amountTotal - walletBalance;
        }
        return 0.0;
    }

    private Double walletBalanceDecrementAndBalanceReturn(Double amountTotal, Double walletBalance, Double chargeAmount) {
        return (walletBalance + chargeAmount) - amountTotal;
    }
}
