import {browser, by, element, ElementFinder, ProtractorBrowser} from "protractor";

export class LoginPage {

  navigateTo() {
    return browser.get('authentication/login');
  }

  getUsernameInput(): ElementFinder {
    return element(by.xpath('//*[@id="insider"]/app-login/form/div[1]/input'));
  }

  getPasswordInput(): ElementFinder {
    return element(by.xpath('//*[@id="insider"]/app-login/form/div[2]/input'));
  }

  getLoginButton(): ElementFinder {
    return element(by.xpath('//*[@id="insider"]/app-login/form/button/span'));
  }

}
