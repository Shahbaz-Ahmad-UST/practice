package com.ust.finalAssessment.factory;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static com.ust.finalAssessment.config.ApiConfiguration.BASE_URL;

public class RequestSpecFactory {
    public static RequestSpecification authRequest =new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setBasePath("/Account/v1")//https://demoqa.com/Account/v1/User
            .addHeader("Content-Type","application/json")
            .build();


    public static RequestSpecification jsonRequest =
            new RequestSpecBuilder()
                    .setBaseUri(BASE_URL)
                    .setBasePath("/BookStore/v1")
                    .addHeader("Content-Type","application/json")
                    .build();

}
