package com.posicube.assignment.domain.users;

import com.posicube.assignment.plan.application.PlanService;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.vo.PlanType;
import com.posicube.assignment.users.application.UserService;
import com.posicube.assignment.users.domain.entity.Users;
import com.posicube.assignment.users.domain.port.UserRepository;
import com.posicube.assignment.users.presentation.dtos.UserCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PlanService planService;
    @InjectMocks private UserService userService;

    @Test
    @DisplayName("createUser: 새로운 사용자를 성공적으로 생성하고 저장한다.")
    public void createUser() throws Exception {
        //given
        UserCreateRequest request = new UserCreateRequest("hysic88", "123456", "현식", "LITE");
        Plan plan = Plan.create(PlanType.LITE);

        when(planService.createPlan(anyString())).thenReturn(plan);
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Users createdUser = userService.createUser(request);

        //then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getAccount()).isEqualTo("hysic88");
        assertThat(createdUser.getPassword()).isEqualTo("123456");
        assertThat(createdUser.getName()).isEqualTo("현식");
        assertThat(createdUser.getPlan().getType()).isEqualTo(PlanType.LITE);

        // userRepository.save()가 정확히 1번 호출되었는지 검증
        verify(userRepository).save(any(Users.class));
        verify(planService).createPlan("LITE");

        // userRepository.save()에 어떤 Users 객체가 전달되었는지 캡처 후 검증
        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(userRepository).save(userCaptor.capture());
        Users savedUser = userCaptor.getValue();

        assertThat(savedUser.getAccount()).isEqualTo("hysic88");
        assertThat(savedUser.getPassword()).isEqualTo("123456");
    }
}
