import { BasePage } from "./BasePage";

export class FlightPage extends BasePage {
  private readonly fromCombo = this.page.getByRole("combobox", { name: "From" });
  private readonly toCombo = this.page.getByRole("combobox", { name: "To" });
  private readonly dateInput = this.page.getByRole("textbox", { name: "Date" });
  private readonly searchButton = this.page.getByRole("button", { name: "Search" });

  private fromOption(label: string) {
    return this.page.getByRole("option", { name: label });
  }

  async searchFlight(opts: { from: string; fromOptionLabel: string; to: string; toOptionLabel: string; date: string }) {
    await this.click(this.fromCombo, "From combobox");
    await this.fill(this.fromCombo, opts.from, "From combobox");
    await this.click(this.fromOption(opts.fromOptionLabel), `From option: ${opts.fromOptionLabel}`);

    await this.click(this.toCombo, "To combobox");
    await this.fill(this.toCombo, opts.to, "To combobox");
    await this.click(this.fromOption(opts.toOptionLabel), `To option: ${opts.toOptionLabel}`);

    await this.fill(this.dateInput, opts.date, "Travel date");
    await this.captureScreenshot("flight-search-criteria");

    await this.click(this.searchButton, "Search button");
    await this.captureScreenshot("flight-search-results");
  }

  async bookFlight(flightLabel: string) {
    await this.expectVisible(this.page.getByText(flightLabel), `Flight result: ${flightLabel}`);
    const bookButton = this.page.getByLabel(flightLabel).getByRole("button", { name: "Book" });
    await this.click(bookButton, `Book button for ${flightLabel}`);
  }
}