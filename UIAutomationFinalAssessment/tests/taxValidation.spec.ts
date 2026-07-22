import { test, expect } from "../fixtures/evidence";

test('tax validation with flow ', async ({ taxflow, evidence }) => {
  evidence.taxValdation ="tax shown in cart with 8.5%";
  await taxflow.runTaxValidationFlow();

  });