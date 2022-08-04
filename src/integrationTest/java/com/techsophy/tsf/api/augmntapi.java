package com.techsophy.tsf.api;

import io.restassured.response.Response;

import java.io.File;

import static com.techsophy.tsf.api.TokenManager.getToken;


public class augmntapi {


    public static Response post(Object data, String path) {
        return RestResource.post(path, getToken(), data);
    }

    public static Response put(Object putdata, String path) {
        return RestResource.put(path, getToken(), putdata);
    }


    public static Response get(String path) {
        return RestResource.get(path, getToken());
    }

    public static Response postFile(File filedata, String path) {
        return RestResource.postfile(path, getToken(), filedata);
    }

    public static Response update(Object updatedata, String path) {
        return RestResource.update(path, getToken(), updatedata);
    }

    public static Response patch(Object patchdata, String path) {
        return RestResource.patch(path, getToken(), patchdata);
    }


    public static Response delete(String path) {
        return RestResource.delete(path, getToken());
    }

}


