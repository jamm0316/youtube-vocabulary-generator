package com.posicube.assignment.querylog.adapter.in.web;

import com.posicube.assignment.common.baseResponse.BaseResponse;
import com.posicube.assignment.querylog.application.commandquery.QueryRequest;
import com.posicube.assignment.querylog.application.commandquery.QueryResponse;
import com.posicube.assignment.querylog.application.service.QueryLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/query")
@RequiredArgsConstructor
public class QueryLogController {
    private final QueryLogService queryService;

    @PostMapping
    public BaseResponse<QueryResponse> submitQuery(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody QueryRequest request) {
        return new BaseResponse<>(queryService.submitQuery(userId, request));
    }
}
