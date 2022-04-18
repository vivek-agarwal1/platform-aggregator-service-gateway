package com.oyorooms.gateway.filter.customFilter;

import com.oyorooms.gateway.core.enums.PreFilterCustomEnum;
import com.oyorooms.gateway.helper.Constants;
import com.oyorooms.gateway.helper.MockConstants;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RemoveAccessTokenFilterTest {

    @InjectMocks
    RemoveAccessTokenFilter removeAccessTokenFilter;

    @Mock
    GatewayFilterChain filterChain;

    private String accessTokenValue;
    private ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);

    @BeforeAll
    void setup() {
        int length = 100;
        boolean useLetters = true;
        boolean useNumbers = false;
        accessTokenValue = RandomStringUtils.random(length, useLetters, useNumbers);
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    void setupForEachTest() {
        when(filterChain.filter(captor.capture())).thenReturn(Mono.empty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"access-token", "access_token"})
    void testApplyWithOnlySingleAccessTokenHeader(String accessTokenKey) {
        MockServerHttpRequest request = MockServerHttpRequest.get(MockConstants.MOCK_URL)
                .header(accessTokenKey, accessTokenValue).build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter filter = removeAccessTokenFilter.apply(new Object());

        Mockito.doReturn(Mono.empty()).when(filterChain).filter(Mockito.anyObject());
        filter.filter(exchange, filterChain).block();

        assertTrue(Objects.nonNull(exchange.getRequest().getHeaders())
                && Objects.isNull(exchange.getRequest().getHeaders().get(accessTokenKey)));
    }

    @Test
    void testApplyWhenBothTypeOfAccessKeyIsPassed() {
        MockServerHttpRequest request = MockServerHttpRequest.get(MockConstants.MOCK_URL)
                .header(Constants.ACCESS_TOKEN, accessTokenValue)
                .header(Constants.AUTH_TOKEN_REQUEST_ATTRIBUTE_NAME_FALLBACK, accessTokenValue).build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter filter = removeAccessTokenFilter.apply(new Object());

        Mockito.doReturn(Mono.empty()).when(filterChain).filter(Mockito.anyObject());
        filter.filter(exchange, filterChain).block();

        assertTrue(Objects.nonNull(exchange.getRequest().getHeaders())
                && Objects.isNull(exchange.getRequest().getHeaders().get(Constants.ACCESS_TOKEN))
                && Objects.isNull(exchange.getRequest().getHeaders().get(Constants.AUTH_TOKEN_REQUEST_ATTRIBUTE_NAME_FALLBACK)));
    }

    @Test
    void testGetOrder() {
        assertTrue(Objects.equals(PreFilterCustomEnum.RemoveAccessTokenFilter.ordinal(), removeAccessTokenFilter.getOrder()));
    }
}