package com.implemica.gateway.config;

//
//import com.implemica.gateway.security.AuthenticationFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.server.WebFilter;
//
//
//public class GatewaySecurityConfig {
//
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//            .authorizeExchange()
//            .pathMatchers("/**").permitAll()
//            .anyExchange().authenticated()
//            .and()
//            .httpBasic()
//            .and()
//            .addFilterAt((WebFilter) new AuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
//            .build();
//    }
//}
