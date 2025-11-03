package com.posicube.assignment.domain.user;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.entity.PlanType;
import com.posicube.assignment.user.domain.entity.User;
import com.posicube.assignment.user.exception.UserExceptionStatus;
import com.posicube.assignment.user.presentation.dtos.UserCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    @Test
    @DisplayName("생성 성공: User 정상 생성")
    public void create_user_success() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.create(
                "evanbackeng@gmail.com", "pass1234", "evan", PlanType.LITE);
        User user = User.create(request);

        //then
        assertThat(user.getAccount()).isEqualTo("evanbackeng@gmail.com");
        assertThat(user.getPassword()).isEqualTo("pass1234");
        assertThat(user.getName()).isEqualTo("evan");
        assertThat(user.getPlan().getType()).isEqualTo(PlanType.LITE);
    }

    @Test
    @DisplayName("생성 실패: User account, password, name이 null이면 예외 발생")
    public void create_user_account_password_name_cannot_null_fail() throws Exception {
        //given
        UserCreateRequest accountNull = UserCreateRequest.create(
                null, "pass1234", "evan", PlanType.LITE);
        UserCreateRequest passwordNull = UserCreateRequest.create(
                "evanbackeng@gmail.com", null, "evan", PlanType.LITE);
        UserCreateRequest nameNull = UserCreateRequest.create(
                "evanbackeng@gmail.com", "pass1234", null, PlanType.LITE);

        //when&then
        assertThatThrownBy(() -> User.create(accountNull))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.ACCOUNT_CANNOT_BE_NULL.getMessage());

        assertThatThrownBy(() -> User.create(passwordNull))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.PASSWORD_CANNOT_BE_NULL.getMessage());

        assertThatThrownBy(() -> User.create(nameNull))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.NAME_CANNOT_BE_NULL.getMessage());
    }

    @Test
    @DisplayName("생성 실패: User account, password, name이 공백이 포함되면 예외 발생")
    public void create_user_account_password_name_cannot_white_space_fail() throws Exception {
        //given
        UserCreateRequest accountWhiteSpace = UserCreateRequest.create(
                "ev an  backeng@  gmail.    com", "pass1234", "evan", PlanType.LITE);
        UserCreateRequest passwordWhiteSpace = UserCreateRequest.create(
                "evanbackeng@gmail.com", "p as  s12   34", "evan", PlanType.LITE);
        UserCreateRequest nameWhiteSpace = UserCreateRequest.create(
                "evanbackeng@gmail.com", "pass1234", "e v  a   n", PlanType.LITE);

        //when&then
        assertThatThrownBy(() -> User.create(accountWhiteSpace))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.ACCOUNT_CANNOT_CONTAIN_WHITESPACE.getMessage());

        assertThatThrownBy(() -> User.create(passwordWhiteSpace))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.PASSWORD_CANNOT_CONTAIN_WHITESPACE.getMessage());

        assertThatThrownBy(() -> User.create(nameWhiteSpace))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.NAME_CANNOT_CONTAIN_WHITESPACE.getMessage());
    }
}
