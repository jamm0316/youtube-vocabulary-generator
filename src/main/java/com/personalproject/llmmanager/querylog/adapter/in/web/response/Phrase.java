package com.personalproject.llmmanager.querylog.adapter.in.web.response;

import java.util.List;

public record Phrase(
        String phrase,
        List<String> meanings,
        String example
) {
}
