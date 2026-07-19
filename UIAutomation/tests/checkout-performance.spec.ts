import { test, expect } from "../fixtures/evidence";
import { BookingFlowInput } from "../flow/BookingFlow";
import { env } from "../utils/env";
import { performance_data } from "../utils/test-data";

test("Checkout page performance", async ({ flow }) => {
  const input: BookingFlowInput = {
    credentials: env.credentials,
    passenger: env.passenger,
    card: env.card,
    ...performance_data,
  };

  const checkoutLoadTime = await flow.measureCheckoutPerformance(input);

  console.log(`Checkout page loaded in ${checkoutLoadTime} ms`);

  expect(checkoutLoadTime).toBeLessThan(4000);// checkout return after 3S
});