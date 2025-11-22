package com.personalproject.llmmanager.querylog.port;

public interface LlmPort {
    String query(String url, String model);
}
