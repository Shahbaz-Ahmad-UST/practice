import { BasePage, expect } from "./BasePage";
import {env } from "../utils/env";
import {details} from "../utils/data"

export class CheckoutPage extends BasePage {
    private readonly checkoutTitle = this.page.getByRole('heading', { name: 'Checkout', exact: true });
    private readonly contactForm = this.page.getByRole('heading', { name: 'Contact Information' });
    private readonly name = this.page.getByTestId('guest-name-input');
     private readonly phone = this.page.getByTestId('guest-phone-input');
     private readonly email = this.page.getByTestId('guest-email-input');
      private readonly street = this.page.getByTestId('shipping-street-input');
       private readonly city = this.page.getByTestId('shipping-city-input');
       private readonly state =this.page.getByTestId('shipping-state-select');
       private readonly zip =  this.page.getByTestId('shipping-zip-input');
       private readonly country = this.page.getByTestId('shipping-country-input');
       private readonly payment = this.page.getByTestId('continue-to-payment-button');
 

    async visibleCheckoutHeading()
    {
        await this.expectVisible(this.checkoutTitle);
         await this.expectVisible(this.contactForm);
    }

  async fillContactAndContinue() {

    await this.click(this.name,"click on name field");
    await this.fill(this.name,env.customer.name,"enter name", false);


    await this.click(this.email,"click on  email");
    await this.fill(this.email,env.customer.email,"enter email", true);

    await this.click(this.phone,"click on  phone");
    await this.fill(this.phone,env.customer.phone,"enter phone number", true);

     await this.click(this.street,"click on street field");
    await this.fill(this.street,details.street,"enter  street", false);

      await this.click(this.city,"click on city field");
    await this.fill(this.city,details.city,"enter city ", false);

    await this.selectOption(this.state,details.state,"select state");

     await this.click(this.zip,"click on zip field");
    await this.fill(this.zip,details.zip,"enter zip ", false);

     await this.click(this.country,"click on coutry field");
    await this.fill(this.country,details.country,"enter country ", false);

    await this.expectVisible(this.payment);
    await this.click(this.payment,"payment button click");
  }
}
