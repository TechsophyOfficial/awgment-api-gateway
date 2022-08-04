package com.techsophy.tsf.api;

import utils.ConfigLoader;
import io.restassured.RestAssured;

import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

public class TokenManager {
    private static String access_token;
    private static Instant expiry_time;
    private static String access_tokenm;
    private static Instant expiry_timem;

    public synchronized static String getToken() {
        try {
            if (access_token == null || Instant.now().isAfter(expiry_time)) {
                System.out.println("Renewing token ...");
                Response response = renewToken();
                RestAssured.urlEncodingEnabled = false;
                access_token = response.path("access_token");
                int expiryDurationInSeconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 100);
            } else {
                System.out.println("Token is good to use");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ABORT!!! Failed to get token");
        }
        return access_token;
    }


    private static Response renewToken() {
        HashMap<String, String> Params = new HashMap<String, String>();
        Params.put("client_id", ConfigLoader.getInstance().getClientId());
        Params.put("username", ConfigLoader.getInstance().getUserName());
        Params.put("password", ConfigLoader.getInstance().getPassword());
        Params.put("grant_type", ConfigLoader.getInstance().getGrantType());
        Params.put("client_secret", ConfigLoader.getInstance().getClientSecret());

        Response response = RestResource.augmntToken(Params);

        if (response.statusCode() != 200) {
            throw new RuntimeException("ABORT!!! Renew Token failed");
        }
        return response;
    }

}

