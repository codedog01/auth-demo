package com.lengao.auth.config;

import com.lengao.auth.security.CustomAuthenticationEntryPoint;
import com.lengao.auth.security.UserAuthAccessDeniedHandler;
import com.lengao.auth.security.filter.CustUsernamePasswordAuthenticationFilter;
import com.lengao.auth.security.filter.JwtVerifyFilter;
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

    //安全认证管理
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, UserDetailsService UserDetailsServiceImpl) throws Exception {

        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(UserDetailsServiceImpl)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    //过滤器链配置
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   CustUsernamePasswordAuthenticationFilter custUsernamePasswordAuthenticationFilter,
                                                   UserAuthAccessDeniedHandler userAuthAccessDeniedHandler,
                                                   JwtVerifyFilter jwtVerifyFilter,
                                                   CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        httpSecurity.formLogin().disable();
        // 将我们自定义的UsernamePasswordAuthenticationFilter代替原来的
        httpSecurity.addFilterAt(custUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);//登录校验
        // 在认证前校验token信息，并将认证后的用户信息放入上下文中
        httpSecurity.addFilterBefore(jwtVerifyFilter, UsernamePasswordAuthenticationFilter.class);//接口 token校验

        httpSecurity.csrf().disable()
                .authorizeRequests()
                //放通所有静态资源
                .antMatchers(loadExcludePath()).permitAll()
                //放通注册
                .antMatchers("/auth/register", "/auth/login").permitAll()
                .antMatchers("/auth/test").hasRole("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/logger/**").hasAnyRole("ADMIN", "LOGGER")
                //其余请求都需要认证后访问
                .anyRequest()
                .authenticated()
                .and()
                //已认证但是权限不够
                .exceptionHandling().accessDeniedHandler(userAuthAccessDeniedHandler)
                .and()
                //未能通过认证，也就是未登录
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//禁用session
        return httpSecurity.build();
    }

}
