package com.personalproject.llmmanager.querylog.adapter.in.web;

import com.personalproject.llmmanager.querylog.application.commandquery.QueryRequest;
import com.personalproject.llmmanager.querylog.application.commandquery.QueryResponse;
import com.personalproject.llmmanager.querylog.application.facade.QueryLogFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/query")
@RequiredArgsConstructor
public class QueryLogController {
    private final QueryLogFacade queryLogFacade;

    @PostMapping
    public QueryResponse submitQuery(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody QueryRequest request) {
        return queryLogFacade.submitQuery(userId, request);
    }
}
