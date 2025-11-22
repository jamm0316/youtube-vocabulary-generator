package com.personalproject.llmmanager.querylog.application.commandquery;

import java.util.List;

public record VocaResponse(
        String videoId,
        List<Word> words,
        List<Phrase> phrases
) {
}
