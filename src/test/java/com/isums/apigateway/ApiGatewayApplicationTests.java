package com.isums.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.io.IOException;
import java.security.Security;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest(properties = {
		"spring.profiles.active=prod",
		"eureka.client.enabled=false"
})
class ApiGatewayApplicationTests {

	@Autowired
	private ClientHttpRequestFactory requestFactory;

	@Autowired
	private Environment environment;

	@Test
	void contextLoads() {
	}

	@Test
	void usesHttpComponentsForGatewayUpstreamCalls() {
		assertThat(requestFactory).isInstanceOf(HttpComponentsClientHttpRequestFactory.class);
		assertThat(environment.getProperty("spring.http.clients.imperative.factory"))
				.isEqualTo("http-components");
		assertThat(environment.getProperty("spring.http.clients.connect-timeout"))
				.isEqualTo("2s");
		assertThat(environment.getProperty("spring.http.clients.read-timeout"))
				.isEqualTo("10s");
		assertThat(Security.getProperty("networkaddress.cache.ttl")).isEqualTo("10");
		assertThat(Security.getProperty("networkaddress.cache.negative.ttl")).isEqualTo("0");
	}

	@Test
	void notificationRouteDoesNotUseRetryInDefaultConfig() throws IOException {
		ClassPathResource resource = new ClassPathResource("application.properties");
		assumeTrue(resource.exists(), "application.properties is ignored and may be absent in CI");
		Properties properties = PropertiesLoaderUtils.loadProperties(resource);

		assertThat(properties.getProperty("spring.cloud.gateway.server.webmvc.routes[18].id"))
				.isEqualTo("notification-service");
		assertThat(properties.getProperty("spring.cloud.gateway.server.webmvc.routes[18].filters[0]"))
				.isNull();
	}

	@Test
	void prodConfigContainsNotificationRoutes() throws IOException {
		Properties properties = PropertiesLoaderUtils.loadProperties(
				new ClassPathResource("application-prod.properties"));

		assertThat(properties.getProperty("spring.cloud.gateway.server.webmvc.routes[15].id"))
				.isEqualTo("notification-docs");
		assertThat(properties.getProperty("spring.cloud.gateway.server.webmvc.routes[15].uri"))
				.isEqualTo("http://notification-service.isums.local:8085");
		assertThat(properties.getProperty("spring.cloud.gateway.server.webmvc.routes[16].id"))
				.isEqualTo("notification-service");
		assertThat(properties.getProperty("spring.cloud.gateway.server.webmvc.routes[16].uri"))
				.isEqualTo("http://notification-service.isums.local:8085");
		assertThat(properties.getProperty("spring.cloud.gateway.server.webmvc.routes[16].filters[0]"))
				.isNull();
	}

}
