import { BasePage } from "./BasePage";

export class TicketPage extends BasePage {
  private readonly successHeading = this.page.getByRole("heading", { name: "You're all set!" });
  private readonly amountPaidText = this.page.getByText("Amount paid₹");
  private readonly viewMyTripsButton = this.page.getByRole("button", { name: "View my trips" });

  private bookingRefText(prefix: string) {
    return this.page.getByText(new RegExp(`^${prefix}\\d+$`));
  }

  async verifyConfirmation(bookingRefPrefix: string) {
    await this.expectVisible(this.successHeading, "Booking success heading");
    await this.expectVisible(this.bookingRefText(bookingRefPrefix), `Booking reference starting with ${bookingRefPrefix}`);
    await this.expectVisible(this.amountPaidText, "Amount paid text");
    await this.expectVisible(this.viewMyTripsButton, "View my trips button");

    // Pull the actual booking ref text into evidence for the report.
    const refText = await this.bookingRefText(bookingRefPrefix).textContent();
    this.record("bookingReference", refText?.trim());

    await this.captureScreenshot("booking-confirmation");
  }

  async goToMyTrips() {
    await this.click(this.viewMyTripsButton, "View my trips button");
  }
}