package com.posicube.assignment.querylog.port;

public interface LlmPort {
    String query(String query, String model);
}
