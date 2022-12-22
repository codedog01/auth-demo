package com.lengao.auth.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lengao.auth.config.BusinessException;
import com.lengao.auth.utils.JwtUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtVerifyFilter extends OncePerRequestFilter {

   @Autowired
   HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    RedissonClient redissonClient;

    public JwtVerifyFilter( HandlerExceptionResolver handlerExceptionResolver, RedissonClient redissonClient) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.redissonClient = redissonClient;
    }



    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        if (requestURI.equals("/auth/register")||requestURI.equals("/auth/login")) {
            chain.doFilter(request, response);
        } else {
            Authentication authentication;
            try {
                JwtUtils.checkToken(request);
                String username = JwtUtils.getUseNameByJwtToken(request);
                RMap<String, Authentication> token = redissonClient.getMap("token");
                authentication = token.get(username);

            } catch (JWTVerificationException | JsonProcessingException e) {
                BusinessException businessException = new BusinessException(401, "无效token");
                handlerExceptionResolver.resolveException(request, response, null, businessException);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }

    }
}

