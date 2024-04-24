package com.switchwon.switchwonapi.payment.service;

import com.switchwon.switchwonapi.common.config.PaymentConfig;
import com.switchwon.switchwonapi.common.exception.SwitchwonException;
import com.switchwon.switchwonapi.payment.dto.PaymentEstimateDto;
import com.switchwon.switchwonapi.payment.dto.PaymentEstimateRequestDto;
import com.switchwon.switchwonapi.payment.enums.Currency;
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

import static com.switchwon.switchwonapi.common.exception.ErrorCode.NOT_FOUND_USER;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
public class PaymentEstimateServiceTest {
    @Autowired
    private PaymentEstimateService paymentEstimateService;

    @Autowired
    private PaymentConfig paymentConfig;

    @Autowired
    private EntityManager entityManager;

    /* 테스트 데이터 */
    private User testUser;
    private Wallet testWallet;

    @BeforeEach
    public void beforeEach() {
        testUser = createTestUser("hong1234", "홍길동");
        testWallet = createTestWallet(testUser);
    }

    @Test
    @DisplayName("결제 예상 결과 조회 요청 - 성공")
    public void paymentEstimateServiceSuccessTest() {
        Double testAmount = 150.0;

        PaymentEstimateDto result = paymentEstimateService.run(PaymentEstimateRequestDto.builder()
                .userId(testUser.getId())
                .amount(testAmount)
                .currency(Currency.USD)
                .merchantId("merchantId1234")
                .build());

        Double fees = paymentConfig.getFeesAmount(testAmount);

        Assertions.assertEquals(paymentConfig.convertAmount(testAmount + fees, Currency.USD), result.getEstimatedTotal());
        Assertions.assertEquals(fees, result.getFees());
        Assertions.assertEquals(Currency.USD, result.getCurrency());
    }

    @Test
    @DisplayName("잔액 조회 요청 실패 - 존재하지 않는 유저")
    public void paymentEstimateServiceFailTest_NotFoundUser() {
        Double testAmount = 150.0;

        SwitchwonException exception = Assertions.assertThrows(SwitchwonException.class, () -> {
            paymentEstimateService.run(PaymentEstimateRequestDto.builder()
                    .userId("test12312412")
                    .amount(testAmount)
                    .currency(Currency.USD)
                    .merchantId("merchantId1234")
                    .build());
        });

        Assertions.assertEquals(NOT_FOUND_USER.name(), exception.getErrorCode().name());
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
}
