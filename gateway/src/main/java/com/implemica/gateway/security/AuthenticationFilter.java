package com.implemica.gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AuthenticationFilter implements GatewayFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = getTokenFromHeader(headers);
        System.out.println(exchange.getRequest().getURI());
        if (token != null) {
            exchange.getRequest().mutate().header(AUTHORIZATION_HEADER, "Bearer " + token);
            System.out.println("Bearer "+ token);
        }
        System.out.println("filter end");
        return chain.filter(exchange);
    }

    private String getTokenFromHeader(HttpHeaders headers) {
        String authHeader = headers.getFirst(AUTHORIZATION_HEADER);
        System.out.println("Bearer "+ authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("Bearer "+ authHeader);
            return authHeader.substring(7);
        }

        return null;
    }

}
