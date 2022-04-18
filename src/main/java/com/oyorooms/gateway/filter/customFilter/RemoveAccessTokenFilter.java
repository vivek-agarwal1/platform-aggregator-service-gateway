package com.oyorooms.gateway.filter.customFilter;

import com.oyorooms.gateway.core.enums.PreFilterCustomEnum;
import com.oyorooms.gateway.helper.Constants;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class RemoveAccessTokenFilter  extends AbstractGatewayFilterFactory implements Ordered {
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            exchange.getRequest().mutate().headers(httpHeaders -> {
                httpHeaders.remove(Constants.AUTH_TOKEN_REQUEST_ATTRIBUTE_NAME_FALLBACK);
                httpHeaders.remove(Constants.AUTH_TOKEN_REQUEST_ATTRIBUTE_NAME);
            }).build();
            return chain.filter(exchange);
        };
    }

    @Override
    public int getOrder() {
        return PreFilterCustomEnum.RemoveAccessTokenFilter.ordinal();
    }
}
