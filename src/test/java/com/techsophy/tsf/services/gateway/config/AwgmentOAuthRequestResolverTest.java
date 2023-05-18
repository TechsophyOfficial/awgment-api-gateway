package com.techsophy.tsf.services.gateway.config;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import java.util.Map;
import static com.techsophy.tsf.services.gateway.constants.GatewayConstants.REALMS;
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

    @BeforeEach
    void beforeAll()
    {
        ReflectionTestUtils.setField(awgmentOAuthRequestResolver,"defaultRealmName","techsophy-platform");
        ReflectionTestUtils.setField(awgmentOAuthRequestResolver,"keycloakIssuerUri"," https://keycloak-tsplatform.techsophy.com/auth/realms/");
    }

    @Test
    void resolveWebExchangeWithHeaderInQueryParamTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of());
        headers.put(AUTHORIZATION,List.of(BEARER_TOKEN));
        MockServerWebExchange webExchange = MockServerWebExchange
                .builder(MockServerHttpRequest
                .get(URL_TEMPLATE+QUERY_PARAM+TESTING_TENANT)
                        .headers(headers)
                .build())
                .build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange);
        StepVerifier.create(resolve)
                        .expectNextMatches(
                                oAuth2AuthorizationRequest ->
                                        oAuth2AuthorizationRequest.getAuthorizationRequestUri()
                                                .contains(REALMS+TESTING_TENANT));
    }

    @Test
    void resolveWebExchangeWithNewHeaderTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of(X_TENANT, List.of(TESTING_TENANT)));
        headers.put(AUTHORIZATION,List.of(BEARER_TOKEN));
        MockServerWebExchange webExchange = MockServerWebExchange
                .builder(MockServerHttpRequest.get(URL_TEMPLATE)
                .headers(headers)
                        .build())
                .build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange);
        StepVerifier.create(resolve)
                .expectNextMatches(
                        oAuth2AuthorizationRequest ->
                                oAuth2AuthorizationRequest.getAuthorizationRequestUri()
                                        .contains(REALMS+TESTING_TENANT));
    }

    @Test
    void resolveWebExchangeWithDefaultHeaderTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of(X_TENANT, List.of(TECHSOPHY_PLATFORM)));
        headers.put(AUTHORIZATION,List.of(BEARER_TOKEN));
        MockServerWebExchange webExchange = MockServerWebExchange
                .builder(MockServerHttpRequest
                        .get(URL_TEMPLATE)
                .headers(headers).build())
                .build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange);
        StepVerifier.create(resolve)
                .expectNextMatches(
                        oAuth2AuthorizationRequest ->
                                oAuth2AuthorizationRequest.getAuthorizationRequestUri()
                                        .contains(REALMS+TECHSOPHY_PLATFORM));
    }

    @Test
    void resolveWebExchangeWithoutHeaderDefaultRealmFromTokenTest()
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of());
        headers.put(AUTHORIZATION,List.of(BEARER_TOKEN));
        MockServerWebExchange webExchange = MockServerWebExchange
                .builder(MockServerHttpRequest.get(URL_TEMPLATE)
                        .headers(headers)
                        .build())
                .build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange);
        StepVerifier.create(resolve)
                .expectNextMatches(
                        oAuth2AuthorizationRequest ->
                                oAuth2AuthorizationRequest.getAuthorizationRequestUri()
                                        .contains(REALMS+TECHSOPHY_PLATFORM));
    }

    @Test
    void resolveWebExchangeAndClientRegistrationWithHeaderTest()
    {
        Mockito.when(repo.findByRegistrationId(anyString())).thenReturn(Mono.just(ClientRegistration
                .withRegistrationId(REGISTRATION_ID)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId(CLIENT_ID)
                .clientName(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .authorizationUri(TEST_URL+REALMS+TECHSOPHY_PLATFORM)
                .redirectUriTemplate(TEST_URL+TECHSOPHY_PLATFORM)
                .tokenUri(TEST_URL)
                .build()));
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of(X_TENANT, List.of(TECHSOPHY_PLATFORM)));
        headers.put(AUTHORIZATION,List.of(BEARER_TOKEN));
        MockServerWebExchange webExchange = MockServerWebExchange.builder(MockServerHttpRequest
                        .get(URL_TEMPLATE)
                .headers(headers).build())
                .build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange,REGISTRATION_ID);
        StepVerifier.create(resolve)
                .expectNextMatches(
                        oAuth2AuthorizationRequest ->
                                oAuth2AuthorizationRequest.getAuthorizationRequestUri()
                                        .contains(REALMS+TECHSOPHY_PLATFORM));
    }

    @Test
    void resolveWebExchangeAndClientRegistrationWithoutHeaderDefaultRealmTest()
    {
        Mockito.when(repo.findByRegistrationId(anyString())).thenReturn(Mono.just(ClientRegistration
                .withRegistrationId(REGISTRATION_ID)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId(CLIENT_ID)
                .clientName(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .authorizationUri(TEST_URL+REALMS+TECHSOPHY_PLATFORM)
                .redirectUriTemplate(TEST_URL+TECHSOPHY_PLATFORM)
                .tokenUri(TEST_URL)
                .build()));
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of());
        headers.put(AUTHORIZATION,List.of(BEARER_TOKEN));
        MockServerWebExchange webExchange = MockServerWebExchange
                .builder(MockServerHttpRequest.get(URL_TEMPLATE)
                        .headers(headers)
                        .build())
                .build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange,REGISTRATION_ID);
        StepVerifier.create(resolve)
                .expectNextMatches(
                        oAuth2AuthorizationRequest ->
                                oAuth2AuthorizationRequest.getAuthorizationRequestUri()
                                        .contains(REALMS+TECHSOPHY_PLATFORM));
    }

    @Test
    void resolveWebExchangeAndClientRegistrationWithNewHeaderTest()
    {
        Mockito.when(repo.findByRegistrationId(anyString())).thenReturn(Mono.just(ClientRegistration
                .withRegistrationId(REGISTRATION_ID)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId(CLIENT_ID)
                .clientName(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .authorizationUri(TEST_URL+REALMS+TECHSOPHY_PLATFORM)
                .redirectUriTemplate(TEST_URL+TECHSOPHY_PLATFORM)
                .tokenUri(TEST_URL)
                .build()));
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of(X_TENANT, List.of(TESTING_TENANT)));
        headers.put(AUTHORIZATION,List.of(BEARER_TOKEN));
        MockServerWebExchange webExchange = MockServerWebExchange
                .builder(MockServerHttpRequest.get(URL_TEMPLATE)
                        .headers(headers)
                        .build())
                .build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange,REGISTRATION_ID);
        StepVerifier.create(resolve)
                .expectNextMatches(
                        oAuth2AuthorizationRequest ->
                                oAuth2AuthorizationRequest.getAuthorizationRequestUri()
                                        .contains(REALMS+TESTING_TENANT));
    }

    @Test
    void resolveWebExchangeAndClientRegistrationWithNewHeaderInQueryTest()
    {
        Mockito.when(repo.findByRegistrationId(anyString())).thenReturn(Mono.just(ClientRegistration
                .withRegistrationId(REGISTRATION_ID)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId(CLIENT_ID)
                .clientName(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .authorizationUri(TEST_URL+REALMS+TECHSOPHY_PLATFORM)
                .redirectUriTemplate(TEST_URL+TECHSOPHY_PLATFORM)
                .tokenUri(TEST_URL)
                .build()));
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(AUTHORIZATION,List.of(BEARER_TOKEN));
        MockServerWebExchange webExchange = MockServerWebExchange
                .builder(MockServerHttpRequest.get(URL_TEMPLATE+QUERY_PARAM+TESTING_TENANT)
                        .headers(headers)
                        .build())
                .build();
        Mono<OAuth2AuthorizationRequest> resolve = awgmentOAuthRequestResolver.resolve(webExchange,REGISTRATION_ID);
        StepVerifier.create(resolve)
                .expectNextMatches(
                        oAuth2AuthorizationRequest ->
                                oAuth2AuthorizationRequest.getAuthorizationRequestUri()
                                        .contains(REALMS+TESTING_TENANT));
    }
}
