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
        config.addAllowedOriginPattern("https://*.ngrok-free.dev");
        config.addAllowedOriginPattern("https://outsystem.isums.pro");
        config.addAllowedOriginPattern("https://isums.pro");
        config.addAllowedOriginPattern("https://www.isums.pro");
        config.addAllowedOriginPattern("https://api-dev.isums.pro");

        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}