package com.techsophy.tsf.services.gateway.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.LinkedMultiValueMap;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import java.util.Map;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class AwgmentOAuthRequestResolverTest
{
    @Mock
    ReactiveClientRegistrationRepository repo;
    @InjectMocks
    AwgmentOAuthRequestResolver awgmentOAuthRequestResolver;

    @Test
    void resolveWebExchangeWithHeaderTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of("X-Tenant", List.of("techsophy-platform")));
        MockServerWebExchange webExchange = MockServerWebExchange.builder(MockServerHttpRequest.get("http://localhost:8080").headers(headers).build()).build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange);
        StepVerifier.create(resolve).expectComplete();
    }

    @Test
    void resolveWebExchangeWithoutHeaderTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of());
        MockServerWebExchange webExchange = MockServerWebExchange.builder(MockServerHttpRequest.get("http://localhost:8080").headers(headers).build()).build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange);
        StepVerifier.create(resolve)
                .expectNext(OAuth2AuthorizationRequest
                .authorizationCode().authorizationRequestUri("http://keycloak")
                        .authorizationUri("http://localhost:8080").clientId("camunda-identity-service")
                        .redirectUri("http://keycloak")
                .build()).expectComplete();
    }

    @Test
    void resolveWebExchangeAndClientRegistrationWithHeaderTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of("X-Tenant", List.of("techsophy-platform")));
        MockServerWebExchange webExchange = MockServerWebExchange.builder(MockServerHttpRequest.get("http://localhost:8080").headers(headers).build()).build();
        Mockito.when(repo.findByRegistrationId(anyString())).thenReturn(Mono.just(ClientRegistration
                .withRegistrationId("camunda-identity-service")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId("camunda-identity-service")
                .clientName("camunda-identity-service")
                .clientSecret("101")
                .authorizationUri("http://localhost")
                .redirectUriTemplate("http://localhost")
                        .tokenUri("http://localhost")
                .build()));
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange,"clientRegistrationId");
        StepVerifier.create(resolve).expectComplete();
    }

    @Test
    void resolveWebExchangeAndClientRegistrationWithoutHeaderTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of());
        MockServerWebExchange webExchange = MockServerWebExchange.builder(MockServerHttpRequest.get("http://localhost:8080").headers(headers).build()).build();
        Mockito.when(repo.findByRegistrationId(anyString())).thenReturn(Mono.just(ClientRegistration
                .withRegistrationId("camunda-identity-service")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId("camunda-identity-service")
                .clientName("camunda-identity-service")
                .clientSecret("101")
                .authorizationUri("http://localhost")
                .redirectUriTemplate("http://localhost")
                .tokenUri("http://localhost")
                .build()));
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange,"clientRegistrationId");
        StepVerifier.create(resolve).expectComplete();
    }
}
