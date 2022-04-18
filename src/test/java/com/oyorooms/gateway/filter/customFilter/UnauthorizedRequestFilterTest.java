package com.oyorooms.gateway.filter.customFilter;

import com.oyorooms.gateway.core.enums.PreFilterCustomEnum;
import com.oyorooms.gateway.helper.MockConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UnauthorizedRequestFilterTest {

    @InjectMocks
    UnauthorizedRequestFilter unauthorizedRequestFilter;

    @Mock
    GatewayFilterChain filterChain;

    private ArgumentCaptor<? extends ServerWebExchange> captor;
    private ServerWebExchange exchange;

    @BeforeAll
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @ValueSource(ints = {401, 502, 503})
    void customTestWhenExchangeResponseStatusCodeIsSet(Integer httpStatus){
        HttpStatus expectedHttpStatus = HttpStatus.valueOf(httpStatus);
        MockServerHttpRequest request = MockServerHttpRequest.get(MockConstants.MOCK_URL).build();
        exchange = MockServerWebExchange.from(request);
        exchange.getResponse().setStatusCode(expectedHttpStatus);
        captor = ArgumentCaptor.forClass(exchange.getClass());
        GatewayFilter filter = unauthorizedRequestFilter.apply(new Object());
        Mockito.when(filterChain.filter(captor.capture())).thenReturn(Mono.empty());
        filter.filter(exchange, filterChain).block();
        assertThrows(MockitoException.class,
                () -> captor.getValue());
    }

    @Test
    void customTestWhenExchangeResponseWhenResponseCodeIsNotSet(){
        MockServerHttpRequest request = MockServerHttpRequest.get(MockConstants.MOCK_URL).build();
        exchange = MockServerWebExchange.from(request);
        captor = ArgumentCaptor.forClass(exchange.getClass());
        Mockito.when(filterChain.filter(captor.capture())).thenReturn(Mono.empty());
        GatewayFilter filter = unauthorizedRequestFilter.apply(new Object());
        filter.filter(exchange, filterChain).block();
        assertNull(captor.getValue().getResponse().getStatusCode());
    }

    @Test
    void testGetOrder() {
        assertTrue(Objects.equals(PreFilterCustomEnum.UnauthorizedRequestFilter.ordinal(), unauthorizedRequestFilter.getOrder()));
    }
}