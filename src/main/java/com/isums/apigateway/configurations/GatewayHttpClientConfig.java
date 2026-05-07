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

/**
 * Apache HttpClient5 config for Spring Cloud Gateway (WebMVC).
 *
 * Why the tuning:
 * - Gateway MVC is blocking; every SSE stream pins one Apache pool slot for
 *   its entire lifetime. The default pool (25/route, 50 total) exhausts fast
 *   under StrictMode double-mounts, HMR, or a handful of tabs.
 * - PoolingHttpClientConnectionManager is the authoritative place to size
 *   and expire connections; the older `HttpClientBuilder.evictIdleConnections`
 *   path leaks config and can't raise the per-route cap.
 */
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
