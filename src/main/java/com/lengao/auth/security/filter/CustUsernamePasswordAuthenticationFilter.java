package com.lengao.auth.security.filter;

import com.lengao.auth.config.BusinessException;
import com.lengao.auth.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
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
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * 不注入到IOC容器当中，否则的话会由Spring管理一份，springsecurity过滤器链中又有一份，导致过滤器走两边
 * 这个方法的报错可以单独用一个类来处理,并在安全配置中添加即可
 * @See CustomAuthenticationEntryPoint
 */
@Component
@Slf4j
public class CustUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter{
    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;
    @Autowired
    RedissonClient redissonClient;
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private boolean postOnly = true;


    public CustUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,HandlerExceptionResolver handlerExceptionResolver) {
        // 自定义登录接口地址
        super(new AntPathRequestMatcher("/auth/login", "POST"));
        this.setAuthenticationManager( authenticationManager);
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        Authentication authentication = null;
        try {
            String username = obtainUsername(request);
            String password = obtainPassword(request);

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);
            authentication = super.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e) {
            BusinessException exception = new BusinessException("账号或密码错误");
            handlerExceptionResolver.resolveException(request, response, null, exception);
            return null;
        }
//        String jwtToken = JwtUtils.getJwtToken(authentication, 60 * 6 * 1000);
//        response.addHeader("Authorization", jwtToken);
        return authentication;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        RMap<String, Authentication> token = redissonClient.getMap("token");

        String jwtToken = JwtUtils.getJwtToken(authResult.getName(), 60 * 6 * 1000);
        token.put(authResult.getName(), authResult);
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
