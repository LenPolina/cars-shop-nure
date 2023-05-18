package com.implemica.gateway.config;

import com.implemica.gateway.security.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RouteLocatorConfig {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes().build();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r.path("/api/**","/management/**")
                .filters(f -> f.filter(authenticationFilter()))
                .uri("http://localhost:8096"))
            .route(r -> r.path("/catalog/**","/car/**")
                .filters(f -> f.filter(authenticationFilter()))
                .uri("http://localhost:8092"))
            .route(r -> r.path("/cart/**")
                .filters(f -> f.filter(authenticationFilter()))
                .uri("http://localhost:8090"))
            .route(r -> r.path("/order/**")
                .filters(f -> f.filter(authenticationFilter()))
                .uri("http://localhost:8094"))
            .route(r -> r.path("/payment/**")
                .filters(f -> f.filter(authenticationFilter()))
                .uri("http://localhost:8095"))
            .route(r -> r.path("/storage/**")
                .filters(f -> f.filter(authenticationFilter()))
                .uri("http://localhost:8093"))
            .route(r -> r.path("/price/**")
                .filters(f -> f.filter(authenticationFilter()))
                .uri("http://localhost:8091"))
            .build();
    }
    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

}

