package com.personalproject.llmmanager.querylog.adapter.in.web.response;

import java.util.List;

public record VocaResponse(
        String videoId,
        List<Word> words,
        List<Phrase> phrases
) {
}
