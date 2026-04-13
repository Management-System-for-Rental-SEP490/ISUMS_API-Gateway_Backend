package com.isums.apigateway.configurations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CorsConfig")
class CorsConfigTest {

    private UrlBasedCorsConfigurationSource source;

    @BeforeEach
    void setUp() {
        source = (UrlBasedCorsConfigurationSource)
                new CorsConfig().corsConfigurationSource();
    }

    private CorsConfiguration lookup(String path) {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI(path);
        return source.getCorsConfiguration(req);
    }

    @Test
    @DisplayName("registers configuration for /** (matches any path)")
    void matchesAllPaths() {
        assertThat(lookup("/api/users/me")).isNotNull();
        assertThat(lookup("/")).isNotNull();
        assertThat(lookup("/anything/deeply/nested")).isNotNull();
    }

    @Test
    @DisplayName("allows credentials and wildcard methods/headers")
    void allowsCredentialsAndWildcards() {
        CorsConfiguration cfg = lookup("/api");
        assertThat(cfg.getAllowCredentials()).isTrue();
        assertThat(cfg.getAllowedMethods()).containsExactly("*");
        assertThat(cfg.getAllowedHeaders()).containsExactly("*");
    }

    @Test
    @DisplayName("allows known production + dev origins")
    void allowsExpectedOrigins() {
        CorsConfiguration cfg = lookup("/api");
        assertThat(cfg.getAllowedOriginPatterns())
                .contains(
                        "https://isums.pro",
                        "https://www.isums.pro",
                        "https://outsystem.isums.pro",
                        "https://api-dev.isums.pro",
                        "http://localhost:5173",
                        "https://*.ngrok-free.dev"
                );
    }

    @Test
    @DisplayName("does not permit arbitrary origins (no plain *)")
    void rejectsWildcardAll() {
        CorsConfiguration cfg = lookup("/api");
        assertThat(cfg.getAllowedOriginPatterns()).doesNotContain("*");
    }
}
