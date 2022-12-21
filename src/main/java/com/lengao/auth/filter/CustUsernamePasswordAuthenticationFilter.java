package com.lengao.auth.filter;

import com.lengao.auth.config.BusinessException;
import com.lengao.auth.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private boolean postOnly = true;

    public CustUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/auth/login", "POST"));
        this.setAuthenticationManager( authenticationManager);
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        Authentication authentication = null;
        try {
            authentication = super.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e) {
            BusinessException exception = new BusinessException("账号或密码错误");
            handlerExceptionResolver.resolveException(request, response, null, exception);
            return null;
        }
        String jwtToken = JwtUtils.getJwtToken(authentication, 60 * 6 * 1000);
        response.addHeader("Authorization", jwtToken);
        return authentication;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String jwtToken = JwtUtils.getJwtToken(authResult, 60 * 6 * 1000);
        response.addHeader("Authorization", jwtToken);
        chain.doFilter(request, response);
//        super.successfulAuthentication(request, response, chain, authResult);
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter).replaceAll(" ", "+");
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return usernameParameter;
    }

    public final String getPasswordParameter() {
        return passwordParameter;
    }




}
