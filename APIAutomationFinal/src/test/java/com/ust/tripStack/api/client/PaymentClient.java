package com.ust.tripStack.api.client;

import io.restassured.response.Response;


import static com.ust.tripStack.support.SpecFactory.commonJsonRequest;
import static io.restassured.RestAssured.given;

public class PaymentClient {

    public Response payment(String token,String id) {


        return given()
                .spec(commonJsonRequest)
                .header("Authorization", "Bearer "+token)
                .pathParam("id",id)
                .post("/bookings/{id}/pay");
    }

    public Response confirm(String token,String id) {


        return given()
                .spec(commonJsonRequest)
                .header("Authorization", "Bearer "+token)
                .pathParam("id",id)
                .post("/bookings/{id}/confirm");
    }
}
