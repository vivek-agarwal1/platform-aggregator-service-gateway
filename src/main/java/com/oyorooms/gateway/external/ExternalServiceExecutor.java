package com.oyorooms.gateway.external;

import com.oyorooms.gateway.exception.ExternalServiceException;
import com.oyorooms.gateway.exception.UnauthorizedException;
import com.oyorooms.gateway.external.response.BaseExternalResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface ExternalServiceExecutor {

    <T extends BaseExternalResponse> Mono<T> execute(ServerWebExchange exchange, Class<T> responseClass) throws ExternalServiceException, UnauthorizedException;

}
