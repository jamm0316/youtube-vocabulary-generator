package com.posicube.assignment.querylog.presentation.dtos;

public record QueryResponse (
        String answer
) {
    public static QueryResponse from(String answer) {
        return new QueryResponse(answer);
    }
}
