package com.oyorooms.gateway.core.config;

import com.oyorooms.gateway.helper.Constants;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTags;
import org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTagsProvider;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;
import java.util.Optional;

import java.util.regex.Pattern;

@Component
public class CustomWebFluxTagsProvider implements WebFluxTagsProvider {

    private static final Tag URI_ROOT = Tag.of("uri", "root");
    private static final Pattern FORWARD_SLASHES_PATTERN = Pattern.compile("//+");

    @Override
    public Iterable<Tag> httpRequestTags(ServerWebExchange exchange, Throwable exception) {
        Tags tags = Tags.empty();
        tags = tags.and(new Tag[]{WebFluxTags.method(exchange)});
        tags = tags.and(new Tag[]{uri(exchange, true)});
        tags = tags.and(new Tag[]{WebFluxTags.exception(exception)});
        tags = tags.and(new Tag[]{WebFluxTags.status(exchange)});
        tags = tags.and(new Tag[]{WebFluxTags.outcome(exchange, exception)});
        String client = Objects.isNull(exchange.getAttribute(Constants.GATEWAY_CLIENT_IDENTIFIER)) ? "No Client" :
                exchange.getAttribute(Constants.GATEWAY_CLIENT_IDENTIFIER);
        tags = tags.and(new Tag[]{Tag.of(Constants.GATEWAY_CLIENT_IDENTIFIER, client)});
        return tags;
    }

    private static Tag uri(ServerWebExchange exchange, boolean ignoreTrailingSlash) {
        String path = getPathInfo(exchange);
        return path.isEmpty() ? URI_ROOT : Tag.of("uri", path);
    }

    private static String removeTrailingSlash(String text) {
        return text.endsWith("/") ? text.substring(0, text.length() - 1) : text;
    }

    private static String getPathInfo(ServerWebExchange exchange) {
        String path = Optional.ofNullable(getRouteInfoId(exchange)).orElse(exchange.getRequest().getPath().value());
        String uri = StringUtils.hasText(path) ? path : "/";
        String singleSlashes = FORWARD_SLASHES_PATTERN.matcher(uri).replaceAll("/");
        return removeTrailingSlash(singleSlashes);
    }

    private static String getRouteInfoId(ServerWebExchange exchange) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if(Objects.nonNull(route)) {
            return route.getId();
        } else {
            return null;
        }
    }
}
