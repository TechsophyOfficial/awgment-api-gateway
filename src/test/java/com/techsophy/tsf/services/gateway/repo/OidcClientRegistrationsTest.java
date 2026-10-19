package com.techsophy.tsf.services.gateway.repo;

import com.techsophy.tsf.services.gateway.repository.OidcClientRegistrations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import static com.techsophy.tsf.services.gateway.support.MockKeycloakServer.*;

class OidcClientRegistrationsTest
{
    @Test
    void keycloak26DiscoveryBuildsRegistration()
    {
        String issuer = issuerPrefix() + "medunited";
        ClientRegistration registration = OidcClientRegistrations.fromOidcIssuerLocation(issuer)
                .registrationId("medunited").clientId("awgment-api").clientSecret("test-secret").build();
        ClientRegistration.ProviderDetails provider = registration.getProviderDetails();
        Assertions.assertEquals(issuer + "/protocol/openid-connect/auth", provider.getAuthorizationUri());
        Assertions.assertEquals(issuer + "/protocol/openid-connect/token", provider.getTokenUri());
        Assertions.assertEquals(issuer + "/protocol/openid-connect/certs", provider.getJwkSetUri());
        Assertions.assertEquals(issuer + "/protocol/openid-connect/userinfo", provider.getUserInfoEndpoint().getUri());
        Assertions.assertEquals(issuer, provider.getConfigurationMetadata().get("issuer"));
        Assertions.assertTrue(provider.getConfigurationMetadata().containsKey("mtls_endpoint_aliases"));
        Assertions.assertEquals(ClientAuthenticationMethod.BASIC, registration.getClientAuthenticationMethod());
        Assertions.assertTrue(registration.getScopes().contains("openid"));
    }

    @Test
    void matchesSpringSecurityForDiscoveryWithoutNestedObjects()
    {
        String issuer = issuerPrefix() + LEGACY_REALM;
        ClientRegistration ours = OidcClientRegistrations.fromOidcIssuerLocation(issuer).clientId("awgment-api").build();
        ClientRegistration spring = ClientRegistrations.fromOidcIssuerLocation(issuer).clientId("awgment-api").build();
        Assertions.assertEquals(spring.getRegistrationId(), ours.getRegistrationId());
        Assertions.assertEquals(spring.getClientName(), ours.getClientName());
        Assertions.assertEquals(spring.getScopes(), ours.getScopes());
        Assertions.assertEquals(spring.getClientAuthenticationMethod(), ours.getClientAuthenticationMethod());
        Assertions.assertEquals(spring.getAuthorizationGrantType(), ours.getAuthorizationGrantType());
        Assertions.assertEquals(spring.getRedirectUriTemplate(), ours.getRedirectUriTemplate());
        Assertions.assertEquals(spring.getProviderDetails().getAuthorizationUri(), ours.getProviderDetails().getAuthorizationUri());
        Assertions.assertEquals(spring.getProviderDetails().getTokenUri(), ours.getProviderDetails().getTokenUri());
        Assertions.assertEquals(spring.getProviderDetails().getJwkSetUri(), ours.getProviderDetails().getJwkSetUri());
        Assertions.assertEquals(spring.getProviderDetails().getUserInfoEndpoint().getUri(), ours.getProviderDetails().getUserInfoEndpoint().getUri());
        Assertions.assertEquals(spring.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(),
                ours.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
    }

    @Test
    void springSecurityStillRejectsKeycloak26Discovery()
    {
        // Documents why OidcClientRegistrations exists; if this starts passing after an upgrade, it can be removed.
        String issuer = issuerPrefix() + "medunited";
        Assertions.assertThrows(IllegalArgumentException.class, () -> ClientRegistrations.fromOidcIssuerLocation(issuer));
    }

    @Test
    void issuerMismatchIsRejected()
    {
        String issuer = issuerPrefix() + MISMATCH_REALM;
        Assertions.assertThrows(IllegalStateException.class, () -> OidcClientRegistrations.fromOidcIssuerLocation(issuer));
    }

    @Test
    void missingRealmIsRejected()
    {
        String issuer = issuerPrefix() + MISSING_REALM;
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> OidcClientRegistrations.fromOidcIssuerLocation(issuer));
        Assertions.assertTrue(e.getMessage().contains(issuer));
    }
}
