package com.personalproject.llmmanager.querylog.application.commandquery;

import java.util.List;

public record Phrase(
        String phrase,
        List<String> meanings,
        String example
) {
}
