package com.techsophy.tsf.api;

import io.restassured.http.ContentType;

import java.util.HashMap;

import static com.techsophy.tsf.api.TokenManager.getToken;
import static io.restassured.RestAssured.given;


public class SampleIT {
    @org.junit.jupiter.api.Test
    void resolve() {

    }

    @org.junit.jupiter.api.Test
    void getAllGroups() {
        HashMap<String, String> groupParams = new HashMap<String, String>();
        groupParams.put("page", "");
        groupParams.put("size", "");
        groupParams.put("sort-by", "");
        given().header("Authorization", "Bearer " + getToken()).queryParams(groupParams)

                .baseUri("https://api-gateway.techsophy.com/api/accounts/v1/keycloak/groups")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }


}
