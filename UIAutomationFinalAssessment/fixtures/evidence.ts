import { test as diagnosticTest, expect } from "./diagnosis";
import { TaxValidationFlow } from "../flow/TaxValidationFlow";
import { ErrorFlow } from "../flow/ErrorFlow";
type Evidence = Record<string, unknown>;

export const test = diagnosticTest.extend<{
  evidence: Evidence;
  taxflow: TaxValidationFlow;
  errorflow :ErrorFlow;

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


  taxflow: async ({ page, log, evidence }, use, testInfo) => {
    await use(new TaxValidationFlow(page, log, testInfo, evidence));
  },


   
  errorflow: async ({ page, log, evidence }, use, testInfo) => {
    await use(new ErrorFlow(page, log, testInfo, evidence));
  },
});

export { expect };