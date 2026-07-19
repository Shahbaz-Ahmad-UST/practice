
import {Page} from "@playwright/test"
import { BasePage } from "./BasePage"
import { SearchData } from "../test-data/SearchData";
import { config } from "../utils/env";

export class LoginPage extends BasePage{
    constructor(page: Page)
    {super(page)}

    private email = this.page.getByRole("textbox", {name:"Email"});
    private password = this.page.getByRole("textbox", {name:"Password"});
    private loginBtn= this.page.getByRole("button", {name:"Sign in"});
    header = () => this.page.getByRole("heading",{name: "Sign in to TripStack"});

    async open(){
        await this.page.goto(config.baseUrl + "login");
    }

    async login(email: string, password: string){
        await this.email.fill(email);
        await this.password.fill(password);
        await this.loginBtn.click();
    }
    
}