package com.ust.tripStack.api.client;

import io.restassured.response.Response;


import static com.ust.tripStack.support.SpecFactory.commonJsonRequest;
import static io.restassured.RestAssured.given;

public class TicketClient {



    public Response getBooking( String token) {

        return given()
                .spec(commonJsonRequest)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/bookings");
    }

    public Response cancelBooking(String orderId, String token) {

        return given()
                .spec(commonJsonRequest)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", orderId)
                .when()
                .post("/bookings/{id}/cancel");
    }

    public Response getPNR(String token,String pnr) {

        return given()
                .spec(commonJsonRequest)
                .header("Authorization", "Bearer " + token)
                .pathParam("pnr", pnr)
                .when()
                .post("/bookings/{pnr}");
    }

}
