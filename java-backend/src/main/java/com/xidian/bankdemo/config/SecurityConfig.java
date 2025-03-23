package com.xidian.bankdemo.config;

import com.xidian.bankdemo.filter.JWTAuthenticationFilter;
import com.xidian.bankdemo.security.XCiphersm;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String[] AUTH_WHITELIST = {
            "/user/register",
            "/user/login",
            "/user/hello", // 测试签名验签
            "/user/helloAgain", // 测试时间戳
            "/vercode",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {// 注册为Bean
        return new PasswordEncoder() {
            @SneakyThrows
            @Override
            public String encode(CharSequence charSequence) {
                return XCiphersm.sm3_Hash((String) charSequence);
            }

            @SneakyThrows
            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return XCiphersm.sm3_Hash((String)charSequence).equals(s);
            }
        };
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/swagger*//**",
                "/v2/api-docs",
                "/webjars*//**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()//使用JWT，不需要csrf,基于token，需要session保存当前登录用户信息
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()//可以匿名访问的链接
                .anyRequest().authenticated()//其余所有请求都需要登录认证才能访问
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()));
//TODO: 配置角色权限
    }
}
