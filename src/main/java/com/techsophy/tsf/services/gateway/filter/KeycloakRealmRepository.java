package com.techsophy.tsf.services.gateway.filter;

import com.techsophy.tsf.services.gateway.service.impl.KeycloakClientCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.ws.rs.client.Client;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeycloakRealmRepository implements ReactiveClientRegistrationRepository, Iterable<ClientRegistration> {

    @Autowired
    private TenantRegistrations tenants;

    @Value("${keycloak.client.id:camunda-identity-service}")
    private String clientId;

    @Autowired
    private KeycloakClientCredentialsService service;

    private Map<String, ClientRegistration> registrationMap;




    @Override
    public Mono<ClientRegistration> findByRegistrationId(String registrationId) {

        return tenants.getRegistrations().stream().filter(s -> s.equals(registrationId)).map(s ->
            Mono.fromCallable(() ->{
                String secret = service.fetchClientDetails(s,false);
            return ClientRegistration.withClientRegistration(registrationMap.get(s))
                    .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                    .clientSecret(secret)
                    .build();
            })
        ).findFirst().get();

    }

    @Override
    public Iterator<ClientRegistration> iterator() {
         registrationMap = tenants.getRegistrations().parallelStream().map(s -> {
            return ClientRegistrations
                    .fromOidcIssuerLocation("https://keycloak-tsplatform.techsophy.com/auth/realms/"+s)
                    .registrationId(s).clientName(s).clientId(clientId).build();
        }).collect(
                Collectors.toMap(clientRegistration ->  clientRegistration.getRegistrationId(),clientRegistration -> clientRegistration
         ));
         return registrationMap.entrySet().stream()
                 .map(entry -> entry.getValue()).collect(Collectors.toList()).iterator();
    }

    public static void main(String[] args) {
        ClientRegistration registration = ClientRegistrations
                .fromIssuerLocation("https://keycloak-tsplatform.techsophy.com/auth/realms/techsophy-platform")
                .clientId("camunda-identity-service")
                .build();
        System.out.println("clients" + registration);
    }
}
