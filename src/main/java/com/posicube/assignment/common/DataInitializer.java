package com.posicube.assignment.common;

import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.port.PlanRepository;
import com.posicube.assignment.plan.domain.vo.PlanType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final PlanRepository planRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (planRepository.count() == 0) {
            Plan litePlan = Plan.create(PlanType.LITE);
            Plan proPlan = Plan.create(PlanType.PRO);

            planRepository.saveAll(List.of(litePlan, proPlan));
        }
    }
}
