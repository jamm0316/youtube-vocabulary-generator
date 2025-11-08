package com.posicube.assignment.querylog.application.service;

public interface LlmClientService {
    String query(String query, String model);
}
