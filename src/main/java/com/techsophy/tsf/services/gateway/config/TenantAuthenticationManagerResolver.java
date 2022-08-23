package com.techsophy.tsf.services.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.tsf.services.gateway.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.*;

@Component
public class TenantAuthenticationManagerResolver implements ReactiveAuthenticationManagerResolver<ServerWebExchange> {
    private final Map<String, ReactiveAuthenticationManager> authenticationManagers = new HashMap<>();

    @Value("${keycloak.service-url}")
    private String keycloakIssuerUri;

    @Override
    public Mono<ReactiveAuthenticationManager> resolve(ServerWebExchange request) {
        return Mono.just(this.authenticationManagers.computeIfAbsent(toTenant(request), this::fromTenant));
    }

    private String toTenant(ServerWebExchange request) {
        String stoken = request.getRequest().getHeaders().getFirst("Authorization");
        try {
            return getIssuerFromToken(stoken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stoken;
    }

    private ReactiveAuthenticationManager fromTenant(String tenant) {
        return (Optional.ofNullable(keycloakIssuerUri + tenant)
                .map(ReactiveJwtDecoders::fromIssuerLocation)
                .map(JwtReactiveAuthenticationManager::new)
                .orElseThrow(() -> new IllegalArgumentException("unknown tenant"))::authenticate);
    }

    public static String getIssuerFromToken(String idToken) throws JsonProcessingException {
        String tenantName = "";
        final Base64.Decoder decoder = Base64.getDecoder();
        if (idToken.startsWith("Bearer ")) {
            idToken = idToken.substring(7);
        }
        Map<String, Object> tokenBody = new HashMap<>();
        List<String> tokenizer = Arrays.asList(idToken.split("\\."));
        for (String token : tokenizer) {
            if (token.equals(tokenizer.get(1))) {
                tokenBody = string2JSONMap(new String(decoder.decode(token)));
            }
        }
        if (tokenBody == null) {
            throw new InvalidInputException("Invalid Token");
        }
        if (!tokenBody.isEmpty() && tokenBody.containsKey("iss")) {
            List<String> elements = Arrays.asList(tokenBody.get("iss").toString().split("/"));
            tenantName = elements.get(elements.size() - 1);
        }
        return tenantName;
    }

    public static Map<String, Object> string2JSONMap(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // convert JSON string to Map
        return mapper.readValue(json, new TypeReference<>() {
        });
    }
}