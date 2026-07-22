

  import { BasePage, expect } from "./BasePage";
  import {env } from "../utils/env";
  
  export class PaymentPage extends BasePage {
      private readonly paymentTitle = this.page.getByRole('heading', { name: 'Payment', exact: true });
      private readonly name = this.page.getByTestId('payment-card-name');
       private readonly cardNumber = this.page.getByTestId('payment-card-number');
       private readonly expiry = this.page.getByTestId('payment-expiry');
        private readonly cvv = this.page.getByTestId('payment-cvv');


         private readonly placedOrder = this.page.getByTestId('place-order-button');
         private readonly error =this.page.getByTestId('payment-general-error');
      
   
  
      async visiblePaymentHeading()
      {
          await this.expectVisible(this.paymentTitle);
        
      }
  
    async fillpaymentDetails() {
  
      await this.click(this.name,"click on name field");
      await this.fill(this.name,env.customer.name,"enter name", true);
  
  
      await this.click(this.cardNumber,"click on  card number");
      await this.fill(this.cardNumber,env.card.cardNumber,"enter card number", true);
  
      await this.click(this.expiry,"click on  expiry");
      await this.fill(this.expiry,env.card.expiry,"enter phone cvv", true);
  
       await this.click(this.cvv,"click on cvv ");
      await this.fill(this.cvv,env.card.cvv,"enter  cvv", true);
    }
  
    async showErrorwhilePlacing()
    {
        this.expectVisible(this.placedOrder);
        this.click(this.placedOrder,"click place order");
         this.expectVisible(this.error);
    }
  }
  
  