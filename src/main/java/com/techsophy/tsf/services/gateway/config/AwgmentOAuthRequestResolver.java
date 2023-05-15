package com.techsophy.tsf.services.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import static com.techsophy.tsf.services.gateway.constants.GatewayConstants.*;

@Component
public class AwgmentOAuthRequestResolver implements ServerOAuth2AuthorizationRequestResolver
{
    private final ServerOAuth2AuthorizationRequestResolver defaultResolver;
    private final String keycloakIssuerUri;
    private final String defaultRealmName;

    public AwgmentOAuthRequestResolver(ReactiveClientRegistrationRepository repo,
            @Value("${keycloak.issuer-uri}") String keycloakIssuerUri,
            @Value("${keycloak.realm.name}")String realmName)
    {
        this.defaultResolver = new DefaultServerOAuth2AuthorizationRequestResolver(repo);
        this.defaultRealmName = realmName;
        this.keycloakIssuerUri=keycloakIssuerUri;
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange)
    {
        String header = exchange.getRequest().getHeaders().getFirst(X_TENANT);
        if(header==null)
        {
            header = exchange.getRequest().getQueryParams().getFirst(QUERY_PARAM_TENANT);
        }
        String tenant = header==null?defaultRealmName:header;
        Mono<OAuth2AuthorizationRequest> authRequest= defaultResolver.resolve(exchange);
        return authRequest.map(auth -> OAuth2AuthorizationRequest.from(auth).authorizationRequestUri(auth.getAuthorizationRequestUri().replace(REALMS+defaultRealmName, REALMS+ tenant)).build());
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId)
    {
        String header = exchange.getRequest().getHeaders().getFirst(X_TENANT);
        if(header==null)
        {
            header = exchange.getRequest().getQueryParams().getFirst(QUERY_PARAM_TENANT);
        }
        String tenant = header==null?defaultRealmName:header;
        Mono<OAuth2AuthorizationRequest> authRequest= defaultResolver.resolve(exchange,clientRegistrationId);
        return authRequest.map(auth -> OAuth2AuthorizationRequest.from(auth).authorizationRequestUri(auth.getAuthorizationRequestUri().replace(REALMS+defaultRealmName, REALMS+ tenant)).build());
    }
}
