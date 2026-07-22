import { BasePage,expect } from "./BasePage";


export class CartPage extends BasePage {

  private readonly cartLogo=this.page.getByTestId('cart-link');
  private readonly product = this.page.getByTestId('cart-item-prod-007');
  private readonly productPrice = this.page.getByTestId('cart-item-price-prod-007');
  private readonly ordersummary = this.page.getByTestId('order-summary');
  private readonly subTotal = this.page.getByTestId('cart-subtotal');
  private readonly tax = this.page.getByTestId('cart-tax');
  private readonly shipping = this.page.getByText('Shipping$');
   private readonly total = this.page.getByTestId('cart-total');
    private readonly checkout = this.page.getByTestId('checkout-button')




 async gotoCart()
 {
    await this.click(this.cartLogo,"click on cart Logo");
 }

  async productVisible() {
    await this.expectVisible(this.product);
    await this.captureScreenshot("product-page-loaded");
  }

  async verifyTax()
  {
    await this.expectVisible(this.productPrice);
    await this.expectVisible(this.ordersummary);
    await this.expectVisible(this.subTotal);
    await this.expectVisible(this.tax);
    await this.expectVisible(this.shipping);
    await this.expectVisible(this.total);

    const productActualPrice = parseFloat((await this.productPrice.innerText()).replace(/[^0-9.-]+/g, ''));
    const displayedTaxText = await this.tax.innerText();
    const displayedTax = parseFloat(displayedTaxText.replace(/[^0-9.-]+/g, ''));
    const calculatedTax = (productActualPrice * 8.5) / 100;
    const expectedTaxText = `$${calculatedTax.toFixed(2)}`;

    await expect(this.tax).toHaveText(expectedTaxText);
  }

  async checkoutButtonClick()
  {
   await this.expectVisible(this.checkout);
   await this.click(this.checkout,"click checkout");
  }


}