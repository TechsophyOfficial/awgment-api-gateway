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
        String tenant = getTenant(exchange);
        Mono<OAuth2AuthorizationRequest> authRequest= defaultResolver.resolve(exchange);
        return getoAuth2AuthorizationRequestMono(tenant, authRequest);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId)
    {
        if(clientRegistrationId==null)
        {
          return   resolve(exchange);
        }
        else
        {
            String tenant = getTenant(exchange);
            Mono<OAuth2AuthorizationRequest> authRequest= defaultResolver.resolve(exchange,clientRegistrationId);
            return getoAuth2AuthorizationRequestMono(tenant, authRequest);
        }
    }

    private String getTenant(ServerWebExchange exchange)
    {
        String header = exchange.getRequest().getHeaders().getFirst(X_TENANT);
        if(header==null)
        {
            header = exchange.getRequest().getQueryParams().getFirst(QUERY_PARAM_TENANT);
        }
        return header==null?defaultRealmName:header;
    }

    private Mono<OAuth2AuthorizationRequest> getoAuth2AuthorizationRequestMono(String tenant, Mono<OAuth2AuthorizationRequest> authRequest)
    {
        return authRequest
                .map(auth -> OAuth2AuthorizationRequest
                        .from(auth)
                        .authorizationRequestUri(auth
                                .getAuthorizationRequestUri()
                                .replace(REALMS + defaultRealmName, REALMS + tenant))
                        .build());
    }
}
