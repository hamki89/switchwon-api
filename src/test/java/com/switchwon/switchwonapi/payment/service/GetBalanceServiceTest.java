package com.switchwon.switchwonapi.payment.service;

import com.switchwon.switchwonapi.common.exception.SwitchwonException;
import com.switchwon.switchwonapi.payment.dto.GetBalanceDto;
import com.switchwon.switchwonapi.payment.dto.GetBalanceRequestDto;
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
public class GetBalanceServiceTest {
    @Autowired
    private GetBalanceService getBalanceService;

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
    @DisplayName("잔액 조회 요청 - 성공")
    public void getGetBalanceServiceSuccessTest() {
        GetBalanceDto result = getBalanceService.run(GetBalanceRequestDto.builder().userId(testUser.getId()).build());

        Assertions.assertEquals(testWallet.getBalance(), result.getBalance());
        Assertions.assertEquals(testWallet.getCurrency(), result.getCurrency());
        Assertions.assertEquals(testWallet.getUser().getId(), result.getUserId());
    }

    @Test
    @DisplayName("잔액 조회 요청 실패 - 존재하지 않는 유저")
    public void getGetBalanceServiceFailTest_NotFoundUser() {
        SwitchwonException exception = Assertions.assertThrows(SwitchwonException.class, () -> {
            getBalanceService.run(GetBalanceRequestDto.builder().userId("test1234").build());
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
