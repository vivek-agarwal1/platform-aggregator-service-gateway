package com.oyorooms.gateway.route;

import com.oyorooms.gateway.configuration.entities.DownstreamUrlConfig;
import com.oyorooms.gateway.filter.CustomGatewayFilterFactory;
import com.oyorooms.gateway.route.factory.RouteReaderConfig;
import com.oyorooms.gateway.route.factory.RouteReaderConfigFactory;
import com.oyorooms.gateway.core.entity.RouteEntity;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class RouteLocatorImplementation {

    @Autowired
    RouteLocatorBuilder builder;

    @Autowired
    private CustomGatewayFilterFactory customGatewayFilterFactory;

    @Autowired
    private RouteReaderConfigFactory routeReaderConfigFactory;

    @Autowired
    DownstreamUrlConfig downstreamUrl;

    public RouteLocator appRoutes() throws IOException {
        RouteReaderConfig routeReaderConfig = routeReaderConfigFactory.getRouteClass("YAML");
        List<RouteEntity> routeEntityList = null;
        routeEntityList = routeReaderConfig.readData();
        Flux<RouteEntity> routeEntitiesAsFlux = Flux.fromIterable(routeEntityList);
        RouteLocatorBuilder.Builder routesBuilder = builder.routes();
        routeEntitiesAsFlux.toStream().forEach(routeEntity -> {
            routesBuilder.route(routeEntity.getName(),
                    predicateSpec -> setPredicateSpec(routeEntity, predicateSpec));
        });
        return routesBuilder.build();
    }

    private Buildable<Route> setPredicateSpec(RouteEntity routeEntity, PredicateSpec predicateSpec) {
        BooleanSpec booleanSpec = predicateSpec.path(routeEntity.getPath());
        booleanSpec.and().order(routeEntity.getOrder());
        booleanSpec.filters(gatewayFilterSpec -> setGatewayFilterSpec(routeEntity.getFilters(), gatewayFilterSpec));
        if (StringUtils.isNotBlank(routeEntity.getMethod())
                && Objects.nonNull(HttpMethod.resolve(routeEntity.getMethod()))) {
            booleanSpec.and().method(routeEntity.getMethod());
        }
        return booleanSpec.uri(getDownstreamUrl(routeEntity.getUri()));
    }

    private String getDownstreamUrl(String service) {
        switch (service) {
            case "OMS_READ":
                return downstreamUrl.getOmsReadUrl();
            case "OMS_WRITE":
                return downstreamUrl.getOmsWriteUrl();
            case "FULFILMENT_CREATE":
                return downstreamUrl.getFulfilmentCreateUrl();
            default:
                return downstreamUrl.getCrsAllUrl();
        }
    }

    // Sets Gateway Filters for all the Routes based on Config
    private UriSpec setGatewayFilterSpec(List<String> filtersAsList, GatewayFilterSpec gatewayFilterSpec) {
        filtersAsList.forEach(filter -> {
            gatewayFilterSpec.filter(customGatewayFilterFactory.getCustomGatewayFilter(filter).apply(new Object()));
        });
        return gatewayFilterSpec;
    }
}
