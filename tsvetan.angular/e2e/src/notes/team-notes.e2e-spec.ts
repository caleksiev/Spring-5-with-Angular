import {CreateNotePage} from "./create-note.po";
import {LoginPage} from "../auth/login/login.po";
import {browser} from "protractor";
import {TeamNotePage} from "./team-note.po";
import {User} from "../../../src/app/authentication/register/user.interface";
import {Note} from "../../../src/app/homepage/notes/notes.interface";


describe(('team-notes page, login-page, create-notes-page'), () => {

  let createNotesPage: CreateNotePage;
  let loginPage: LoginPage;
  let teamNotePage: TeamNotePage;

  let testUser: User = {
    username: 'ivan',
    password: '1234'
  };

  let testNote: Note = {
    title: 'Some title...',
    description: 'Some description...',
    deadline: new Date('2020-06-01'),
    user: testUser
  };

  beforeEach(() => {
    createNotesPage = new CreateNotePage();
    loginPage = new LoginPage();
    teamNotePage = new TeamNotePage();
  });


  it('it should create team notes after login with characteristics as expected', () => {

    //try to navigate to create notes page unauthorized
    createNotesPage.navigateTo().then(() => {

      //login to the page
      loginPage.getUsernameInput().sendKeys(testUser.username);
      loginPage.getPasswordInput().sendKeys(testUser.password);
      loginPage.getLoginButton().click();
      browser.sleep(2000);

      ///navigate to create notes page authorized
      createNotesPage.navigateTo().then(() => {

        //create note
        createNotesPage.getNoteTitleInput().sendKeys(testNote.title);
        createNotesPage.getNoteDescriptionInput().sendKeys(testNote.description);
        createNotesPage.getNoteDeadlineInput().sendKeys(testNote.deadline.toDateString());
        createNotesPage.getNoteVisibilityTeamInput().click();

        createNotesPage.getCreateButton().click();
        expect(createNotesPage.getResultFromCreateParagraph()).toBeTruthy();

        //check note new created note characteristics
        teamNotePage.navigateTo();
        browser.sleep(2000);
        teamNotePage.getLastNoteShowDetailsButton().click();
        expect(teamNotePage.getLastCreatedNoteTitle()).toEqual('Title note: ' + testNote.title);
        expect(teamNotePage.getLastCreatedNoteDescription()).toEqual('Description: ' + testNote.description);
        expect(teamNotePage.getLastCreatedNoteCreator()).toEqual('Creator(username): ' + testUser.username);
        teamNotePage.getLastNoteShowDetailsButton().click();

        //delete test note
        teamNotePage.getLastNoteRemoveButton().click();
        browser.switchTo().alert().accept();
      });
    });
  })
});
