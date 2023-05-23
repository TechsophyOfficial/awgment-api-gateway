package com.techsophy.tsf.services.gateway.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.ArrayList;
import java.util.List;
import static com.techsophy.tsf.services.gateway.constants.GatewayTestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakClientCredentialServiceTest
{
    @Mock
    private Keycloak keycloak;
    @InjectMocks
    KeycloakClientCredentialServiceImpl keycloakClientCredentialService;

    @BeforeEach
    void before()
    {
        ReflectionTestUtils.setField(keycloakClientCredentialService,"keycloakAuthUrl","https://keycloak-tsplatform.techsophy.com/auth/realms/");
        ReflectionTestUtils.setField(keycloakClientCredentialService,"adminRealmName","techsophy-platform");
        ReflectionTestUtils.setField(keycloakClientCredentialService,"userName","test");
        ReflectionTestUtils.setField(keycloakClientCredentialService,"password","test");
        ReflectionTestUtils.setField(keycloakClientCredentialService,"adminClientId","camunda-identity-service");
    }

    @Test
    void testClientSecret()
    {
        RealmResource realmResource= Mockito.mock(RealmResource.class);
        when(keycloak.realm(anyString())).thenReturn(realmResource);
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setId(CLIENT_ID);
        ClientsResource clientsResource=Mockito.mock(ClientsResource.class);
        when(realmResource.clients()).thenReturn(clientsResource);
        ClientResource clientResource=mock(ClientResource.class);
        when(clientsResource.get(any())).thenReturn(clientResource);
        List<ClientRepresentation> clientRepresentationList =new ArrayList<>();
        ClientRepresentation clientRepresentationTest=new ClientRepresentation();
        clientRepresentationTest.setId(CLIENT_SECRET);
        clientRepresentationList.add(clientRepresentationTest);
        when(clientsResource.findByClientId(any())).thenReturn(clientRepresentationList);
        CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
        credentialRepresentation.setValue(CLIENT_SECRET);
        when(clientResource.getSecret()).thenReturn(credentialRepresentation);
        Assertions.assertEquals(CLIENT_SECRET,keycloakClientCredentialService.fetchClientDetails("techsophy-platform",false));
    }

    @Test
    void testClientSecretRealmIsNullTest()
    {
        when(keycloak.realm(anyString())).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,()->keycloakClientCredentialService.fetchClientDetails(CLIENT_ID,false));
    }

    @Test
    void testClientSecretRealmNoClientByIdTest()
    {
        RealmResource realmResource= Mockito.mock(RealmResource.class);
        when(keycloak.realm(anyString())).thenReturn(realmResource);
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setId(CLIENT_ID);
        ClientsResource clientsResource=Mockito.mock(ClientsResource.class);
        when(realmResource.clients()).thenReturn(clientsResource);
        List<ClientRepresentation> clientRepresentationList =new ArrayList<>();
        when(clientsResource.findByClientId(any())).thenReturn(clientRepresentationList);
        Assertions.assertThrows(IllegalArgumentException.class,()->keycloakClientCredentialService.fetchClientDetails(TECHSOPHY_PLATFORM,false));
    }
}
