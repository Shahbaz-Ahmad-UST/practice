import { BasePage } from "./BasePage";
import {env} from "../utils/env"

const BASE_URL = env.baseurl+"/";

export class HomePage extends BasePage {
  private readonly loginLink = this.page.getByTestId('logo-link');
  private readonly clothCategory = this.page.getByTestId('category-nav-clothing');


  async openHomePage() {
    await this.goto(BASE_URL);
    await this.captureScreenshot("home-page-loaded");
  }

  async clickClothNavCategory()
  {
    await this.expectVisible(this.clothCategory);
    await this.click(this.clothCategory,"click on cloth category");
  }

}