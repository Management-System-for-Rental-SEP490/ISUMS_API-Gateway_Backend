package com.isums.apigateway.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("http://localhost:5173");
        config.addAllowedOriginPattern("https://localhost:5173");
        config.addAllowedOriginPattern("http://localhost:5174");
        config.addAllowedOriginPattern("https://localhost:5174");
        config.addAllowedOriginPattern("https://localhost:8082");
        config.addAllowedOriginPattern("http://localhost:8082");
        config.addAllowedOriginPattern("https://*.ngrok-free.dev");
        config.addAllowedOriginPattern("https://outsystem.isums.pro");
        config.addAllowedOriginPattern("https://isums.pro");
        config.addAllowedOriginPattern("https://www.isums.pro");
        config.addAllowedOriginPattern("https://api-dev.isums.pro");
        config.addAllowedOriginPattern("https://dev.isums.pro");
        config.addAllowedOriginPattern("https://dev-outsystem.isums.pro");

        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}