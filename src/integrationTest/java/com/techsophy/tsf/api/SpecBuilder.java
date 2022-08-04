package com.techsophy.tsf.api;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.techsophy.tsf.api.Route.AUTH;

public class SpecBuilder {

    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder().

                setBaseUri("https://api-gateway.techsophy.com").
                setContentType(ContentType.JSON).
                log(LogDetail.ALL).
                build();
    }

    public static RequestSpecification getTokenForm() {
        return new RequestSpecBuilder().
                setBaseUri("https://keycloak-tsplatform.techsophy.com/").
                setBasePath(AUTH).
                setContentType(ContentType.URLENC).

                //addFilter(new AllureRestAssured()).
                log(LogDetail.ALL).
                build();
    }

    public static ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder().

                log(LogDetail.ALL).
                build();
    }


}

