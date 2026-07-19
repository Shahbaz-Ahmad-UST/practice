import { test as diagnosticTest, expect } from "./diagnosis";
import { BookingFlow } from "../flow/BookingFlow"
import { NegativeFlow } from "../flow/NegativeFlow";
import {PaymentLatencyFlow} from "../flow/PaymentLatencyFlow"
type Evidence = Record<string, unknown>;

export const test = diagnosticTest.extend<{
  evidence: Evidence;
  flow: BookingFlow;
  negativeflow:NegativeFlow;
  paymentLatencyFlow:PaymentLatencyFlow;
}>({
  evidence: async ({}, use, testInfo) => {
    const evidence: Evidence = {};

    await use(evidence);

    for (const [name, value] of Object.entries(evidence)) {
      if (value === undefined) continue;

      const isText = typeof value === "string";

      await testInfo.attach(`${name}.${isText ? "txt" : "json"}`, {
        body: isText ? value : JSON.stringify(value, null, 2),
        contentType: isText ? "text/plain" : "application/json",
      });
    }
  },


  flow: async ({ page, log, evidence }, use, testInfo) => {
    await use(new BookingFlow(page, log, testInfo, evidence));
  },
  negativeflow: async ({ page, log, evidence }, use, testInfo) => {
    await use(new NegativeFlow(page, log, testInfo, evidence));
  },
  
   paymentLatencyFlow: async ({ page, log, evidence }, use, testInfo) => {
    await use(new PaymentLatencyFlow(page, log, testInfo, evidence));
  },

});

export { expect };