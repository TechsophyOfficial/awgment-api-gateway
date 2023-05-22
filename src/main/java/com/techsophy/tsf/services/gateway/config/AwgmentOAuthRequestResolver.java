package com.techsophy.tsf.services.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.util.Map;


//@Component
public class AwgmentOAuthRequestResolver implements ServerOAuth2AuthorizationRequestResolver {

    private ServerOAuth2AuthorizationRequestResolver defaultResolver;


    private String keycloakIssuerUri;

    private String defaultRealmName;
    private ServerAuthorizationRequestRepository requestRepository;

    public AwgmentOAuthRequestResolver(
            ReactiveClientRegistrationRepository repo,
            @Value("${keycloak.issuer-uri}") String keycloakIssuerUri,
            @Value("${keycloak.realm.name}")String realmName){
        this.defaultResolver = new DefaultServerOAuth2AuthorizationRequestResolver(repo,
                new PathPatternParserServerWebExchangeMatcher("/api"));
        this.defaultRealmName = realmName;
        this.keycloakIssuerUri=keycloakIssuerUri;
    }
//    public AwgmentOAuthRequestResolver(
//            ReactiveClientRegistrationRepository repo,
//            ServerAuthorizationRequestRepository requestRepository,
//            @Value("${keycloak.issuer-uri}") String keycloakIssuerUri,
//            @Value("${keycloak.realm.name}")String realmName){
//        this(repo,keycloakIssuerUri,realmName);
//        this.requestRepository = requestRepository;
//    }


    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
        return resolve(exchange,null);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
        String header = exchange.getRequest().getHeaders().getFirst("X-Tenant");
        String tenant = header==null?"techsophy-platform":header;
        Mono<OAuth2AuthorizationRequest> authRequest=null;
        if(clientRegistrationId==null){
            authRequest = defaultResolver.resolve(exchange);
        }else{
            authRequest = defaultResolver.resolve(exchange,clientRegistrationId);
        }

        if(exchange.getAttributes().get("tenant")==null) {
            exchange.getAttributes().put("tenant", tenant);
        }



        return authRequest.map(auth -> {
            String authURI = keycloakIssuerUri+ tenant+ "/protocol/openid-connect/auth";
//            String tenantAttribute = auth.getAttribute("tenant");
            return  OAuth2AuthorizationRequest.from(auth).authorizationRequestUri(auth.getAuthorizationRequestUri().replace("realms/techsophy-platform", "realms/"+ tenant)).build();
        });

//        return authRequest.map(auth -> {
//            System.out.println(auth.getAuthorizationRequestUri());
//            String authURI = keycloakIssuerUri+ tenant+ "/protocol/openid-connect/auth";
//            return  OAuth2AuthorizationRequest.from(auth).authorizationRequestUri(auth.getAuthorizationRequestUri().replace("techsophy-platform", tenant)).build();
//        });
    }
}
