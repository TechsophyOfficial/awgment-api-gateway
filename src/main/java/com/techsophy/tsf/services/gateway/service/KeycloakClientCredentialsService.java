package com.techsophy.tsf.services.gateway.service;

public interface KeycloakClientCredentialsService {
    //    @Override
    String fetchClientDetails(String tenant, boolean refreshSecret);
}
