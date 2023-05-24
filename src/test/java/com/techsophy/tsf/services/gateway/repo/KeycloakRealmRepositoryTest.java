package com.techsophy.tsf.services.gateway.repo;

import com.techsophy.tsf.services.gateway.dto.TenantRegistration;
import com.techsophy.tsf.services.gateway.repository.KeycloakRealmRepository;
import com.techsophy.tsf.services.gateway.service.KeycloakClientCredentialsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.techsophy.tsf.services.gateway.constants.GatewayConstants.REALMS;
import static com.techsophy.tsf.services.gateway.constants.GatewayTestConstants.*;

@ExtendWith(MockitoExtension.class)
class KeycloakRealmRepositoryTest
{
    @Mock
    KeycloakClientCredentialsService keycloakClientCredentialsService;
    @InjectMocks
    KeycloakRealmRepository keycloakRealmRepository;

    @Test
    void testFindByRegistrationIdRefreshSecretFalse()
    {
        Map<String,ClientRegistration> registrationMap=new HashMap<>();
        registrationMap.put(TECHSOPHY_PLATFORM,ClientRegistration
                .withRegistrationId(TECHSOPHY_PLATFORM)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .clientId(CLIENT_ID)
                        .redirectUriTemplate(TEST_URL+REALMS+TECHSOPHY_PLATFORM)
                        .authorizationUri(TEST_URL+REALMS+TECHSOPHY_PLATFORM)
                        .tokenUri(TEST_URL)
                .build());
        ReflectionTestUtils.setField(keycloakRealmRepository,"registrationMap",registrationMap);
        List<String> tenantsList=new ArrayList<>();
        tenantsList.add(TECHSOPHY_PLATFORM);
        tenantsList.add(KIMS);
        TenantRegistration tenantRegistration=new TenantRegistration();
        tenantRegistration.setRegistrations(tenantsList);
        ReflectionTestUtils.setField(keycloakRealmRepository,"tenants",tenantRegistration);
        Mockito.when(keycloakClientCredentialsService.fetchClientDetails(Mockito.anyString(),Mockito.anyBoolean())).thenReturn(CLIENT_SECRET);
        Mono<ClientRegistration> clientRegistrationMono=keycloakRealmRepository.findByRegistrationId(TECHSOPHY_PLATFORM);
        ClientRegistration clientRegistration=clientRegistrationMono.block();
        Assertions.assertNotNull(clientRegistration);
        Assertions.assertEquals(CLIENT_SECRET,clientRegistration.getClientSecret());
        Assertions.assertEquals(CLIENT_ID,clientRegistration.getClientId());
    }

    @Test
    void testFindByRegistrationIdRefreshSecretTrue()
    {
        Map<String,ClientRegistration> registrationMap=new HashMap<>();
        registrationMap.put(TECHSOPHY_PLATFORM,ClientRegistration
                .withRegistrationId(TECHSOPHY_PLATFORM)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId(CLIENT_ID)
                .redirectUriTemplate(TEST_URL+REALMS+TECHSOPHY_PLATFORM)
                .authorizationUri(TEST_URL+REALMS+TECHSOPHY_PLATFORM)
                .tokenUri(TEST_URL)
                .build());
        ReflectionTestUtils.setField(keycloakRealmRepository,"registrationMap",registrationMap);
        List<String> tenantsList=new ArrayList<>();
        tenantsList.add(TECHSOPHY_PLATFORM);
        tenantsList.add(KIMS);
        TenantRegistration tenantRegistration=new TenantRegistration();
        tenantRegistration.setRegistrations(tenantsList);
        ReflectionTestUtils.setField(keycloakRealmRepository,"tenants",tenantRegistration);
        Mockito.when(keycloakClientCredentialsService.fetchClientDetails(Mockito.anyString(),Mockito.eq(false))).thenReturn(null);
        Mockito.when(keycloakClientCredentialsService.fetchClientDetails(Mockito.anyString(),Mockito.eq(true))).thenReturn(CLIENT_SECRET);
        Mono<ClientRegistration> clientRegistrationMono=keycloakRealmRepository.findByRegistrationId(TECHSOPHY_PLATFORM);
        ClientRegistration clientRegistration=clientRegistrationMono.block();
        Assertions.assertNotNull(clientRegistration);
        Assertions.assertEquals(CLIENT_SECRET,clientRegistration.getClientSecret());
        Assertions.assertEquals(CLIENT_ID,clientRegistration.getClientId());
    }
}
