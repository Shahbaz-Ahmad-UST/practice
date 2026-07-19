package com.ust.tripStack.tests;

import com.ust.tripStack.report.ExtentTestListener;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static io.restassured.RestAssured.given;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpTimeoutException;
import java.time.Duration;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;
import io.restassured.RestAssured;


@Epic("tripStack Journeys")
@Feature("Full Checkout Journey")
@Owner("Shahbaz Ahmad")
@ExtendWith(ExtentTestListener.class)
public class WireMockTest {

    @RegisterExtension
    static WireMockExtension vm = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    private HttpClient client;

    @BeforeEach
    void setup(){
        RestAssured.baseURI  = vm.baseUrl();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void cleanup() {
        vm.resetAll();
    }

    @Test
    @DisplayName("mock payment decline")
    void paymentDecline(){
        vm.stubFor(get(urlEqualTo("/mocks.tripstack.doomple.com/payment"))
                .willReturn(aResponse()
                        .withStatus(402)
                        .withBody("""
                            {"latency":"500","timeout":"5000","reset":"true"}
                        """)
                        .withHeader("Content-Type", "application/json")
                ));

        given()
                .when()
                .get("/mocks.tripstack.doomple.com/payment")
                .then()
                .statusCode(402)
                .body("timeout", equalTo("5000"));;

        vm.verify(getRequestedFor(urlEqualTo("/mocks.tripstack.doomple.com/payment")));
    }

    @Test
    @DisplayName("seat expiry conflict")
    void seatHoldConflict(){
        vm.stubFor(get(urlEqualTo("/mocks.tripstack.doomple.com/seathold"))
                .willReturn(aResponse()
                        .withStatus(409)
                        .withBody("""
                            {"expiry-period":"10000"}
                        """)
                        .withHeader("Content-Type", "application/json")
                ));

        given()
                .when()
                .get("/mocks.tripstack.doomple.com/seathold")
                .then()
                .statusCode(409)
                .body("expiry-period", equalTo("10000"));

        vm.verify(getRequestedFor(urlEqualTo("/mocks.tripstack.doomple.com/seathold")));
    }
}
