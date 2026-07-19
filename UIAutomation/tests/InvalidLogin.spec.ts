import { test } from "../fixtures/evidence";
import {env} from "../utils/env";

test("Invalid Login", async ({ negativeflow }) => {
  await negativeflow.invalidLogin(
    env.credentials.email,
    "dfghjkhgfdgh"
  );
});