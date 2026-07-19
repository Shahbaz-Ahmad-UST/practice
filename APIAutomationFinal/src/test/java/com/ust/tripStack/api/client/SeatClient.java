package com.ust.tripStack.api.client;

import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static com.ust.tripStack.support.SpecFactory.commonJsonRequest;
import static io.restassured.RestAssured.given;

public class SeatClient {

    public Response getFlight(String token, String id) {

        return given()
                .spec(commonJsonRequest)
                .header("Authorization", "Bearer "+token)
                .pathParam("id", id)
                .get("/flights/{id}/seats");
    }

    public Response booking(String token) {

        {
                            // optional (testing hook for the expiry negative)
        }

        var resBody = Map.of(
                "journeyType", "flight",
                "inventoryId", "FL-BLRCCU-57",
                "seatIds", List.of("2A"),
                  "refundable", true,
                "holdTtlSec", 120
        );

        return given()
                .spec(commonJsonRequest)
                .header("Authorization", "Bearer "+token)
                .body(resBody)
                .post("/bookings");
    }
}
