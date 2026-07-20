package com.ust.shopkart.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.ust.shopkart.config.SelenideConfig;
import com.ust.shopkart.ui.locators.CheckoutLocators;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CheckoutPage extends BasePage {

    public CheckoutPage openCheckoutPage() {
        openPage(SelenideConfig.checkoutUrl());
        return this;
    }

    public CheckoutPage verifyCheckoutPageLoaded() {
        CheckoutLocators.CHECKOUT_PAGE_TITLE
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Checkout"));

        return this;
    }

    public CheckoutPage enterShippingAddress(String address) {
        type(CheckoutLocators.ADDRESS, address);
        return this;
    }

    public CheckoutPage clickPlaceOrder() {
        CheckoutLocators.PLACE_ORDER
                .shouldBe(Condition.enabled)
                .click();

        return this;
    }
    public CheckoutPage open() {
        Selenide.open("/checkout");   // CONFIRM: is this the real route?
        return this;
    }

    public void enterAddress(String address) {
        CheckoutLocators.ADDRESS.setValue(address);
    }

    public void confirmCheckout() {
        CheckoutLocators.PLACE_ORDER.click();
    }
}