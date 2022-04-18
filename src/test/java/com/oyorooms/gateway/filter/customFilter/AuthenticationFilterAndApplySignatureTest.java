package com.oyorooms.gateway.filter.customFilter;

import com.oyorooms.gateway.core.enums.PreFilterCustomEnum;
import com.oyorooms.gateway.exception.ExternalServiceException;
import com.oyorooms.gateway.exception.UnauthorizedException;
import com.oyorooms.gateway.external.response.AppClientDetails;
import com.oyorooms.gateway.external.response.IdentityResponse;
import com.oyorooms.gateway.external.service.ValidateTokenExecutor;
import com.oyorooms.gateway.helper.MockConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationFilterAndApplySignatureTest {

    @InjectMocks
    AuthenticationFilterAndApplySignature authenticationFilter;

    @Mock
    GatewayFilterChain filterChain;

    @Mock
    ValidateTokenExecutor validateTokenExecutor;

    private ArgumentCaptor<? extends ServerWebExchange> captor;
    private IdentityResponse identityResponse;
    private ServerWebExchange exchange;


    @BeforeAll
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    void setupForEachTest() {
        identityResponse = new IdentityResponse();
        identityResponse.setAuthValid(true);
        identityResponse.setAppClient(new AppClientDetails());
        MockServerHttpRequest request = MockServerHttpRequest.get(MockConstants.MOCK_URL).build();
        exchange = MockServerWebExchange.from(request);
        captor = ArgumentCaptor.forClass(exchange.getClass());
        Mockito.when(filterChain.filter(captor.capture())).thenReturn(Mono.empty());
    }

    @Test
    void customTestWhenValidationTokenExecutorThrowsUnauthorized() throws UnauthorizedException {
        GatewayFilter filter = authenticationFilter.apply(new Object());

        Mockito.doThrow(new UnauthorizedException()).when(validateTokenExecutor)
                .execute(Mockito.any(ServerWebExchange.class),
                Mockito.eq(IdentityResponse.class));
        filter.filter(exchange, filterChain).block();
        assertTrue(HttpStatus.UNAUTHORIZED.equals(exchange.getResponse().getStatusCode()));
    }

    @Test
    void customTestWhenValidationTokenExecutorReceivesServiceUnavailable() throws UnauthorizedException {
        GatewayFilter filter = authenticationFilter.apply(new Object());
        HttpStatus expectedHttpStatus = HttpStatus.SERVICE_UNAVAILABLE;

        Mockito.doReturn(Mono.error(new ExternalServiceException(
                String.valueOf(expectedHttpStatus.value())))).when(validateTokenExecutor)
                .execute(Mockito.any(ServerWebExchange.class),
                        Mockito.eq(IdentityResponse.class));
        filter.filter(exchange, filterChain).block();
        assertTrue(expectedHttpStatus.equals(captor.getValue().getResponse().getStatusCode()));
    }

//    @Test
//    void customTestWhenValidationTokenExecutorWhenUserIsValid() throws UnauthorizedException {
//        GatewayFilter filter = authenticationFilter.apply(new Object());
//        JWTAuthenticationService jwtAuthenticationService = new JWTAuthenticationService(JWTStrategyType.SIGNATURE);
//        jwtAuthenticationService = Mockito.mock(jwtAuthenticationService.getClass());
//
//        Mockito.doReturn(Mono.just(identityResponse)).when(validateTokenExecutor)
//                .execute(Mockito.any(ServerWebExchange.class),
//                        Mockito.eq(IdentityResponse.class));
//        Mockito.when(jwtAuthenticationService.generateToken(Mockito.any(JWTRequest.class)))
//                .thenReturn(MockConstants.MOCK_JWT_SIGNATURE);
//        filter.filter(exchange, filterChain).block();
//        assertTrue(Objects.isNull(exchange.getResponse().getStatusCode()));
//    }

    @Test
    void customTestWhenValidationTokenExecutorWhenUserIsNotValid() throws UnauthorizedException {
        GatewayFilter filter = authenticationFilter.apply(new Object());
        identityResponse.setAuthValid(false);

        Mockito.doReturn(Mono.just(identityResponse)).when(validateTokenExecutor)
                .execute(Mockito.any(ServerWebExchange.class),
                        Mockito.eq(IdentityResponse.class));
        filter.filter(exchange, filterChain).block();
        assertTrue(HttpStatus.UNAUTHORIZED.equals(captor.getValue().getResponse().getStatusCode()));
    }

    @Test
    void testGetOrder() {
        assertTrue(Objects.equals(PreFilterCustomEnum.AuthenticationFilterAndApplySignature.ordinal(), authenticationFilter.getOrder()));
    }
}