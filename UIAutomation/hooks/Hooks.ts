import { test } from "../fixtures/evidence";

test.beforeAll(async () => {

    console.log("========== PLAYWRIGHT EXECUTION STARTED ==========");

});

test.beforeEach(async ({ page }, testInfo) => {

    console.log(`Starting Test : ${testInfo.title}`);

    await page.setViewportSize({
        width: 1536,
        height: 864
    });

});

test.afterEach(async ({ page }, testInfo) => {

    console.log(`Finished Test : ${testInfo.title}`);
    console.log(`Status : ${testInfo.status}`);

});

test.afterAll(async () => {

    console.log("========== PLAYWRIGHT EXECUTION COMPLETED ==========");

});