package com.oyorooms.gateway.filter.customFilter;

import com.oyorooms.gateway.core.enums.PreFilterCustomEnum;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UnauthorizedRequestFilter extends AbstractGatewayFilterFactory implements Ordered {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            if(Objects.nonNull(exchange.getResponse().getStatusCode())
                    && exchange.getResponse().getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
                return exchange.getResponse().setComplete();
            } else if(Objects.nonNull(exchange.getResponse().getStatusCode())
                    && exchange.getResponse().getStatusCode().isError()){
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }

    @Override
    public int getOrder() {
        return PreFilterCustomEnum.UnauthorizedRequestFilter.ordinal();
    }
}
