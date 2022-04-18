package com.oyorooms.gateway.core.config;

import com.oyorooms.gateway.helper.Constants;
import io.micrometer.core.instrument.Tags;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.gateway.support.tagsprovider.GatewayRouteTagsProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;

@Component
public class CustomGatewayTagsProvider extends GatewayRouteTagsProvider {

    @Override
    public Tags apply(ServerWebExchange serverWebExchange) {
        Route route = (Route)serverWebExchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String client = Objects.isNull(serverWebExchange.getAttribute(Constants.GATEWAY_CLIENT_IDENTIFIER)) ? "No Client" :
                serverWebExchange.getAttribute(Constants.GATEWAY_CLIENT_IDENTIFIER);
        return route != null ? Tags.of(new String[]{"routeId", route.getId(), "routeUri", route
                .getUri().toString(), Constants.GATEWAY_CLIENT_IDENTIFIER, client}) : Tags.empty();
    }
}
