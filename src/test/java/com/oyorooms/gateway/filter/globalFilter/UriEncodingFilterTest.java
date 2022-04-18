package com.oyorooms.gateway.filter.globalFilter;

import com.oyorooms.gateway.core.enums.PreFilterGlobalEnum;
import com.oyorooms.gateway.helper.MockConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UriEncodingFilterTest {

    @InjectMocks
    UriEncodingFilter uriEncodingFilter;

    @Mock
    GatewayFilterChain filterChain;

    private ArgumentCaptor<? extends ServerWebExchange> captor;

    @BeforeAll
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    void setupForEachTest() {
        filterChain = Mockito.mock(GatewayFilterChain.class);
    }

    @Test
    void filterWithNoPath() {
        MockServerHttpRequest request = MockServerHttpRequest.get(MockConstants.MOCK_URL).build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        uriEncodingFilter.filter(exchange, filterChain);
        Mockito.verify(filterChain, Mockito.times(1)).filter(Mockito.anyObject());
    }

    @Test
    void filterWithNoEncodedPath() {
        MockServerHttpRequest request = MockServerHttpRequest.get(MockConstants.MOCK_URL
                + "/api/v2/bookings?fields=1").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        uriEncodingFilter.filter(exchange, filterChain);
        Mockito.verify(filterChain, Mockito.times(1)).filter(Mockito.anyObject());
    }

    @Test
    void filterWithEncodedPath() {
        MockServerHttpRequest request = MockServerHttpRequest.get(MockConstants.MOCK_URL
                + "/api/v2/bookings?fields=0%2C1%2C2%2C3").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        uriEncodingFilter.filter(exchange, filterChain);
        Mockito.verify(filterChain, Mockito.times(1)).filter(Mockito.anyObject());
    }

    @Test
    void filterWithEncodedPathWithGatewayRoute() {
        MockServerHttpRequest request = MockServerHttpRequest.get(MockConstants.MOCK_URL
                + "/api/v2/bookings?fields=0%2C1%2C2%2C3").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR,Route.async()
                .predicate(p -> p.getRequest().getMethod().equals(HttpMethod.GET))
                .uri(MockConstants.MOCK_DOWNSTREAM).id("mockId").build());
        uriEncodingFilter.filter(exchange, filterChain);
        URI expectedURI = URI.create(MockConstants.MOCK_DOWNSTREAM + ":443/api/v2/bookings?fields=0%252C1%252C2%252C3");
        Mockito.verify(filterChain, Mockito.times(1)).filter(Mockito.anyObject());
        assertEquals(expectedURI, exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR));
    }

    @Test
    void getOrderIsNotEqualToEnum() {
        assertFalse(Objects.equals(PreFilterGlobalEnum.UriPartialEncodingToFullEncodingFilter.ordinal(), uriEncodingFilter.getOrder()));
    }

    @Test
    void getOrderIsEqualToRouteToRequestFilterPlus1() {
        assertTrue(Objects.equals(RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER + 1, uriEncodingFilter.getOrder()));
    }
}