package com.techsophy.tsf.services.gateway.service;

public interface KeycloakClientCredentialsService
{
    String fetchClientDetails(String tenant, boolean refreshSecret);
}
