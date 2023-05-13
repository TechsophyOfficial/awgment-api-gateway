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
import static com.techsophy.tsf.services.gateway.constants.GatewayConstants.X_TENANT;
import static com.techsophy.tsf.services.gateway.constants.GatewayTestConstants.*;
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
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of(X_TENANT, List.of(TECHSOPHY_PLATFORM)));
        MockServerWebExchange webExchange = MockServerWebExchange.builder(MockServerHttpRequest.get(TEST_URL).headers(headers).build()).build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange);
        StepVerifier.create(resolve)
                .expectNext(OAuth2AuthorizationRequest
                        .authorizationCode().authorizationRequestUri(TEST_URL)
                        .authorizationUri(TEST_URL).clientId(CLIENT_ID)
                        .redirectUri(TEST_URL)
                        .build()).expectComplete();
    }

    @Test
    void resolveWebExchangeWithoutHeaderTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of());
        MockServerWebExchange webExchange = MockServerWebExchange.builder(MockServerHttpRequest.get(TEST_URL).headers(headers).build()).build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange);
        StepVerifier.create(resolve)
                .expectNext(OAuth2AuthorizationRequest
                .authorizationCode().authorizationRequestUri(TEST_URL)
                        .authorizationUri(TEST_URL).clientId(CLIENT_ID)
                        .redirectUri(TEST_URL)
                .build()).expectComplete();
    }

    @Test
    void resolveWebExchangeAndClientRegistrationWithHeaderTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of(X_TENANT, List.of(TECHSOPHY_PLATFORM)));
        MockServerWebExchange webExchange = MockServerWebExchange.builder(MockServerHttpRequest.get(TEST_URL).headers(headers).build()).build();
        Mockito.when(repo.findByRegistrationId(anyString())).thenReturn(Mono.just(ClientRegistration
                .withRegistrationId(CLIENT_ID)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId(CLIENT_ID)
                .clientName(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .authorizationUri(TEST_URL)
                .redirectUriTemplate(TEST_URL)
                        .tokenUri(TEST_URL)
                .build()));
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange,CLIENT_REG_ID);
        StepVerifier.create(resolve)
                .expectNext(OAuth2AuthorizationRequest
                        .authorizationCode().authorizationRequestUri(TEST_URL)
                        .authorizationUri(TEST_URL).clientId(CLIENT_ID)
                        .redirectUri(TEST_URL)
                        .build()).expectComplete();
    }

    @Test
    void resolveWebExchangeAndClientRegistrationWithoutHeaderTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of());
        MockServerWebExchange webExchange = MockServerWebExchange.builder(MockServerHttpRequest.get(TEST_URL).headers(headers).build()).build();
        Mockito.when(repo.findByRegistrationId(anyString())).thenReturn(Mono.just(ClientRegistration
                .withRegistrationId(CLIENT_ID)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId(CLIENT_ID)
                .clientName(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .authorizationUri(TEST_URL)
                .redirectUriTemplate(TEST_URL)
                .tokenUri(TEST_URL)
                .build()));
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange,CLIENT_REG_ID);
        StepVerifier.create(resolve)
                .expectNext(OAuth2AuthorizationRequest
                        .authorizationCode().authorizationRequestUri(TEST_URL)
                        .authorizationUri(TEST_URL).clientId(CLIENT_SECRET)
                        .redirectUri(TEST_URL)
                        .build()).expectComplete();
    }
}
