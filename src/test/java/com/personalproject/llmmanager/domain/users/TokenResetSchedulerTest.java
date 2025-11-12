package com.personalproject.llmmanager.domain.users;

import com.personalproject.llmmanager.plan.domain.model.PlanType;
import com.personalproject.llmmanager.users.application.commandquery.UserCreateRequest;
import com.personalproject.llmmanager.users.application.commandquery.UserInfoResponse;
import com.personalproject.llmmanager.users.application.service.TokenResetScheduler;
import com.personalproject.llmmanager.users.application.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TokenResetSchedulerTest {
    @Autowired
    private TokenResetScheduler tokenResetScheduler;
    @Autowired
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        usersService.createUser(new UserCreateRequest("test1", "test1234", "evan", "LITE"));
        usersService.createUser(new UserCreateRequest("test2", "test5678", "sofia", "PRO"));
    }

    @Test
    @DisplayName("모든 사용자의 토큰 사용량을 성공적으로 초기화 한다.")
    public void resetAllUsersTokens_shouldReset() {
        //given
        tokenResetScheduler.resetAllUserTokens();

        //when
        List<UserInfoResponse> allUsersInfo = usersService.findAllUsersInfo();

        //then
        assertThat(allUsersInfo.size()).isEqualTo(2);
        allUsersInfo.forEach(user -> {
            if (user.plan().equals(PlanType.LITE)) {
                assertThat(user.tokens().getRemainingTokens()).isEqualTo(PlanType.LITE.getQuota());
            } else {
                assertThat(user.tokens().getRemainingTokens()).isEqualTo(PlanType.PRO.getQuota());
            }
            assertThat(user.tokens().getUsedTokens()).isEqualTo(0);
        });
    }
}
