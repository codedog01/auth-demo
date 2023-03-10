package com.lengao.auth.interceptor;

import com.lengao.auth.config.BusinessException;
import com.lengao.auth.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Component
@Slf4j
public class SysExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public Result<Object> exceptionGet(Exception e, HttpServletRequest request, HttpServletResponse response) {
        BusinessException ex = (BusinessException) e;
        log.error(ex.getErrCode() + ex.getMessage());
        return Result.ofFail(ex.getErrCode(), ex.getMessage(), ex.getData());
    }
}
