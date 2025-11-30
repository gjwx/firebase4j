package com.jfztkg.common.exception;

/**
 * @Author gongjiawei
 * @Date 2025/11/28 23:36
 * @注释
 */

import com.jfztkg.common.ApiResult;
import com.jfztkg.common.ResultCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResult<Void> handleIllegalArgument(IllegalArgumentException e) {
        return ApiResult.failure(ResultCode.PARAM_ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResult<Void> handleException(Exception e) {
        // 这里可以打印日志
        e.printStackTrace();
        return ApiResult.failure(ResultCode.SERVER_ERROR, e.getMessage());
    }
}
