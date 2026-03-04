package com.isums.apigateway.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain aiChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/ai/**")
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",

                                "/v3/api-docs",
                                "/v3/api-docs/**",

                                "/api/econtracts/v3/api-docs",
                                "/api/econtracts/v3/api-docs/**",

                                "/api/houses/v3/api-docs",
                                "/api/houses/v3/api-docs/**",

                                "/api/assets/v3/api-docs",
                                "/api/assets/v3/api-docs/**",

                                "/api/users/v3/api-docs",
                                "/api/users/v3/api-docs/**",

                                "/api/econtracts/processCode",
                                "/api/econtracts/ready",
                                "/api/econtracts/outsystem",
                                "/api/econtracts/sign",

                                "/error",
                                "/actuator/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}
