import { BasePage } from "./BasePage";

export interface PassengerDetails {
  seatLabel: string; 
  firstName: string;
  lastName: string;
  age: string;
  gender: string;
  email: string;
  phone: string;
}

export class PassengerDetailPage extends BasePage {
  private readonly heading = this.page.getByRole("heading", { name: "Who's travelling?" });
  private readonly emailInput = this.page.getByRole("textbox", { name: "Email" });
  private readonly phoneInput = this.page.getByRole("textbox", { name: "Phone number" });
  private readonly continueButton = this.page.getByRole("button", { name: "Continue to payment" });

  private firstNameInput(seatLabel: string) {
    return this.page.getByRole("textbox", { name: `First name (seat ${seatLabel})` });
  }

  private lastNameInput(seatLabel: string) {
    return this.page.getByRole("textbox", { name: `Last name (seat ${seatLabel})` });
  }

  private ageInput(seatLabel: string) {
    return this.page.getByRole("spinbutton", { name: `Age (seat ${seatLabel})` });
  }

  private genderSelect(seatLabel: string) {
    return this.page.getByLabel(`Gender (seat ${seatLabel})`);
  }

  async verifyLoaded() {
    await this.expectVisible(this.heading, "Who's travelling? heading");
  }

  async fillDetails(details: PassengerDetails) {
    const firstName = this.firstNameInput(details.seatLabel);
    await this.click(firstName, `First name (seat ${details.seatLabel})`);
    await this.fill(firstName, details.firstName, `First name (seat ${details.seatLabel})`);

    const lastName = this.lastNameInput(details.seatLabel);
    await this.click(lastName, `Last name (seat ${details.seatLabel})`);
    await this.fill(lastName, details.lastName, `Last name (seat ${details.seatLabel})`);

    const age = this.ageInput(details.seatLabel);
    await this.click(age, `Age (seat ${details.seatLabel})`);
    await this.fill(age, details.age, `Age (seat ${details.seatLabel})`);

    await this.selectOption(this.genderSelect(details.seatLabel), details.gender, `Gender (seat ${details.seatLabel})`);

    await this.click(this.emailInput, "Contact email");
    await this.fill(this.emailInput, details.email, "Contact email");

    await this.click(this.phoneInput, "Contact phone number");
    await this.fill(this.phoneInput, details.phone, "Contact phone number");

    await this.captureScreenshot("passenger-details-filled");
  }

  async continueToPayment() {
    await this.click(this.continueButton, "Continue to payment button");
  }
}