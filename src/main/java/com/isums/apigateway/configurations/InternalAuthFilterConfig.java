package com.isums.apigateway.configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalAuthFilterConfig extends OncePerRequestFilter {

    @Value("${ai.internal.token}")
    private String token;

    @Override
    protected void doFilterInternal(HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain) throws IOException, ServletException {

        if (req.getRequestURI().startsWith("/api/ai/scoring")) {

            String auth = req.getHeader("Authorization");

            if (auth == null || !auth.equals("Bearer " + token)) {
                res.setStatus(401);
                res.getWriter().write("unauthorized");
                return;
            }
        }

        chain.doFilter(req, res);
    }
}
