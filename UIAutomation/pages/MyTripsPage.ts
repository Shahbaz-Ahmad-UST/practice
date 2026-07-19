import { BasePage } from "./BasePage";

export class MyTripsPage extends BasePage {
  private tripSummaryText(text: string) {
    return this.page.getByText(text);
  }

 async verifyTripPresent(bookingReference: string, seat: string, amount: string) {
  this.log.info(`Verifying trip present: ${bookingReference}`);
  const tripText = this.page.getByText(
    `${bookingReference} CONFIRMED flight · seats ${seat} ${amount} Cancel`
  );
  await this.expectVisible(tripText, `Trip summary: ${bookingReference}`);
}
}