package com.techsophy.tsf.services.gateway.filter;

import com.techsophy.idgenerator.IdGeneratorImpl;
import com.techsophy.tsf.services.gateway.config.TenantAuthenticationManagerResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import static com.techsophy.tsf.services.gateway.constants.GatewayConstants.X_CORRELATIONID;
import static com.techsophy.tsf.services.gateway.constants.GatewayConstants.X_TENANT;

@Component
public class MultiTenancyFilter implements GlobalFilter
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
        ServerHttpRequest request;
        if(tenant==null)
        {
           request= exchange.getRequest()
                    .mutate()
                    .header(X_CORRELATIONID,correlationId)
                    .build();
        }
        else
        {
            request =exchange.getRequest()
                    .mutate()
                    .header(X_TENANT,tenant)
                    .header(X_CORRELATIONID,correlationId)
                    .build();
        }
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
}
