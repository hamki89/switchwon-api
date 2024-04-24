package com.switchwon.switchwonapi.payment.service;

import com.switchwon.switchwonapi.common.config.PaymentConfig;
import com.switchwon.switchwonapi.common.exception.SwitchwonException;
import com.switchwon.switchwonapi.payment.dto.PaymentApprovalDto;
import com.switchwon.switchwonapi.payment.dto.PaymentApprovalRequestDto;
import com.switchwon.switchwonapi.payment.dto.PaymentDetailDto;
import com.switchwon.switchwonapi.payment.entity.Payment;
import com.switchwon.switchwonapi.payment.entity.PaymentDetail;
import com.switchwon.switchwonapi.payment.enums.Currency;
import com.switchwon.switchwonapi.payment.enums.PaymentMethod;
import com.switchwon.switchwonapi.payment.enums.PaymentStatus;
import com.switchwon.switchwonapi.user.entity.User;
import com.switchwon.switchwonapi.wallet.entity.Wallet;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.switchwon.switchwonapi.common.exception.ErrorCode.*;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
public class PaymentApprovalServiceTest {
    @Autowired
    private PaymentApprovalService paymentApprovalService;

    @Autowired
    private PaymentConfig paymentConfig;

    @Autowired
    private EntityManager entityManager;

    /* 테스트 데이터 */
    private User testUser;
    private Wallet testWallet;
    private Payment testPayment;
    private Payment testPayment2;
    private PaymentDetail testPaymentDetail;
    private PaymentDetail testPaymentDetail2;

    @BeforeEach
    public void beforeEach() {
        testUser = createTestUser("hong1234", "홍길동");
        testWallet = createTestWallet(testUser);
        testPayment = createTestPayment(150.0, "merchantId1234", Currency.USD, testUser);
        testPayment2 = createTestPayment(350.0, "merchantId1234", Currency.KRW, testUser);
        testPaymentDetail = createTestPaymentDetail(testPayment);
        testPaymentDetail2 = createTestPaymentDetail(testPayment2);
    }

    @Test
    @DisplayName("결제 승인 요청 - 성공")
    public void paymentApprovalServiceSuccessTest() {
        Double testAmount = testPayment.getAmount();
        Double testFees = testPayment.getFees();
        PaymentApprovalRequestDto request = PaymentApprovalRequestDto.builder()
                .userId(testUser.getId())
                .amount(testAmount)
                .currency(testPayment.getCurrency())
                .merchantId(testPayment.getMerchantId())
                .paymentMethod(PaymentMethod.CREDIT_CARD.getMethodName())
                .paymentDetails(PaymentDetailDto.builder()
                        .cardNumber("1234-5678-9123-4567")
                        .expiryDate("12/24")
                        .cvv("123")
                        .build())
                .build();
        PaymentApprovalDto result = paymentApprovalService.run(request);

        Assertions.assertEquals(testPayment.getId(), result.getPaymentId());
        Assertions.assertEquals(paymentConfig.convertAmount((testAmount + testFees), testPayment.getCurrency()), result.getAmountTotal());
        Assertions.assertEquals(PaymentStatus.APPROVE.getStatus(), result.getStatus());
    }

    @Test
    @DisplayName("결제 승인 요청 실패 - 존재하지 않는 유저")
    public void paymentApprovalServiceFailTest_NotFoundUser() {
        Double testAmount = testPayment.getAmount();
        Double testFees = testPayment.getFees();
        PaymentApprovalRequestDto request = PaymentApprovalRequestDto.builder()
                .userId("test1234")
                .amount(testAmount)
                .currency(testPayment.getCurrency())
                .merchantId(testPayment.getMerchantId())
                .paymentMethod(PaymentMethod.CREDIT_CARD.getMethodName())
                .paymentDetails(PaymentDetailDto.builder()
                        .cardNumber("1234-5678-9123-4567")
                        .expiryDate("12/24")
                        .cvv("123")
                        .build())
                .build();

        SwitchwonException exception = Assertions.assertThrows(SwitchwonException.class, () -> {
            paymentApprovalService.run(request);
        });
        Assertions.assertEquals(NOT_FOUND_USER.name(), exception.getErrorCode().name());
    }

    @Test
    @DisplayName("결제 승인 요청 실패 - 존재하지 않는 결제")
    public void paymentApprovalServiceFailTest_NotFoundPayment() {
        Double testAmount = testPayment.getAmount();
        Double testFees = testPayment.getFees();
        PaymentApprovalRequestDto request = PaymentApprovalRequestDto.builder()
                .userId(testUser.getId())
                .amount(testAmount)
                .currency(testPayment.getCurrency())
                .merchantId("merchant303030")
                .paymentMethod(PaymentMethod.CREDIT_CARD.getMethodName())
                .paymentDetails(PaymentDetailDto.builder()
                        .cardNumber("1234-5678-9123-4567")
                        .expiryDate("12/24")
                        .cvv("123")
                        .build())
                .build();

        SwitchwonException exception = Assertions.assertThrows(SwitchwonException.class, () -> {
            paymentApprovalService.run(request);
        });
        Assertions.assertEquals(NOT_FOUND_PAYMENT.name(), exception.getErrorCode().name());
    }

    @Test
    @DisplayName("결제 승인 요청 실패 - 존재하지 않는 결제타입")
    public void paymentApprovalServiceFailTest_NotFoundPaymentMethod() {
        Double testAmount = testPayment.getAmount();
        Double testFees = testPayment.getFees();
        PaymentApprovalRequestDto request = PaymentApprovalRequestDto.builder()
                .userId(testUser.getId())
                .amount(testAmount)
                .currency(testPayment.getCurrency())
                .merchantId(testPayment.getMerchantId())
                .paymentMethod("applepay")
                .paymentDetails(PaymentDetailDto.builder()
                        .cardNumber("1234-5678-9123-4567")
                        .expiryDate("12/24")
                        .cvv("123")
                        .build())
                .build();

        SwitchwonException exception = Assertions.assertThrows(SwitchwonException.class, () -> {
            paymentApprovalService.run(request);
        });
        Assertions.assertEquals(NOT_FOUND_PAYMENT_METHOD.name(), exception.getErrorCode().name());
    }


    /* 테스트 데이터 생성*/
    private User createTestUser(String userId, String userName) {
        User user = User.builder()
                .id(userId)
                .name(userName)
                .build();
        entityManager.persist(user);
        return user;
    }

    private Wallet createTestWallet(User user) {
        Wallet wallet = Wallet.builder()
                .balance(100.0)
                .user(user)
                .build();
        entityManager.persist(wallet);
        return wallet;
    }

    private Payment createTestPayment(Double amount, String merchantId, Currency currency, User user) {
        Payment payment = Payment.builder()
                .amount(amount)
                .fees(paymentConfig.getFeesAmount(amount))
                .currency(currency)
                .merchantId(merchantId)
                .user(user)
                .paymentStatus(PaymentStatus.WAIT.getStatus())
                .build();
        entityManager.persist(payment);
        return payment;
    }

    private PaymentDetail createTestPaymentDetail(Payment payment) {
        PaymentDetail paymentDetail = PaymentDetail.builder()
                .payment(payment)
                .build();
        entityManager.persist(paymentDetail);
        return paymentDetail;
    }
}
