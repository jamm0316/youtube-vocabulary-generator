package com.posicube.assignment.users.domain.policy;

import java.math.BigDecimal;

public record ModelUsageDetails(
        String name,
        long tokens,
        BigDecimal price
) {

}
