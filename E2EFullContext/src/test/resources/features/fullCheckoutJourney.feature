Feature: Full Checkout Journey

  Background:
    Given customer logs into Shopkart
@ui @smoke
  Scenario: Complete checkout journey
    Given customer searches for product "Metro Carryall"
    When customer creates cart using API
    And customer adds item using API
    And customer adds the product to cart
    Then product should be visible in cart
    When customer places order using API
    Then order should be stored in database
    And order contract should be valid