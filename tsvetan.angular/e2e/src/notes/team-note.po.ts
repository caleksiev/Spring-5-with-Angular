import {browser, by, element} from "protractor";

export class TeamNotePage {

  navigateTo() {
    return browser.get('/homepage/team/notes');
  }

  getLastCreatedNoteTitle() {
    return element(by.xpath('/html/body/app-root/app-dashboard/p/app-notes/ul[last()]/app-note/li[@id="noteTitle"]')).getText();
  }

  getLastNoteShowDetailsButton() {
    return element(by.xpath('/html/body/app-root/app-dashboard/p/app-notes/ul[2]/app-note/button[@id="showButton"]'));
  }

  getLastCreatedNoteDescription() {
    return element(by.xpath('//*[@id="noteDescription"]')).getText();
  }

  getLastCreatedNoteCreatedDate() {
    return element(by.xpath('/html/body/app-root/app-dashboard/p/app-notes/ul[last()]/app-note/ol[1]/li[@id="noteCreated"]')).getText();
  }

  getLastCreatedNoteDeadline() {
    return element(by.xpath('/html/body/app-root/app-dashboard/p/app-notes/ul[last()]/app-note/ol[1]/li[@id="noteDeadline"]')).getText();
  }

  getLastCreatedNoteCreator() {
    return element(by.xpath('/html/body/app-root/app-dashboard/p/app-notes/ul[last()]/app-note/ol[1]/li[@id="noteCreator"]')).getText();
  }

  getLastNoteRemoveButton() {

    return element(by.xpath('/html/body/app-root/app-dashboard/p/app-notes/ul[2]/app-note/button[@id="removeButton"]'));
  }
}
