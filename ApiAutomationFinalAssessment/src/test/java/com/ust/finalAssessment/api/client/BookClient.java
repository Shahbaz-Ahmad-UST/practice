package com.ust.finalAssessment.api.client;

import io.restassured.response.Response;


import static com.ust.finalAssessment.factory.RequestSpecFactory.*;

import static io.restassured.RestAssured.given;

public class BookClient {

    public Response getListOfBook(String token) {

        return given()
                .spec(jsonRequest)
                .header("Authorization","Bearer "+token)
                .when()
                .get("/Books");
    }
}