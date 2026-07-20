package com.ust.shopkart.tests;

import com.ust.shopkart.api.client.AuthClient;
import com.ust.shopkart.api.client.CartClient;
import com.ust.shopkart.api.client.OrderClient;
import com.ust.shopkart.config.DatabaseConfig;
import com.ust.shopkart.model.OrderRow;
import com.ust.shopkart.report.ExtentTestListener;
import com.ust.shopkart.support.DbSupport;
import com.ust.shopkart.support.E2EContext;
import com.ust.shopkart.support.TestEnvironment;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * M4 - generated from the same user story as the AI draft
 * ("As a customer, I want to cancel my order and have that reflected everywhere").
 * M5 - hardened. Every fix below is traceable to a specific line in the AI draft:
 *
 *   HALLUCINATION LIST (what was caught and replaced)
 *   1. Wrong owner for the fact  -> removed the fake "UI displayed status" assertion;
 *      status is now asserted only where it's actually persisted: the DB row.
 *   2. Hardcoded data            -> email, sku, and address now come from one E2EContext,
 *      set once, reused by every layer.
 *   3. Merging AI unread          -> n/a here since this file was rewritten by hand,
 *      but the draft's Thread.sleep and getLatestOrder() were read, understood, and removed.
 *   4. AI's non-determinism       -> Thread.sleep(3000) deleted. The API call for cancel
 *      is synchronous; there is nothing to wait for.
 *   5. Contract checks values     -> contract step now validates schema shape only,
 *      via order-response-schema.json, never a specific totalPaise number.
 *   6. The "/latest" shortcut     -> orderId comes from E2EContext, captured directly
 *      off the placeOrder response - never re-derived by "get most recent order".
 */
@Epic("Shopkart Journeys")
@Feature("Cancel Order Journey")
@Owner("SDET Trainer")
@ExtendWith(ExtentTestListener.class)
public class CancelOrderJourneyE2ETest {

    private final E2EContext ctx = new E2EContext()
            .withCredentials(
                    TestEnvironment.required("CUSTOMER_1_NAME"),
                    TestEnvironment.required("CUSTOMER_PASSWORD"))
            .withProduct("SKU-BAG", "Metro Carryall")
            .withShippingAddress("Chennai, Tamil Nadu");

    private final AuthClient authClient = new AuthClient();
    private final CartClient cartClient = new CartClient();
    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Cancel order journey: API cart -> API order -> API cancel -> DB persistence -> contract shape")
    @Story("A shopper cancels a placed order and the cancellation is provably real, not just returned")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Hardened from an AI-generated draft: no sleeps, no /latest lookups, no UI-as-DB-proof, contract checks shape only")
    void cancelOrderJourney() {
        login();
        createCartAndAddItem();
        placeOrder();
        cancelOrder();
        dbVerifyOrderCancelled();
        contractValidateCancelledOrderShape();
        secondCancelIsRejected();
    }

    @Step("Login as {0}")
    private void login() {
        String token = authClient.login(ctx.customerEmail(), ctx.customerPassword())
                .then()
                .statusCode(200)
                .extract()
                .path("token");
        ctx.withToken(token);
    }

    @Step("Create cart and add {0}")
    private void createCartAndAddItem() {
        Response cartResponse = cartClient.createCart(ctx.token())
                .then()
                .statusCode(201)
                .extract()
                .response();

        long cartId = cartResponse.jsonPath().getLong("cartId");
        ctx.withCartId(cartId);

        cartClient.addItem(ctx.cartId(), ctx.sku(), 1, ctx.token())
                .then()
                .statusCode(200);
    }

    @Step("Place order for cart {0}")
    private void placeOrder() {
        Response orderResponse = orderClient.placeOrder(ctx.cartId(), ctx.shippingAddress(), ctx.token())
                .then()
                .statusCode(201)
                .extract()
                .response();

        JsonPath orderJson = orderResponse.jsonPath();
        // orderId comes straight off THIS response, never re-fetched via a "latest" lookup
        ctx.withOrderId(orderJson.getLong("id"))
                .withExpectedTotalPaise(orderJson.getInt("totalPaise"));

        assertEquals("PLACED", orderJson.getString("status"));
    }

    @Step("Cancel order {0}")
    private void cancelOrder() {
        Response cancelResponse = orderClient.cancelOrder(ctx.orderId(), ctx.token());

        cancelResponse.then().statusCode(200);
        assertEquals("CANCELLED", cancelResponse.jsonPath().getString("status"));
        // No Thread.sleep: cancellation is synchronous, so the very next call is safe to assert on
    }

    @Step("DB: verify order {0} is CANCELLED at the source of truth")
    private void dbVerifyOrderCancelled() {
        DbSupport db = new DbSupport(DatabaseConfig.fromEnvironmentCredential());
        OrderRow row = db.findOrder(ctx.orderId());

        // This is the only assertion that actually proves persistence.
        // The API returning "CANCELLED" only proves the API said so.
        assertEquals(ctx.orderId(), row.id());
        assertEquals("CANCELLED", row.status());
        assertEquals(ctx.expectedTotalPaise(), row.totalPaise());
    }

    @Step("Contract: validate the cancelled order response shape")
    private void contractValidateCancelledOrderShape() {
        orderClient.getOrder(ctx.orderId(), ctx.token())
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("json/order-response-schema.json"));
        // Deliberately no value assertions here - shape only, per contract-layer ownership
    }

    @Step("Cancelling an already-cancelled order {0} is rejected")
    private void secondCancelIsRejected() {
        orderClient.cancelOrder(ctx.orderId(), ctx.token())
                .then()
                .statusCode(409);
    }
}