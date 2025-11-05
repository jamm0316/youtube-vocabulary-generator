package com.posicube.assignment.common.utils;

import org.springframework.stereotype.Component;

@Component
public class TokenCalculator {
    private static final double CHARS_TO_TOKENS_RATIO = 0.75;

    /**
     * 주어진 프롬프트 문자열로부터 예상 토큰 수를 계산합니다.
     * 토큰 계산 공식: 문자열 길이 * 0.75 (소수점 첫쨰 자리에서 반올림)
     *
     * @param prompt 토큰 수를 계산할 프롬프트 문자열
     * @return 계산된 토큰 수
     */
    public Long calculateTokensFromPrompt(String prompt) {
        if (prompt == null || prompt.isEmpty()) {
            return 0L;
        }
        return Math.round(prompt.trim().length() * CHARS_TO_TOKENS_RATIO);
    }
}
