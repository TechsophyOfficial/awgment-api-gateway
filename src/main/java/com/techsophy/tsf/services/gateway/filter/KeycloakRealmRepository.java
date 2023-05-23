package com.techsophy.tsf.services.gateway.filter;

import com.techsophy.tsf.services.gateway.service.KeycloakClientCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeycloakRealmRepository implements ReactiveClientRegistrationRepository,Iterable<ClientRegistration>
{
    @Autowired
    private TenantRegistration tenants;

    @Value("${keycloak.client.id}")
    private String clientId;

    @Value("${keycloak.issuer-uri}")
    private String keycloakIssuerURI;

    @Autowired
    private KeycloakClientCredentialsService service;

    private Map<String, ClientRegistration> registrationMap;
    @Override
    public Iterator<ClientRegistration> iterator()
    {
        registrationMap = tenants.getRegistrations().parallelStream().map(s -> ClientRegistrations
                .fromOidcIssuerLocation(keycloakIssuerURI+s)
                .registrationId(s).clientName(s).clientId(clientId).build()).collect(
                Collectors.toMap(ClientRegistration::getRegistrationId, clientRegistration -> clientRegistration
                ));
        return new ArrayList<>(registrationMap.values()).iterator();
    }

    @Override
    public Mono<ClientRegistration> findByRegistrationId(String registrationId)
    {
        return tenants.getRegistrations().stream().filter(s -> s.equals(registrationId)).map(s ->
                Mono.fromCallable(() ->{
                    String secret = service.fetchClientDetails(s,false);
                    return ClientRegistration.withClientRegistration(registrationMap.get(s))
                            .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                            .clientSecret(secret)
                            .build();
                })).findFirst().orElseThrow();
    }
}
