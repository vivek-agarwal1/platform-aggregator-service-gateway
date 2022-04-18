package com.oyorooms.gateway.filter.customFilter;

import com.oyorooms.gateway.core.enums.PreFilterCustomEnum;
import com.oyorooms.gateway.exception.UnauthorizedException;
import com.oyorooms.gateway.external.response.IdentityResponse;
import com.oyorooms.gateway.external.service.ValidateTokenExecutor;
import com.oyorooms.gateway.helper.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilterAndApplySignature extends AbstractGatewayFilterFactory implements Ordered {

    @Autowired
    ValidateTokenExecutor validateTokenExecutor;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            Mono<IdentityResponse> identityResponseMono = null;
            try {
                identityResponseMono = validateTokenExecutor.execute(exchange,
                            IdentityResponse.class);
            } catch (UnauthorizedException e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return identityResponseMono.map(identityResponse -> {
                if(identityResponse.isUserValid()){
//                    exchange.getRequest().mutate().headers(httpHeaders -> {
//                        httpHeaders.add(Constants.GATEWAY_TOKEN, JWTSignatureUtil
//                                .getJwtSignature(Constants.VALIDATE_TOKEN_RESPONSE, identityResponse));
//                    }).build();
                    exchange.getAttributes().put(Constants.GATEWAY_CLIENT_IDENTIFIER, identityResponse.getAppClient().getApiClient());
                }
                else{
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                }
                return exchange;
            }).doOnError(ex -> {
                exchange.getResponse().setStatusCode(HttpStatus
                        .valueOf(Integer.valueOf(ex.getMessage())));
            }).onErrorReturn(exchange).flatMap(chain::filter);
        };
    }

    @Override
    public int getOrder() {
        return PreFilterCustomEnum.AuthenticationFilterAndApplySignature.ordinal();
    }
}
