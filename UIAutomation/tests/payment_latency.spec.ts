import { test, expect } from "../fixtures/evidence";
import { PaymentLatencyInput } from "../flow/PaymentLatencyFlow";
import { env } from "../utils/env";
import { payment_latency } from "../utils/test-data";

test("Gateway payment latency", async ({ paymentLatencyFlow, evidence }) => {

  const input: PaymentLatencyInput =
   {
    credentials: env.credentials,
    passenger: env.passenger,
    card: env.card,
    ...payment_latency,
  };

  await paymentLatencyFlow.checkPaymentLatency(input);


});