package com.posicube.assignment.querylog.presentation;

import com.posicube.assignment.common.baseResponse.BaseResponse;
import com.posicube.assignment.querylog.application.QueryLogService;
import com.posicube.assignment.querylog.presentation.dtos.QueryRequest;
import com.posicube.assignment.querylog.presentation.dtos.QueryResponse;
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
        queryService.submitQuery(userId, request);

        return null;
    }
}
