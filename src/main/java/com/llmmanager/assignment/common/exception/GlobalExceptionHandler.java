package com.llmmanager.assignment.common.exception;

import com.llmmanager.assignment.common.baseResponse.BaseResponse;
import com.llmmanager.assignment.common.baseResponse.BaseResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * BaseException (커스텀 예외) 발생 시 처리하는 핸들러
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<Object>> handleBaseException(BaseException e, HttpServletRequest request) {
        BaseResponse<Object> errorResponse = new BaseResponse<>(e.getStatus());
        return new ResponseEntity<>(errorResponse,e.getStatus().getHttpStatus());
    }

    /**
     * 그 외 예상하지 못한 Exception 발생 시 처리하는 핸들러
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleException(Exception e) {
        e.printStackTrace();
        BaseResponse<Object> errorResponse = new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR.getHttpStatus());
        return new ResponseEntity<>(errorResponse, BaseResponseStatus.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

    /**
     * DTO @Valid 실패 시 발생하는 Validation 에러 처리 핸들러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        BaseResponse<Object> errorResponse = new BaseResponse<>(BaseResponseStatus.VALIDATION_ERROR, errorMessage);
        return new ResponseEntity<>(errorResponse, BaseResponseStatus.VALIDATION_ERROR.getHttpStatus());
    }
}
