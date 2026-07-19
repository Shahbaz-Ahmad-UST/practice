Feature: Full Checkout Journey

  Background:
    Given customer logs into tripStack

  @ui @smoke
  Scenario: Complete checkout journey
    Given customer resets and searches for flight
    When customer selects seat using API
    And customer books seat using API
    Then booking should be visible in order
    When customer pays and confirms booking
    Then booking should be visible in ticket API
    And booking should be stored in database
    And booking contract should be valid