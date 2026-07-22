import { Page, TestInfo } from "@playwright/test";
import { AppLogger } from "../src/logger";

import { HomePage } from "../pages/HomePage";
import { CartPage } from "../pages/CartPage";
import { ProductPage } from "../pages/ProductPage";


export class TaxValidationFlow {
  readonly home: HomePage;
  readonly cart: CartPage;
  readonly product: ProductPage;


  constructor(
    private readonly page: Page,
    private readonly log: AppLogger,
    private readonly testInfo: TestInfo,
    private readonly evidence: Record<string, unknown>
  ) {
    this.home = new HomePage(page, log, testInfo, evidence);
    this.cart = new CartPage(page, log, testInfo, evidence);
    this.product = new ProductPage(page, log, testInfo, evidence);
  }

    async gotoHomePage()
    {
          this.log.info("Step: home page ");
          await this.home.openHomePage();
    }

     async gotoClothCategory()
    {
          this.log.info("Step: cloth category ");
          await this.home.clickClothNavCategory();
    }

    async productPageShow()
    {
        this.log.info("step: product page open");
        await this.product.productListVisible();
    }
    async addTocart()
    {
        this.log.info("step: add to cart");
        await this.product.addToCart();
    }
    async gotoCartPage()
    {
        this.log.info("step: go to cart");
        await this.cart.gotoCart();
    }

    async productVisibleinCart()
    {
        await this.cart.productVisible();
    }
    async taxValidationInCart()
    {
        this.log.info("step: tax validation in cart part");
        await this.cart.verifyTax();
    }


  async runTaxValidationFlow() {
   await this.gotoHomePage();
   await this.gotoClothCategory();
   await this.productPageShow();
   await this.addTocart();
   await this.gotoCartPage();
   await this.productVisibleinCart();
   await this.taxValidationInCart();

}
}