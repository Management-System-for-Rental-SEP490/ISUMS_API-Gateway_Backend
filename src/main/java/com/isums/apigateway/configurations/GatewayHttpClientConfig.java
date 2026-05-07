package com.isums.apigateway.configurations;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.pool.PoolReusePolicy;
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
                ConnectionConfig connectionConfig = ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.ofSeconds(2))
                        .setValidateAfterInactivity(TimeValue.ofSeconds(1))
                        .setTimeToLive(TimeValue.ofMinutes(10))
                        .build();

                PoolingHttpClientConnectionManager connectionManager =
                        PoolingHttpClientConnectionManagerBuilder.create()
                                .setMaxConnPerRoute(200)
                                .setMaxConnTotal(500)
                                .setConnPoolPolicy(PoolReusePolicy.LIFO)
                                .setDefaultConnectionConfig(connectionConfig)
                                .build();

                return httpComponentsBuilder
                        .withHttpClientCustomizer(client -> client
                                .setConnectionManager(connectionManager)
                                .evictExpiredConnections()
                                .evictIdleConnections(TimeValue.ofMinutes(1)))
                        .withDefaultRequestConfigCustomizer(config -> config
                                .setConnectionRequestTimeout(Timeout.ofSeconds(5))
                                .setDefaultKeepAlive(60, TimeUnit.SECONDS));
            }
            return (ClientHttpRequestFactoryBuilder) builder;
        };
    }
}
