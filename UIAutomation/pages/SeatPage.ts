import { expect } from "@playwright/test";
import { BasePage } from "./BasePage";

export class SeatPage extends BasePage {
  private readonly continueButton = this.page.locator(
    "//button[text()='Continue to passenger details']"
  );

  private seatLocator(seatDescription: string) {
    return this.page.getByLabel(seatDescription);
  }

  async selectSeat(seatDescription: string) {
    await this.click(this.seatLocator(seatDescription), `Seat: ${seatDescription}`);
    await this.page.waitForLoadState('domcontentloaded');
    await this.captureScreenshot("seat-selected");
  }

  async continueToPassengerDetails() {
    await expect(this.continueButton).toBeEnabled({
  timeout: 30000,
});
    await this.click(this.continueButton, "Continue to passenger details button");
  }

  async verifySeatNotAvailable(seatDescription: string) {
  await expect(
    this.page.getByLabel(seatDescription)
  ).toHaveCount(0);

  await this.captureScreenshot("seat-not-available");
}
}