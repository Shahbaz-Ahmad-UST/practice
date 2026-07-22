import { test, expect } from "../fixtures/evidence";

test('payment error validation with error flow ', async ({ errorflow, evidence }) => {
  evidence.taxValdation ="tax shown in cart with 8.5%";
  await errorflow.runErrorValidatioFlow();

  });