import { Page, TestInfo, expect } from "@playwright/test";
import { AppLogger } from "../src/logger";

import { HomePage } from "../pages/HomePage";
import { FlightPage } from "../pages/FlightPage";
import { SeatPage } from "../pages/SeatPage";
import { PassengerDetailPage, PassengerDetails } from "../pages/PassengerDetailPage";
import { CheckoutPageWithPayment, CardDetails } from "../pages/CheckoutPageWithPayment";
import { TicketPage } from "../pages/TicketPage";
import { MyTripsPage } from "../pages/MyTripsPage";
import { BookingFlowInput } from "./BookingFlow";

export class NegativeFlow {
  readonly home: HomePage;
  readonly flight: FlightPage;
  readonly seat: SeatPage;
  readonly passengerDetails: PassengerDetailPage;
  readonly checkout: CheckoutPageWithPayment;
  readonly ticket: TicketPage;
  readonly myTrips: MyTripsPage;

  constructor(
    private readonly page: Page,
    private readonly log: AppLogger,
    private readonly testInfo: TestInfo,
    private readonly evidence: Record<string, unknown>
  ) {
    this.home = new HomePage(page, log, testInfo, evidence);
    this.flight = new FlightPage(page, log, testInfo, evidence);
    this.seat = new SeatPage(page, log, testInfo, evidence);
    this.passengerDetails = new PassengerDetailPage(page, log, testInfo, evidence);
    this.checkout = new CheckoutPageWithPayment(page, log, testInfo, evidence);
    this.ticket = new TicketPage(page, log, testInfo, evidence);
    this.myTrips = new MyTripsPage(page, log, testInfo, evidence);
  }

  // Invalid Login
  async invalidLogin(email: string, password: string) {
    this.log.info("Step: Invalid Login");

    await this.home.open();
    await this.home.goToLogin();
    await this.home.login(email, password);

    await this.home.verifyInvalidLogin();

    await this.home.captureScreenshot("invalid-login");
  }
}