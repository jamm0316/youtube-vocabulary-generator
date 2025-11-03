package com.posicube.assignment.common.exception;

import com.posicube.assignment.common.baseResponse.BaseResponse;
import com.posicube.assignment.common.baseResponse.BaseResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * BaseException (커스텀 예외) 발생 시 처리하는 핸들러
     */
    @ExceptionHandler(BaseException.class)
    public BaseResponse<Object> handleBaseException(BaseException e, HttpServletRequest request) {
        return new BaseResponse<>(e.getStatus());
    }

    /**
     * 그 외 예상하지 못한 Exception 발생 시 처리하는 핸들러
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> handleException(Exception e) {
        e.printStackTrace();
        return new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * DTO @Valid 실패 시 발생하는 Validation 에러 처리 핸들러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new BaseResponse<>(BaseResponseStatus.VALIDATION_ERROR, errorMessage);
    }
}
