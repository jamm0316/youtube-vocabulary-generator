package com.llmmanager.assignment.domain.users;

import com.llmmanager.assignment.common.exception.BaseException;
import com.llmmanager.assignment.plan.application.service.PlanService;
import com.llmmanager.assignment.plan.domain.model.Plan;
import com.llmmanager.assignment.plan.domain.model.PlanType;
import com.llmmanager.assignment.users.application.commandquery.UserCreateRequest;
import com.llmmanager.assignment.users.application.service.UsersService;
import com.llmmanager.assignment.users.domain.model.Users;
import com.llmmanager.assignment.users.exception.UserExceptionStatus;
import com.llmmanager.assignment.users.port.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private PlanService planService;
    @InjectMocks
    private UsersService usersService;

    @Test
    @DisplayName("createUser: 새로운 사용자를 성공적으로 생성하고 저장한다.")
    public void createUser() {
        //given
        UserCreateRequest request = new UserCreateRequest("hysic88", "123456", "현식", "LITE");
        Plan plan = Plan.create(PlanType.LITE);

        when(planService.findPlanByType(any(PlanType.class))).thenReturn(Optional.of(plan));
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Users createdUser = usersService.createUser(request);

        //then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getAccount()).isEqualTo("hysic88");
        assertThat(createdUser.getPassword()).isEqualTo("123456");
        assertThat(createdUser.getName()).isEqualTo("현식");
        assertThat(createdUser.getPlanType()).isEqualTo(PlanType.LITE);

        // planService.createPlan()이 정확히 1번 호출되었는지 검증
        verify(planService).findPlanByType(PlanType.from("LITE"));

        // userRepository.save()에 어떤 Users 객체가 전달되었는지 캡처 후 검증
        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(usersRepository).save(userCaptor.capture());
        Users savedUser = userCaptor.getValue();

        assertThat(savedUser.getAccount()).isEqualTo("hysic88");
        assertThat(savedUser.getPassword()).isEqualTo("123456");
    }

    @Test
    @DisplayName("유저 생성 실패: 이미 계정이 존재할 경우 예외를 반환한다.")
    public void createUser_fail() {
        //given
        UserCreateRequest request = new UserCreateRequest("hysic88", "123456", "현식", "LITE");
        when(usersRepository.findUsersByAccount(request.account())).thenReturn(Optional.of(mock(Users.class)));

        //when&then
        assertThatThrownBy(() -> usersService.createUser(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.DUPLICATE_ACCOUNT.getMessage());

        //then
        verify(usersRepository, never()).save(any(Users.class));

    }
}
