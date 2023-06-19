package com.techsophy.tsf.services.gateway.filter;

import com.techsophy.idgenerator.IdGeneratorImpl;
import com.techsophy.tsf.services.gateway.config.TenantAuthenticationManagerResolver;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.techsophy.tsf.services.gateway.constants.GatewayConstants.X_CORRELATIONID;
import static com.techsophy.tsf.services.gateway.constants.GatewayConstants.X_TENANT;

@Component
public class MultiTenancyFilter implements GlobalFilter, OrderedWebFilter
{
    private final Logger logger = LoggerFactory.getLogger(MultiTenancyFilter.class);
    private final IdGeneratorImpl idGenerator=new IdGeneratorImpl();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        String jwtTenant= TenantAuthenticationManagerResolver.toTenant(exchange);
        String tenant = jwtTenant==null?exchange.getRequest().getHeaders().getFirst(X_TENANT):jwtTenant;
        String correlationId=exchange.getRequest().getHeaders().getFirst(X_CORRELATIONID);
        correlationId=correlationId==null?String.valueOf(idGenerator.nextId()):correlationId;
        ServerHttpRequest.Builder builder=exchange.getRequest()
                .mutate()
                .header(X_CORRELATIONID,correlationId);
        ServerHttpRequest request;
        if(tenant!=null)
        {
            builder=builder.header(X_TENANT,tenant);
        }
        request=builder.build();
        ServerWebExchange modifiedWebExchange=exchange.mutate().request(request).build();
        logger.info("Global Pre Filter executed");
        String finalCorrelationId = correlationId;
        return chain.filter(modifiedWebExchange).then(Mono.fromRunnable(()->{
            ServerHttpResponse serverHttpResponse=modifiedWebExchange.getResponse();
            HttpHeaders httpHeaders=serverHttpResponse.getHeaders();
            if(tenant!=null)
            {
                httpHeaders.set(X_TENANT,tenant);
            }
            httpHeaders.set(X_CORRELATIONID, finalCorrelationId);
        }));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String tenant="techsophy-platform";

        ServerWebExchange modifiedWebExchange= exchange;
        modifiedWebExchange.getResponse().beforeCommit(() -> Mono.fromRunnable(() -> {
            ReactiveSecurityContextHolder.getContext().map(securityContext -> {
                return securityContext.getAuthentication();
            }).map(authentication -> {
                return authentication.getClass().getName();
            }).map(o -> Mono.fromRunnable(()->{
                modifiedWebExchange.getResponse().getHeaders().set(X_TENANT,tenant);
                if(modifiedWebExchange.getResponse().getStatusCode().is3xxRedirection()){
                    String location = modifiedWebExchange.getResponse().getHeaders().getFirst("Location");
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(location);
                    builder.queryParam("_tenant",tenant);
                    location = builder.toUriString();
                    System.out.println(location);
                    modifiedWebExchange.getResponse().getHeaders().set("Location",location);
                }
            })).block();


        }));
        return chain.filter(modifiedWebExchange);

    }

    @Override
    public int getOrder() {
        return 101;
    }
}
