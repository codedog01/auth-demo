package com.lengao.auth.config;

import com.lengao.auth.security.filter.CustUsernamePasswordAuthenticationFilter;
import com.lengao.auth.security.filter.JwtVerifyFilter;
import com.lengao.auth.security.CustomAuthenticationEntryPoint;
import com.lengao.auth.security.UserAuthAccessDeniedHandler;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private String[] loadExcludePath() {
        return new String[]{
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/user/test",
                "/v2/api-docs",
                "/v3/api-docs",
                "/webjars/**",
                "/static/**",
                "/templates/**",
                "/img/**",
                "/js/**",
                "/css/**",
                "/lib/**"
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, UserDetailsService UserDetailsServiceImpl) throws Exception {

        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(UserDetailsServiceImpl)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   AuthenticationManager authenticationManager,
                                                   HandlerExceptionResolver handlerExceptionResolver,
                                                   UserAuthAccessDeniedHandler userAuthAccessDeniedHandler,
                                                   RedissonClient redissonClient,
                                                   CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {

        httpSecurity.formLogin().disable();
        httpSecurity.addFilterAt(new CustUsernamePasswordAuthenticationFilter(authenticationManager, handlerExceptionResolver), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.csrf().disable()
                .authorizeRequests()

                //放通所有静态资源
                .antMatchers(loadExcludePath()).permitAll()
                //放通注册
                .antMatchers("/auth/register", "/auth/login").permitAll()
                .antMatchers("/auth/test").hasAnyAuthority("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/logger/**").hasAnyRole("ADMIN", "LOGGER")
                //其余请求都需要认证后访问
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new JwtVerifyFilter(authenticationManager, handlerExceptionResolver,redissonClient))
                //已认证但是权限不够
                .exceptionHandling().accessDeniedHandler(userAuthAccessDeniedHandler)
                .and()
//                //未能通过认证，也就是未登录
//                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
//                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//禁用session
        return httpSecurity.build();
    }

}
