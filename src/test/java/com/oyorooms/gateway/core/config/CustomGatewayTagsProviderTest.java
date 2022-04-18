package com.oyorooms.gateway.core.config;

import com.oyorooms.gateway.helper.Constants;
import com.oyorooms.gateway.helper.MockConstants;
import io.micrometer.core.instrument.Tags;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomGatewayTagsProviderTest {

    CustomGatewayTagsProvider customGatewayTagsProvider;

    MockServerWebExchange mockServerWebExchange;

    @BeforeAll
    void setup() {
        customGatewayTagsProvider = new CustomGatewayTagsProvider();
    }

    @BeforeEach
    void beforeEachTest() {
        MockServerHttpRequest request = MockServerHttpRequest.get(MockConstants.MOCK_URL).build();
        mockServerWebExchange = MockServerWebExchange.from(request);
    }

    @Test
    void applyTagsTestWhenServerWebExchangeReturnNullRoute() {
        assertEquals(Tags.empty(), customGatewayTagsProvider.apply(mockServerWebExchange));
    }

    @Test
    void applyTagsTestWhenServerWebExchangeReturnRouteWithoutClient() {
        Route expectedRoute = Route.async().predicate(p -> p.getRequest().getMethod().equals(HttpMethod.GET))
                .uri(MockConstants.MOCK_DOWNSTREAM).id("mockId").build();
        Tags expectedTags = Tags.of(new String[]{"routeId", expectedRoute.getId(), "routeUri", expectedRoute.getUri().toString(), Constants.GATEWAY_CLIENT_IDENTIFIER, "No Client"});
        mockServerWebExchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, expectedRoute);
        Tags actualTags = customGatewayTagsProvider.apply(mockServerWebExchange);
        assertEquals(expectedTags, actualTags);
    }

    @Test
    void applyTagsTestWhenServerWebExchangeReturnRouteWithClient() {
        Route expectedRoute = Route.async().predicate(p -> p.getRequest().getMethod().equals(HttpMethod.GET))
                .uri(MockConstants.MOCK_DOWNSTREAM).id("mockId").build();
        String expectedClient = "Android App";
        Tags expectedTags = Tags.of(new String[]{"routeId", expectedRoute.getId(), "routeUri", expectedRoute.getUri().toString(), Constants.GATEWAY_CLIENT_IDENTIFIER, expectedClient});
        mockServerWebExchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, expectedRoute);
        mockServerWebExchange.getAttributes().put(Constants.GATEWAY_CLIENT_IDENTIFIER, expectedClient);
        Tags actualTags = customGatewayTagsProvider.apply(mockServerWebExchange);
        assertEquals(expectedTags, actualTags);
    }
}