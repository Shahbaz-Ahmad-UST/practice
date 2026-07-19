import { BasePage } from "./BasePage";

export interface CardDetails {
  nameOnCard: string;
  cardNumber: string;
  expiry: string;
  cvv: string;
}

export class CheckoutPageWithPayment extends BasePage {
  private readonly heading = this.page.getByRole("heading", { name: "Secure checkout" });
  private readonly couponText = (label: string) => this.page.getByText(label);
  private readonly nameOnCardInput = this.page.getByRole("textbox", { name: "Name on card" });
  private readonly cardNumberInput = this.page.getByRole("textbox", { name: "Card number" });
  private readonly expiryInput = this.page.getByRole("textbox", { name: "Expiry" });
  private readonly cvvInput = this.page.getByRole("textbox", { name: "CVV" });
  private readonly totalPayableText = this.page.getByText("Total payable₹");
  private readonly payButton = this.page.getByRole("button", { name: "Pay ₹" });
  private readonly gatewayTimeout=this.page.locator("//p[@role='alert']");

  async verifyLoaded() {
    await this.expectVisible(this.heading, "Secure checkout heading");
  }

  async applyCoupon(couponLabel: string) {
    await this.click(this.couponText(couponLabel), `Coupon: ${couponLabel}`);
  }

  async fillCardDetails(card: CardDetails) {
    await this.click(this.nameOnCardInput, "Name on card");
    await this.fill(this.nameOnCardInput, card.nameOnCard, "Name on card");

    await this.click(this.cardNumberInput, "Card number");
    await this.fill(this.cardNumberInput, card.cardNumber, "Card number", true);

    await this.click(this.expiryInput, "Expiry");
    await this.fill(this.expiryInput, card.expiry, "Expiry");

    await this.click(this.cvvInput, "CVV");
    await this.fill(this.cvvInput, card.cvv, "CVV", true);

    await this.expectVisible(this.totalPayableText, "Total payable amount");
    await this.captureScreenshot("checkout-payment-filled");
  }

  async pay() {
    await this.click(this.payButton, "Pay button");
  }

   async displayMesassage()
    {
        await this.expectVisible(this.gatewayTimeout, "Secure checkout heading");
    }
}