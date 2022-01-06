package com.microservices.apigateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        //redirect when get path "localhost/get" to hardcoded url
        //it is possible to add custom variable e.g. header
        return builder.routes()
                .route(p -> p // matching base of e.g. path, filters, headers, weight
                        .path("/get")
                        .filters(f -> f
                                .addRequestHeader("MyHeader","MyURI")
                                .addRequestParameter("Param","ParamValue")
                        )
                        .uri("http://httpbin.org:80"))
                .route(p -> p
                        .path("/currency-exchange/**")
                        .uri("lb://currency-exchange-service")
                ) // lb - load balance
                .route(p -> p.path("/currency-conversion/**")
                        .uri("lb://currency-conversion-service")
                )
                .route(p -> p.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion-service")
                )
                .route(p -> p.path("/currency-conversion-new/**")
                        .filters(f -> f.rewritePath(
                                "/currency-conversion-new/(?<segment>.*)",
                                "/currency-conversion-feign/${segment}")
                        ) // redirect to different path and replace path by regex
                        .uri("lb://currency-conversion-service")
                )
                .build();
        // without customization
        // return builder.routes().build();
    }
}
