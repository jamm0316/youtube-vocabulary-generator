package com.posicube.assignment.domain.users;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.vo.PlanType;
import com.posicube.assignment.users.domain.entity.Users;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UsersTest {
    private Users user;

    @BeforeEach
    void setUp() {
        String account = "evanbackeng@gmail.com";
        String password = "pass1234";
        String name = "evan";
        Plan plan = Plan.create(PlanType.LITE);
        user = Users.create(account, password, name, plan);
    }

    @Test
    @DisplayName("생성 성공: User 정상 생성")
    public void create_user_success() throws Exception {
        //then
        assertThat(user.getAccount()).isEqualTo("evanbackeng@gmail.com");
        assertThat(user.getPassword()).isEqualTo("pass1234");
        assertThat(user.getName()).isEqualTo("evan");
        assertThat(user.getPlan().getType()).isEqualTo(PlanType.LITE);
        assertThat(user.getTokens().getQuota()).isEqualTo(PlanType.LITE.getQuota());
        assertThat(user.getTokens().getRemainingTokens()).isEqualTo(PlanType.LITE.getQuota());
    }

    @Test
    @DisplayName("생성 실패: User account, password, name이 null이면 예외 발생")
    public void create_user_account_password_name_cannot_null_fail() throws Exception {
        //when&then
        assertThatThrownBy(() -> Users.create(
                null, "pass1234", "evan", Plan.create(PlanType.LITE)))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.ACCOUNT_CANNOT_BE_NULL.getMessage());

        assertThatThrownBy(() -> Users.create(
                "evanbackeng@gmail.com", null, "evan", Plan.create(PlanType.LITE)))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.PASSWORD_CANNOT_BE_NULL.getMessage());

        assertThatThrownBy(() -> Users.create(
                "evanbackeng@gmail.com", "pass1234", null, Plan.create(PlanType.LITE)))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.NAME_CANNOT_BE_NULL.getMessage());
    }

    @Test
    @DisplayName("생성 실패: User account, password, name이 공백이 포함되면 예외 발생")
    public void create_user_account_password_name_cannot_white_space_fail() throws Exception {
        //when&then
        assertThatThrownBy(() -> Users.create(
                "ev an  backeng@  gmail.    com", "pass1234", "evan", Plan.create(PlanType.LITE)))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.ACCOUNT_CANNOT_CONTAIN_WHITESPACE.getMessage());

        assertThatThrownBy(() -> Users.create(
                "evanbackeng@gmail.com", "p as  s12   34", "evan", Plan.create(PlanType.LITE)))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.PASSWORD_CANNOT_CONTAIN_WHITESPACE.getMessage());

        assertThatThrownBy(() -> Users.create(
                "evanbackeng@gmail.com", "pass1234", "e v  a   n", Plan.create(PlanType.LITE)))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.NAME_CANNOT_CONTAIN_WHITESPACE.getMessage());
    }
}
