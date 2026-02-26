package com.isums.apigateway.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:5173",
                        "https://localhost:5173",
                        "http://localhost:5174",
                        "https://localhost:5174",
                        "https://*.ngrok-free.dev",
                        "https://outsystem.isums.pro",
                        "https://isums.pro",
                        "https://www.isums.pro"
                )
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

