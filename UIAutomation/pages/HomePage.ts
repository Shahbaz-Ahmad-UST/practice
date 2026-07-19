import { BasePage } from "./BasePage";
import {env} from "../utils/env"

const BASE_URL = env.baseurl+"/";

export class HomePage extends BasePage {
  private readonly loginLink = this.page.getByRole("link", { name: "Log in" });
  private readonly emailInput = this.page.getByRole("textbox", { name: "Email" });
  private readonly passwordInput = this.page.getByRole("textbox", { name: "Password" });
  private readonly signInHeading = this.page.getByRole("heading", { name: "Sign in to TripStack" });
  private readonly signInButton = this.page.getByRole("button", { name: "Sign in" });
    private readonly invalidLoginMessage = this.page.getByText("Invalid email or password.");

  async open() {
    await this.goto(BASE_URL);
    await this.captureScreenshot("home-page-loaded");
  }

  async goToLogin() {
    await this.click(this.loginLink, "Log in link");
    await this.expectVisible(this.signInHeading, "Sign in to TripStack heading");
  }

 
  async login(email: string, password: string) {
    await this.click(this.emailInput, "Email field");
    await this.fill(this.emailInput, email, "Email field");
    await this.click(this.passwordInput, "Password field");
    await this.fill(this.passwordInput, password, "Password field", true);
    await this.captureScreenshot("login-form-filled");
    await this.click(this.signInButton, "Sign in button");
  }


async verifyInvalidLogin() {
  await this.expectVisible(this.invalidLoginMessage,"Invalid login message"
  );

  await this.captureScreenshot("invalid-login");
}
}