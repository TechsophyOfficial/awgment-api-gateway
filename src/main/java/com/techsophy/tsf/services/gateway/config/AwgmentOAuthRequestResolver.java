package com.techsophy.tsf.services.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class AwgmentOAuthRequestResolver implements ServerOAuth2AuthorizationRequestResolver {

    private ServerOAuth2AuthorizationRequestResolver defaultResolver;


    private String keycloakIssuerUri;

    private String defaultRealmName;

    public AwgmentOAuthRequestResolver(
            ReactiveClientRegistrationRepository repo,
            @Value("${keycloak.issuer-uri}") String keycloakIssuerUri,
            @Value("${keycloak.realm.name}")String realmName){
        this.defaultResolver = new DefaultServerOAuth2AuthorizationRequestResolver(repo);
        this.defaultRealmName = realmName;
        this.keycloakIssuerUri=keycloakIssuerUri;
    }


    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
//        String tenant =TenantAuthenticationManagerResolver.toTenant(exchange);
        String header = exchange.getRequest().getHeaders().getFirst("X-Tenant");
        if(header==null)
            header =TenantAuthenticationManagerResolver.toTenant(exchange);
        String tenant = header==null?"techsophy-platform":header;
        Mono<OAuth2AuthorizationRequest> authRequest= defaultResolver.resolve(exchange);
        return authRequest.map(auth -> {
            String authURI = keycloakIssuerUri+ tenant+ "/protocol/openid-connect/auth";
            return  OAuth2AuthorizationRequest.from(auth).authorizationRequestUri(auth.getAuthorizationRequestUri().replace("realms/techsophy-platform", "realms/"+ tenant)).build();
        });

    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
        String header = exchange.getRequest().getHeaders().getFirst("X-Tenant");
        if(header==null)
            header =TenantAuthenticationManagerResolver.toTenant(exchange);
        String tenant = header==null?"techsophy-platform":header;
        Mono<OAuth2AuthorizationRequest> authRequest= defaultResolver.resolve(exchange,clientRegistrationId);
        return authRequest.map(auth -> {
            System.out.println(auth.getAuthorizationRequestUri());
            String authURI = keycloakIssuerUri+ tenant+ "/protocol/openid-connect/auth";
            return  OAuth2AuthorizationRequest.from(auth).authorizationRequestUri(auth.getAuthorizationRequestUri().replace("techsophy-platform", tenant)).build();
        });
    }
}
