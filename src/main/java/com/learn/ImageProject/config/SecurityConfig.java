package com.learn.ImageProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()          // enable CORS (uses your CorsConfig)
            .csrf().disable()      // disable CSRF for APIs
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // allow all endpoints (for development)
            );

        return http.build();
    }
}
