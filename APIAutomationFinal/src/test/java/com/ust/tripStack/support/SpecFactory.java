package com.ust.tripStack.support;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class SpecFactory {

    public static final String BASE_URL =TestEnvironment.required("BASE_URL");

    public static RequestSpecification authLoginRequest =new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setBasePath("/api")
            .addHeader("Content-Type","application/json")
            .build();




    public static RequestSpecification commonJsonRequest =
            new RequestSpecBuilder()
                    .setBaseUri(BASE_URL)
                    .setBasePath("/api")
                    .addHeader("Content-Type","application/json")
                    .build();

}
