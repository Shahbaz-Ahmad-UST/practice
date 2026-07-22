import { Page, Locator, TestInfo, expect } from "@playwright/test";
import { AppLogger } from "../src/logger";

export abstract class BasePage {
  constructor(
    protected readonly page: Page,
    protected readonly log: AppLogger,
    protected readonly testInfo: TestInfo,
    protected readonly evidence: Record<string, unknown>
  ) {}


  async goto(url: string) {
    this.log.info(`Navigating to ${url}`);
    await this.page.goto(url);
  }

  async click(locator: Locator, description: string) {
    await locator.click();
  }

 
  async fill(locator: Locator, value: string, description: string, mask = false) {
    this.log.info(`Filling: ${description} = "${mask ? "*".repeat(value.length) : value}"`);
    await locator.fill(value);
  }

  async selectOption(locator: Locator, value: string, description: string) {
    this.log.info(`Selecting: ${description} = "${value}"`);
    await locator.selectOption(value);
  }

  async expectVisible(locator: Locator) {
    await expect(locator).toBeVisible();
  }

  async captureScreenshot(name: string) {
    const safeName = name.replace(/\s+/g, "_").toLowerCase();
    const buffer = await this.page.screenshot({ fullPage: true });

    await this.testInfo.attach(`screenshot-${safeName}`, {
      body: buffer,
      contentType: "image/png",
    });

    this.evidence[`screenshot:${safeName}`] = `captured @ ${new Date().toLocaleTimeString("en-GB", { hour12: false })}`;
    this.log.info(`Screenshot captured: ${safeName}`);
  }
}
export{expect};