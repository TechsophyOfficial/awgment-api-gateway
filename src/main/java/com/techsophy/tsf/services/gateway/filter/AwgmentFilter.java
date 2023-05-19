package com.techsophy.tsf.services.gateway.filter;

import com.techsophy.tsf.services.gateway.config.TenantAuthenticationManagerResolver;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AwgmentFilter implements GlobalFilter {



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String jwtTenant = TenantAuthenticationManagerResolver.toTenant(exchange);
        String tenant = jwtTenant==null?exchange.getRequest().getHeaders().getFirst("X-Tenant"):jwtTenant;
        String correlationId = exchange.getRequest().getHeaders().getFirst("X-CorrelationId");
        tenant = tenant==null?"techsophy-platform":tenant;
        exchange.getRequest().getHeaders().set("X-Tenant",tenant);
//        exchange.getRequest().getHeaders().set("X-CorrelationId",correlationId==null?idGenerator.next:correlationId);
        //.getRequest().getHeaders().getFirst("Authorization");

        return null;
    }
}
