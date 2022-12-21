package com.lengao.auth.security;

import com.lengao.auth.config.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 当用户试图访问未经授权的接口时，会到这里处理。
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-19
 */
@Component
public class UserAuthAccessDeniedHandler implements AccessDeniedHandler {
    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        BusinessException exception = new BusinessException(403, "权限不足");
        handlerExceptionResolver.resolveException(request, response, null, exception);

    }
}
