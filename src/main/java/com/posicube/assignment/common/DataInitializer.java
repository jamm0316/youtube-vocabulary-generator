package com.posicube.assignment.common;

import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.port.PlanRepository;
import com.posicube.assignment.plan.domain.vo.PlanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final PlanRepository planRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            if (planRepository.count() == 0) {
                Plan litePlan = Plan.create(PlanType.LITE);
                Plan proPlan = Plan.create(PlanType.PRO);

                planRepository.saveAll(List.of(litePlan, proPlan));
            }
        } catch (DataAccessException e) {
            log.error("Plan 데이터 초기화에 실패했습니다.", e);
            throw new DataAccessException("Plan 초기화 실패", e){};
        }

    }
}
