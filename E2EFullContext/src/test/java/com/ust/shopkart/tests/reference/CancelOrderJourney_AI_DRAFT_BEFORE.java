package com.ust.shopkart.tests.reference;

// ============================================================================
// RAW AI-GENERATED DRAFT - DO NOT RUN, DO NOT WIRE INTO ANY GRADLE TASK.
// Kept only as "before" evidence for the hardening milestone (M4/M5).
// Every numbered comment below maps to a row on the "Where E2E assembly
// goes wrong" pitfalls slide.
// ============================================================================

import com.ust.shopkart.api.client.AuthClient;
import com.ust.shopkart.api.client.CartClient;
import com.ust.shopkart.api.client.OrderClient;
import com.ust.shopkart.support.TestEnvironment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CancelOrderJourney_AI_DRAFT_BEFORE {

    AuthClient auth = new AuthClient();
    CartClient cart = new CartClient();
    OrderClient order = new OrderClient();

    @Test
    void cancelOrderJourney() throws InterruptedException {

        String token = auth.login(TestEnvironment.required("CUSTOMER_1_NAME"), TestEnvironment.required("CUSTOMER_PASSWORD"))   // (2) HARDCODED DATA
                .then().statusCode(200).extract().path("token");

        var cartResp = cart.createCart(token).then().statusCode(201).extract().response();
        long cartId = cartResp.jsonPath().getLong("cartId");

        cart.addItem(cartId, "SKU-BAG", 1, token).then().statusCode(200);        // (2) HARDCODED DATA - literal repeated, not sourced once

        var orderResp = order.placeOrder(cartId, "Chennai, Tamil Nadu", token)   // (2) HARDCODED DATA - address literal again
                .then().statusCode(201).extract().response();
        System.out.println(orderResp.prettyPrint());
      // (4) AI'S NON-DETERMINISM - "wait for processing", races the real system

        var latestOrder = order.getLatestOrder(orderResp.jsonPath().getLong("id"),token)                            // (6) THE "/latest" SHORTCUT - order-dependent, flaky under parallel runs
                .then().statusCode(200).extract().response();
        long orderId = latestOrder.jsonPath().getLong("id");

        var cancelResp = order.cancelOrder(orderId, token);
        cancelResp.then().statusCode(200);

        // UI total shown on a hypothetical confirmation screen used as "proof" the DB updated:
        // (1) WRONG OWNER FOR THE FACT - the UI does not prove persistence, only the DB does
        String uiDisplayedStatus = "CANCELLED"; // pretend this came from scraping the UI
        assertEquals("CANCELLED", uiDisplayedStatus);

        // Contract layer asserting an exact value instead of shape:
        // (5) CONTRACT CHECKS VALUES - a contract test should never assert totalPaise == 49900
        cancelResp.then().body("totalPaise", org.hamcrest.Matchers.equalTo(49900));
    }
}