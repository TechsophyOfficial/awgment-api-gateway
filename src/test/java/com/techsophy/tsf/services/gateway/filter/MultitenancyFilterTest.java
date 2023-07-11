package com.techsophy.tsf.services.gateway.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import java.util.Map;
import static com.techsophy.tsf.services.gateway.constants.GatewayConstants.X_CORRELATIONID;
import static com.techsophy.tsf.services.gateway.constants.GatewayConstants.X_TENANT;
import static com.techsophy.tsf.services.gateway.constants.GatewayTestConstants.*;

@ExtendWith(MockitoExtension.class)
class MultitenancyFilterTest
{
    @Test
    void filterWithNullTenantTest()
    {
        MultiTenancyFilter multiTenancyFilter=new MultiTenancyFilter();
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        MockServerWebExchange webExchange = MockServerWebExchange
                .builder(MockServerHttpRequest
                        .get(TEST_URL)
                        .headers(headers)
                        .build())
                .build();
        GatewayFilterChain gatewayFilterChain = (exchange) -> {
            headers.set(X_TENANT, TECHSOPHY_PLATFORM);
            return ServerResponse.ok()
                    .headers(h -> h.addAll(exchange.getResponse().getHeaders()))
                    .bodyValue(RESPONSE_BODY)
                    .then();
        };
        Mono<Void> result=multiTenancyFilter.filter(webExchange, gatewayFilterChain);
        StepVerifier.create(result)
                .expectSubscription()
                .expectComplete()
                .verify();
        MockServerHttpResponse response=webExchange.getResponse();
        HttpHeaders httpHeadersResponse=response.getHeaders();
        Assertions.assertNull(httpHeadersResponse.get(X_TENANT));
        Assertions.assertNotNull(httpHeadersResponse.get(X_CORRELATIONID));
    }

    @Test
    void filterWithDefaultHeaderTest()
    {
        MultiTenancyFilter multiTenancyFilter=new MultiTenancyFilter();
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of(X_TENANT, List.of(TECHSOPHY_PLATFORM)));
        headers.put(AUTHORIZATION,List.of(BEARER_TOKEN));
        MockServerWebExchange webExchange = MockServerWebExchange
                .builder(MockServerHttpRequest
                .get(TEST_URL)
                        .headers(headers)
                        .build())
                .build();
        GatewayFilterChain gatewayFilterChain = (exchange) -> {
            headers.set(X_TENANT, TECHSOPHY_PLATFORM);
            return ServerResponse.ok()
                    .headers(h -> h.addAll(exchange.getResponse().getHeaders()))
                    .bodyValue(RESPONSE_BODY)
                    .then();
        };
        Mono<Void> result=multiTenancyFilter.filter(webExchange, gatewayFilterChain);
        StepVerifier.create(result)
                .expectSubscription()
                .expectComplete()
                .verify();
        MockServerHttpResponse response=webExchange.getResponse();
        HttpHeaders httpHeadersResponse=response.getHeaders();
        Assertions.assertEquals(TECHSOPHY_PLATFORM, httpHeadersResponse.getFirst(X_TENANT));
        Assertions.assertNotNull(httpHeadersResponse.get(X_CORRELATIONID));
    }

    @Test
    void filterWithNewHeaderTest()
    {
        MultiTenancyFilter multiTenancyFilter=new MultiTenancyFilter();
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of(X_TENANT, List.of(TESTING_TENANT)));
        MockServerWebExchange webExchange = MockServerWebExchange
                .builder(MockServerHttpRequest
                        .get(TEST_URL)
                        .headers(headers)
                        .build())
                .build();
        GatewayFilterChain gatewayFilterChain = (exchange) -> {
            headers.set(X_TENANT,TESTING_TENANT);
            return ServerResponse.ok()
                    .headers(h -> h.addAll(exchange.getResponse().getHeaders()))
                    .bodyValue(RESPONSE_BODY)
                    .then();
        };
        Mono<Void> result=multiTenancyFilter.filter(webExchange, gatewayFilterChain);
        StepVerifier.create(result)
                .expectSubscription()
                .expectComplete()
                .verify();
        MockServerHttpResponse response=webExchange.getResponse();
        HttpHeaders httpHeadersResponse=response.getHeaders();
        Assertions.assertEquals(TESTING_TENANT, httpHeadersResponse.getFirst(X_TENANT));
        Assertions.assertNotNull(httpHeadersResponse.get(X_CORRELATIONID));
    }
}
