import { Page, TestInfo } from "@playwright/test";
import { AppLogger } from "../src/logger";

import { HomePage } from "../pages/HomePage";
import { FlightPage } from "../pages/FlightPage";
import { SeatPage } from "../pages/SeatPage";
import { PassengerDetailPage, PassengerDetails } from "../pages/PassengerDetailPage";
import { CheckoutPageWithPayment, CardDetails } from "../pages/CheckoutPageWithPayment";
import { TicketPage } from "../pages/TicketPage";
import { MyTripsPage } from "../pages/MyTripsPage";

export interface PaymentLatencyInput {
  credentials: { email: string; password: string };

  search: {
    from: string;
    fromOptionLabel: string;
    to: string;
    toOptionLabel: string;
    date: string;
  };

  flightLabel: string;

  seatDescription: string;

  passengerSeatLabel: string;

  passenger: Omit<PassengerDetails, "seatLabel">;

  card: CardDetails;

  couponLabel: string;

  bookingRefPrefix: string;

  myTripsSummary: string;
}


export class PaymentLatencyFlow {
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

  async login(email: string, password: string) {
    this.log.info("Step: Login");
    await this.home.open();
    await this.home.goToLogin();
    await this.home.login(email, password);
  }

  async searchAndBookFlight(input: PaymentLatencyInput["search"], flightLabel: string) {
    this.log.info("Step: Search and book flight");
    await this.flight.searchFlight(input);
    await this.flight.bookFlight(flightLabel);
  }


  async selectSeatAndContinue(seatDescription: string) {
    this.log.info("Step: Select seat");
    await this.seat.selectSeat(seatDescription);
    await this.seat.continueToPassengerDetails();
  }


 async fillPassengerDetails(
  passenger: Omit<PassengerDetails, "seatLabel">,
  seatLabel: string
) {
  this.log.info("Step: Fill passenger details");

  await this.passengerDetails.verifyLoaded();

  const passengerDetails: PassengerDetails = {
    ...passenger,
    seatLabel,
  };

  await this.passengerDetails.fillDetails(passengerDetails);
  await this.passengerDetails.continueToPayment();
}


  async payForBooking(couponLabel: string, card: CardDetails) {
    this.log.info("Step: Checkout and payment");
    await this.checkout.verifyLoaded();
    await this.checkout.applyCoupon(couponLabel);
    await this.checkout.fillCardDetails(card);
    await this.checkout.pay();
  }

  async gatewayTimeOut()
  {
    this.log.info("get payment getway timeout");
    await this.checkout.displayMesassage();
  }


  async checkPaymentLatency(input: PaymentLatencyInput) {
  await this.login(input.credentials.email, input.credentials.password);

  await this.searchAndBookFlight(
    input.search,
    input.flightLabel
  );

  await this.selectSeatAndContinue(
    input.seatDescription
  );

  const start = Date.now();

  await this.fillPassengerDetails(
    input.passenger,
    input.passengerSeatLabel
  );


  await this.checkout.verifyLoaded();
  const end = Date.now();
  return end - start;

  await this.payForBooking(input.couponLabel,input.card);
  this.gatewayTimeOut();
}
}