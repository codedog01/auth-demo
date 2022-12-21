package com.lengao.auth.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lengao.auth.config.BusinessException;
import com.lengao.auth.utils.JwtUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class JwtVerifyFilter extends BasicAuthenticationFilter {

   @Autowired
   HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    RedissonClient redissonClient;

    public JwtVerifyFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public JwtVerifyFilter(AuthenticationManager authenticationManager, HandlerExceptionResolver handlerExceptionResolver, RedissonClient redissonClient) {
        super(authenticationManager);
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.redissonClient = redissonClient;
    }



    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        if (requestURI.equals("/auth/register")||requestURI.equals("/auth/login")) {
            chain.doFilter(request, response);
        } else {
            User user = null;
            try {
                JwtUtils.checkToken(request);
                user = JwtUtils.getUserByJwtToken(request);
            } catch (JWTVerificationException | JsonProcessingException e) {
                BusinessException businessException = new BusinessException(401,"无效token");
                handlerExceptionResolver.resolveException(request, response, null, businessException);
                return;
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }

    }
}

