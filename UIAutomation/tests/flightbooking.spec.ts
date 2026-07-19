import { test, expect } from "../fixtures/evidence";
import { BookingFlowInput } from "../flow/BookingFlow";
import { env } from "../utils/env";
import { onwardTrip } from "../utils/test-data";

test("Book a flight from BLR to CCU and verify it in My Trips", async ({ flow, evidence }) => {
  const input: BookingFlowInput = {
    credentials: env.credentials,
    passenger: env.passenger,
    card: env.card,
    ...onwardTrip,
  };

  await flow.runFullBookingFlow(input);

  expect(evidence.bookingReference).toBeTruthy();
});