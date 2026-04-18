package com.isums.apigateway.configurations;

import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpComponentsClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.autoconfigure.ClientHttpRequestFactoryBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Security;
import java.util.concurrent.TimeUnit;

@Configuration
public class GatewayHttpClientConfig {

    static {
        Security.setProperty("networkaddress.cache.ttl", "10");
        Security.setProperty("networkaddress.cache.negative.ttl", "0");
    }

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    ClientHttpRequestFactoryBuilderCustomizer gatewayHttpClientCustomizer() {
        return builder -> {
            if (builder instanceof HttpComponentsClientHttpRequestFactoryBuilder httpComponentsBuilder) {
                return httpComponentsBuilder
                        .withConnectionConfigCustomizer(config -> config
                                .setValidateAfterInactivity(TimeValue.ofSeconds(1))
                                .setTimeToLive(TimeValue.ofSeconds(20)))
                        .withDefaultRequestConfigCustomizer(config -> config
                                .setConnectionRequestTimeout(Timeout.ofSeconds(2))
                                .setDefaultKeepAlive(15, TimeUnit.SECONDS));
            }
            return (ClientHttpRequestFactoryBuilder) builder;
        };
    }
}
