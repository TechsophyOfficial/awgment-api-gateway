package com.techsophy.tsf.services.gateway.service.impl;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class KeycloakClientCredentialsService implements com.techsophy.tsf.services.gateway.service.KeycloakClientCredentialsService {

    @Value("${keycloak.master.username}")
    String userName;

    @Value("${keycloak.master.password}")
    String password;

    @Value("${keycloak.auth-server-url}")
    String keycloakAuthUrl;

    @Value("${keycloak.client.id}")
    String clientId;

    @Value("${keycloak.master.realm.name:master}")
    String adminRealmName;

    @Value("${keycloak.master.client.id:admin-cli}")
    String adminClientId;
    private Map<String, String> tenantSecretCache = new ConcurrentHashMap<>();

    private Keycloak keycloak;
    public void init() {
        if(keycloak==null) {
            keycloak = Keycloak.getInstance(keycloakAuthUrl, adminRealmName, userName, password, adminClientId);
        }
    }

    @Override
    public String fetchClientDetails(String tenant, boolean refreshSecret)  {
        String clientDetails =null;

        if(!refreshSecret){
            clientDetails = tenantSecretCache.get(tenant);
            if(clientDetails!=null){
                return clientDetails;
            }
        }
        clientDetails = fetchClientDetails(tenant);
        tenantSecretCache.put(tenant,clientDetails);
        return clientDetails;
    }


    private String fetchClientDetails(String tenant)  {
        init();
        RealmResource realm = keycloak
                .realm(tenant);
        if(realm==null)
        {
            throw new IllegalArgumentException();
        }
        List<ClientRepresentation> clientRepresentation;
        clientRepresentation = realm.clients()
                .findByClientId(clientId);
        if(clientRepresentation.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        String secret = realm.clients().get(clientRepresentation.get(0).getId()).getSecret().getValue();
//        ClientDetails clientDetails=new ClientDetails();
//        clientDetails.setClientId(clientId);
//        clientDetails.setSecret(secret);
        return secret;
    }
}
