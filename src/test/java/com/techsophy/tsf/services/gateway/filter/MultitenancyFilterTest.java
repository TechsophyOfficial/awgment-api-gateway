package com.techsophy.tsf.services.gateway.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MultitenancyFilterTest
{
    @Mock
    ServerWebExchange serverWebExchange;
    @Mock
    GatewayFilterChain gatewayFilterChain;
    @Mock
    ServerHttpRequest.Builder builder;
    @Mock
    ServerWebExchange.Builder serverWebExchangeBuilder;
    @Mock
    ServerHttpRequest serverHttpRequest;
    @Mock
    ServerHttpResponse serverHttpResponse;

    @Test
    void filterWithHeaderTest()
    {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("X-Tenant","test-tenant");
        httpHeaders.add("X-CorrelationId","testId");
        httpHeaders.add("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2X2NzTUtiOVFsUVpETUg2TXBUdXV1YURtUWstVTQ3bmZjWVZGbFlpLTMwIn0.eyJleHAiOjE2ODM3MjE4MDIsImlhdCI6MTY4MzcyMDAwMiwianRpIjoiMWQzNTBhMDQtMDA5YS00MjJkLTk0NWItZjM4YTk4MmE4YzkxIiwiaXNzIjoiaHR0cHM6Ly9rZXljbG9hay10c3BsYXRmb3JtLnRlY2hzb3BoeS5jb20vYXV0aC9yZWFsbXMvdGVjaHNvcGh5LXBsYXRmb3JtIiwiYXVkIjpbImNhbXVuZGEtcmVzdC1hcGkiLCJyZWFsbS1tYW5hZ2VtZW50IiwidGlja2V0aW5nLXN5c3RlbSIsImFjY291bnQiXSwic3ViIjoiY2EwYjAzYjEtMjE3NS00YjI1LWI4NDYtNWYwYzlkNGQ4MWNiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY2FtdW5kYS1pZGVudGl0eS1zZXJ2aWNlIiwic2Vzc2lvbl9zdGF0ZSI6ImUxYzVkMmNmLTEyYjEtNDhjNi1iMWU0LWFkNTM4YzYyMDNhNSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiIsImh0dHA6Ly9sb2NhbGhvc3Q6MzAwMSJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJlYWxtLW1hbmFnZW1lbnQiOnsicm9sZXMiOlsidmlldy1yZWFsbSIsInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwibWFuYWdlLWlkZW50aXR5LXByb3ZpZGVycyIsImltcGVyc29uYXRpb24iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX19LCJzY29wZSI6ImNhbXVuZGEtcmVzdC1hcGkgcHJvZmlsZSBlbWFpbCBhd2dtZW50IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoidmFpYmhhdiBqYWlzd2FsIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidmFpYmhhdiIsImdpdmVuX25hbWUiOiJ2YWliaGF2IiwiZmFtaWx5X25hbWUiOiJqYWlzd2FsIiwidXNlcklkIjoiMTA5NzQ5MzkwMzU0OTc0MzEwNCIsImVtYWlsIjoidmFpYmhhdi5rQHRlY2hzb3BoeS5jb20ifQ.aUeeEuKyxZ9ofyzzoDOSkd2N-lMMvpSpI5BmWfLWPhJXSJFP8ev5OmEnQukpeV7dxCDrmElNG8WQ4x4z7SIVDSg8G6uUOxUrIWb1gqeZxOfEMYP9cPsb3LrDZ5HopZVAxZ6xtnprJ80P2XmfiWnR0O1MnYP2pRBQwZxIOyNGpdTZWNqtL0-_Efl2OEhcVIjv8UDMsB7CGd3LA2bSH8M9b6Z908HYLYbU2PGjTM6zUSy5M9D0X1bAW1UrMiNxVczQ33HSvdkrGtHDBntOzlWCDR3OWRTU_uhix3I-qxvTfnoLS6nvUs8zDYOmgx5eSbth0m5zX3jFw94TVbKS1luEiA");
        Mockito.when(serverHttpRequest.mutate()).thenReturn(builder);
        Mockito.when(builder.header(anyString(),anyString())).thenReturn(builder);
        Mockito.when(builder.build()).thenReturn(serverHttpRequest);
        Mockito.when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        Mockito.when(serverHttpRequest.getHeaders()).thenReturn(httpHeaders);
        Mockito.when(serverWebExchange.mutate()).thenReturn(serverWebExchangeBuilder);
        Mockito.when(serverWebExchangeBuilder.request((ServerHttpRequest) any())).thenReturn(serverWebExchangeBuilder);
        Mockito.when(serverWebExchangeBuilder.build()).thenReturn(serverWebExchange);
        Mockito.when(gatewayFilterChain.filter(any())).thenReturn(Mono.empty());
        Mockito.when(serverWebExchange.getResponse()).thenReturn(serverHttpResponse);
        Mockito.when(serverHttpResponse.getHeaders()).thenReturn(httpHeaders);
        MultiTenancyFilter multiTenancyFilter=new MultiTenancyFilter();
        Mono<Void> result=multiTenancyFilter.filter(serverWebExchange,gatewayFilterChain);
        StepVerifier.create(result)
                .expectSubscription()
                .expectComplete()
                .verify();
        verify(gatewayFilterChain).filter(any(ServerWebExchange.class));
    }

    @Test
    void filterWithoutHeaderTest()
    {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization",null);
        Mockito.when(serverHttpRequest.mutate()).thenReturn(builder);
        Mockito.when(builder.header(anyString(),anyString())).thenReturn(builder);
        Mockito.when(builder.build()).thenReturn(serverHttpRequest);
        Mockito.when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        Mockito.when(serverHttpRequest.getHeaders()).thenReturn(httpHeaders);
        Mockito.when(serverWebExchange.mutate()).thenReturn(serverWebExchangeBuilder);
        Mockito.when(serverWebExchangeBuilder.request((ServerHttpRequest) any())).thenReturn(serverWebExchangeBuilder);
        Mockito.when(serverWebExchangeBuilder.build()).thenReturn(serverWebExchange);
        Mockito.when(gatewayFilterChain.filter(any())).thenReturn(Mono.empty());
        Mockito.when(serverWebExchange.getResponse()).thenReturn(serverHttpResponse);
        Mockito.when(serverHttpResponse.getHeaders()).thenReturn(httpHeaders);
        MultiTenancyFilter multiTenancyFilter=new MultiTenancyFilter();
        Assertions.assertThrows(Exception.class,()->multiTenancyFilter.filter(serverWebExchange,gatewayFilterChain));
    }
}
