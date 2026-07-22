import { BasePage } from "./BasePage";


export class ProductPage extends BasePage {
  private readonly productlist = this.page.getByTestId('product-grid');//
  private readonly product = this.page.getByTestId('product-card-prod-007');
  private readonly addCart = this.page.getByTestId('add-to-cart-prod-007');
  private readonly cartLink = this.page.getByTestId('cart-link');



  async productListVisible() {
    await this.expectVisible(this.productlist);
    await this.captureScreenshot("product-page-loaded");
  }

  async addToCart()
  {
    await this.expectVisible(this.product);
    await this.click(this.addCart,"product is added to cart");
  }

}