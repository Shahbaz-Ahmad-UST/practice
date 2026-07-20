package com.ust.shopkart.stepdefs;

import com.ust.shopkart.api.client.AuthClient;
import com.ust.shopkart.api.client.CartClient;
import com.ust.shopkart.api.client.OrderClient;
import com.ust.shopkart.config.DatabaseConfig;
import com.ust.shopkart.config.SelenideConfig;
import com.ust.shopkart.model.OrderRow;
import com.ust.shopkart.support.DbSupport;
import com.ust.shopkart.support.E2EContext;
import com.ust.shopkart.support.TestEnvironment;
import com.ust.shopkart.ui.locators.CartLocators;
import com.ust.shopkart.ui.pages.CartPage;
import com.ust.shopkart.ui.pages.CheckoutPage;
import com.ust.shopkart.ui.pages.HomePage;
import com.ust.shopkart.ui.pages.LoginPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Shopkart Journeys")
@Feature("Cancel Order Journey")
@Owner("Shahbaz Ahmad")
public class CheckoutStepsE2E {

    private final E2EContext ctx = new E2EContext()
            .withCredentials(
                    TestEnvironment.required("CUSTOMER_1_NAME"),
                    TestEnvironment.required("CUSTOMER_PASSWORD"))
            .withProduct("SKU-BAG", "Metro Carryall")
            .withShippingAddress("Chennai, Tamil Nadu");

    private final AuthClient authClient = new AuthClient();
    private final CartClient cartClient = new CartClient();
    private final OrderClient orderClient = new OrderClient();

    @Before
    public void setUp() {
        SelenideConfig.configure();
    }

    @Given("customer logs into Shopkart")
    public void customer_logs_into_shopkart() {

        LoginPage loginPage = new LoginPage().openLoginPage();
        loginPage.login(ctx.customerEmail(), ctx.customerPassword());

    }

    @Given("customer searches for product {string}")
    public void customer_searches_for_product(String productName) {

        HomePage homePage = new HomePage();
        homePage.searchProduct(productName);

    }

    @When("customer creates cart using API")
    public void customer_creates_cart_using_api() {

        String token = authClient.login(ctx.customerEmail(), ctx.customerPassword())
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        ctx.withToken(token);

        Response response = cartClient.createCart(ctx.token())
                .then()
                .statusCode(201)
                .extract()
                .response();

        long cartId = response.jsonPath().getLong("cartId");

        ctx.withCartId(cartId);

    }

    @When("customer adds item using API")
    public void customer_adds_item_using_api() {

        cartClient.addItem(
                        ctx.cartId(),
                        ctx.sku(),
                        1,
                        ctx.token())
                .then()
                .statusCode(200);

    }

    @When("customer adds the product to cart")
    public void customer_adds_the_product_to_cart() {

        HomePage homePage = new HomePage();
        homePage.addProductToCart(ctx.productName());

    }

    @Then("product should be visible in cart")
    public void product_should_be_visible_in_cart() {

        CartPage cartPage = new CartPage().openCartPage();

        boolean itemVisible = CartLocators.ITEM_NAME_LIST.texts()
                .stream()
                .anyMatch(item -> item.contains(ctx.productName()));

//        assertTrue(itemVisible, "Product should be visible in cart");

    }

    @When("customer places order using API")
    public void customer_places_order_using_api() {

        CheckoutPage checkoutPage = new CheckoutPage().open();

        checkoutPage.enterAddress(ctx.shippingAddress());
        checkoutPage.confirmCheckout();

        Response orderResponse = orderClient.placeOrder(
                        ctx.cartId(),
                        ctx.shippingAddress(),
                        ctx.token())
                .then()
                .statusCode(201)
                .extract()
                .response();

        JsonPath json = orderResponse.jsonPath();

        ctx.withOrderId(json.getLong("id"))
                .withExpectedTotalPaise(json.getInt("totalPaise"));

        assertEquals("PLACED", json.getString("status"));
        assertEquals(ctx.shippingAddress(), json.getString("address"));

    }

    @Then("order should be stored in database")
    public void order_should_be_stored_in_database() {

        DbSupport db = new DbSupport(DatabaseConfig.fromEnvironmentCredential());

        OrderRow row = db.findOrder(ctx.orderId());

        assertEquals(ctx.orderId(), row.id());
        assertEquals(ctx.cartId(), row.cartId());
        assertEquals("PLACED", row.status());
        assertEquals(ctx.expectedTotalPaise(), row.totalPaise());
        assertEquals(ctx.shippingAddress(), row.address());

    }

    @Then("order contract should be valid")
    public void order_contract_should_be_valid() {

        orderClient.getOrder(ctx.orderId(), ctx.token())
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("json/order-response-schema.json"));

    }
}

