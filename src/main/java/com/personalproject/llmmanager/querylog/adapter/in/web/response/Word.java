package com.personalproject.llmmanager.querylog.adapter.in.web.response;

import java.util.List;

public record Word(
        String word,
        List<String> meanings,
        String partsOfSpeech,
        List<String> synonyms,
        List<String> examples
) {
}
