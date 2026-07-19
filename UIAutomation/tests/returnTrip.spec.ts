import { test, expect } from "../fixtures/evidence";
import { BookingFlowInput } from "../flow/BookingFlow";
import { env } from "../utils/env";
import { returnTrip } from "../utils/test-data";

test("Book a return flight from CCU to BLR and verify it in My Trips", async ({ flow, evidence }) => {
  const input: BookingFlowInput = {
    credentials: env.credentials,
    passenger: env.passenger,
    card: env.card,
    ...returnTrip,
  };

  await flow.runFullBookingFlow(input);

  expect(evidence.bookingReference).toBeTruthy();
});