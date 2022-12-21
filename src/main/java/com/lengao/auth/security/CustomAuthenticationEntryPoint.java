package com.lengao.auth.security;

import com.lengao.auth.config.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 当用户没有有效凭证时，会用此类进行处理。
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-19
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException){
        BusinessException exception = new BusinessException(401, "未登录");
        handlerExceptionResolver.resolveException(request, response, null, exception);
    }

}
