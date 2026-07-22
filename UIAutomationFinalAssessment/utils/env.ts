import * as dotenv from "dotenv";
import * as path from "path";

dotenv.config({ path: path.resolve(__dirname, "../.env") });

function required(key: string): string {
  const value = process.env[key];
  if (!value) {
    throw new Error(`Missing required environment variable: ${key}`);
  }
  return value;
}

export const env = {
  baseurl: required("BASEURL"),

  customer: {
    email: required("CUSTOMER_EMAIL"),
    name: required("CUSTOMER_NAME"),
    phone: required("CUSTOMER_PHONE"),
  },

  card: {
    nameOnCard: required("CARD_NAME_ON_CARD"),
    cardNumber: required("CARD_NUMBER"),
    expiry: required("CARD_EXPIRY"),
    cvv: required("CARD_CVV"),
  },
};