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

  credentials: {
    email: required("CUSTOMER_EMAIL"),
    password: required("CUSTOMER_PASSWORD"),
  },

  passenger: {
    firstName: required("PASSENGER_FIRST_NAME"),
    lastName: required("PASSENGER_LAST_NAME"),
    age: required("PASSENGER_AGE"),
    gender: required("PASSENGER_GENDER"),
    email: required("PASSENGER_EMAIL"),
    phone: required("PASSENGER_PHONE"),
  },

  card: {
    nameOnCard: required("CARD_NAME_ON_CARD"),
    cardNumber: required("CARD_NUMBER"),
    expiry: required("CARD_EXPIRY"),
    cvv: required("CARD_CVV"),
  },
};