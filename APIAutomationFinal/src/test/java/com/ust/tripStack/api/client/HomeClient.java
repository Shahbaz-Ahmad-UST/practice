package com.ust.tripStack.api.client;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static com.ust.tripStack.support.SpecFactory.*;
import static io.restassured.RestAssured.post;

public class HomeClient {

    public Response searchFlight(String token,String from,String to) {

        return given()
                .spec(commonJsonRequest)
                .header("Authorization", "Bearer "+token)
                .queryParam("from", from)
                .queryParam("to",to)
                .when()
                .get("/flights");
    }


    public Response reset(String token) {
        return given()
                .spec(commonJsonRequest)
                .header("Authorization", "Bearer "+token)
                .when()
                .post("/reset");
    }

}