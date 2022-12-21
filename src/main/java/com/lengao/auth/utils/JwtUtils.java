package com.lengao.auth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author LengAo
 * @date 2021/11/3 11:00
 */
public class JwtUtils {
    public static void main(String[] args) {
    }

    /**
     * 秘钥
     */

    public static final String SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    /**
     * 生成token字符串的方法
     *
     * @return
     */
    @SneakyThrows
    public static String getJwtToken(Authentication authentication, int expire) {
        String name = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MILLISECOND, expire); //设置过期时间
        return JWT.create()
                .withIssuedAt(new Date())
                .withClaim("username", name)
                .withClaim("roles", roles)
                .withExpiresAt(instance.getTime()) //指定令牌的过期时间
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 判断token是否存在与有效
     *
     * @Param jwtToken
     */
    public static void checkToken(String jwtToken) throws JWTVerificationException {
        JWTVerifier build = JWT.require(Algorithm.HMAC256(SECRET))
                .build();
        build.verify(jwtToken);
    }

    /**
     * 判断token是否存在与有效
     *
     * @Param request
     */
    public static void checkToken(HttpServletRequest request)throws JWTVerificationException {
        String token = request.getHeader("token");
        checkToken(token);
    }

    /**
     * 根据token获取user
     *
     * @Param request
     */
    public static User getUserByJwtToken(HttpServletRequest request) throws JsonProcessingException {
        String token = request.getHeader("token");
        DecodedJWT decode = JWT.decode(token);
        String username = decode.getClaim("username").asString();
        Set<SimpleGrantedAuthority> roles = decode.getClaim("roles").asList(String.class).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return new User(username, "", roles);
    }
}
