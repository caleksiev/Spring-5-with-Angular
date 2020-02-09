import {browser, by, element} from "protractor";

export class CreateNotePage {

  navigateTo() {
    return browser.get('/homepage/create/notes');
  }

  getNoteTitleInput() {
    return element(by.xpath('//*[@id="noteTitle"]/input'));
  }

  getNoteDescriptionInput() {
    return element(by.xpath('//*[@id="noteDescription"]/input'));
  }

  getNoteDeadlineInput() {
    return element(by.xpath('//*[@id="noteDeadline"]/input'));
  }

  getNoteVisibilityTeamInput() {
    return element(by.xpath('//*[@id="team"]'));
  }

  getNoteVisibilityCompanyInput() {
    return element(by.xpath('//*[@id="company"]'));
  }

  getCreateButton(){
    return  element(by.xpath('/html/body/app-root/app-dashboard/p/app-create-notes/form/button'));
  }
  getResultFromCreateParagraph() {
    return element(by.css('p'));
  }

}
