package com.techsophy.tsf.services.gateway.service.impl;

import com.techsophy.tsf.services.gateway.service.KeycloakClientCredentialsService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeycloakClientCredentialServiceImpl implements KeycloakClientCredentialsService
{
    @Value("${keycloak.multi-realm.username}")
    String userName;

    @Value("${keycloak.multi-realm.password}")
    String password;

    @Value("${keycloak.auth-server-url}")
    String keycloakAuthUrl;

    @Value("${keycloak.client.id}")
    String clientId;

    @Value("${keycloak.master.realm.name:master}")
    String adminRealmName;

    @Value("${keycloak.master.client.id:admin-cli}")
    String adminClientId;

    private final Map<String, String> tenantSecretCache = new ConcurrentHashMap<>();

    private Keycloak keycloak;
    public void init()
    {
        if(keycloak==null)
        {
            keycloak = Keycloak.getInstance(keycloakAuthUrl, adminRealmName, userName, password, adminClientId);
        }
    }

    @Override
    public String fetchClientDetails(String tenant, boolean refreshSecret)
    {
        String clientDetails;
        if(!refreshSecret)
        {
            clientDetails = tenantSecretCache.get(tenant);
            if(clientDetails!=null)
            {
                return clientDetails;
            }
        }
        clientDetails = fetchClientDetails(tenant);
        tenantSecretCache.put(tenant,clientDetails);
        return clientDetails;
    }

    private String fetchClientDetails(String tenant)
    {
        init();
        RealmResource realm = keycloak.realm(tenant);
        if(realm==null)
        {
            throw new IllegalArgumentException();
        }
        List<ClientRepresentation> clientRepresentation = realm.clients().findByClientId(clientId);
        if(clientRepresentation.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        return realm.clients().get(clientRepresentation.get(0).getId()).getSecret().getValue();
    }
}
