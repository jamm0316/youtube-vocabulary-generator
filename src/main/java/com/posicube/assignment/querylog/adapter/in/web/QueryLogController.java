package com.posicube.assignment.querylog.adapter.in.web;

import com.posicube.assignment.querylog.application.commandquery.QueryRequest;
import com.posicube.assignment.querylog.application.commandquery.QueryResponse;
import com.posicube.assignment.querylog.application.facade.QueryLogFacade;
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
