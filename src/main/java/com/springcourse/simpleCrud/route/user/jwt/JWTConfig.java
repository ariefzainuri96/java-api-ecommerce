package com.springcourse.simpleCrud.route.user.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Bean
    public JWTAuthService jwtAuthService() {
        return new JWTAuthService(secret, issuer);
    }
}
