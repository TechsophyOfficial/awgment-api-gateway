package com.techsophy.tsf.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.File;
import java.util.HashMap;

import static com.techsophy.tsf.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

public class RestResource {

    public static Response post(String path, String token, Object payload) {

        return given(getRequestSpec()).
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                body(payload).
                when().post(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }


    public static Response augmntToken(HashMap<String, String> formParams) {
        return given(getTokenForm()).
                formParams(formParams).
                when().post("realms/techsophy-platform/protocol/openid-connect/token").
                then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response get(String path, String token) {
        return given(getRequestSpec()).
                header("Authorization", "Bearer " + token).
                when().get(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }


    public static Response update(String path, String token, Object requestPlaylist) {
        return given(getRequestSpec()).
                header("Authorization", "Bearer " + token).
                body(requestPlaylist).
                when().put(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response patch(String path, String token, Object requestPlaylist) {
        return given(getRequestSpec()).
                header("Authorization", "Bearer " + token).
                body(requestPlaylist).
                when().patch(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response put(String path, String token, Object putdata) {
        return given(getRequestSpec()).
                header("Authorization", "Bearer " + token).
                body(putdata).
                when().put(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response delete(String path, String token) {
        return given(getRequestSpec()).
                header("Authorization", "Bearer " + token).
                when().delete(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response postfile(String path, String token, File file) {

        return given(getRequestSpec()).
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                body(file).
                when().post(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }


}

