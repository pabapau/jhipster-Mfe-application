import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  UseraccountComponentsPage,
  /* UseraccountDeleteDialog, */
  UseraccountUpdatePage,
} from './useraccount.page-object';

const expect = chai.expect;

describe('Useraccount e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let useraccountComponentsPage: UseraccountComponentsPage;
  let useraccountUpdatePage: UseraccountUpdatePage;
  /* let useraccountDeleteDialog: UseraccountDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Useraccounts', async () => {
    await navBarPage.goToEntity('useraccount');
    useraccountComponentsPage = new UseraccountComponentsPage();
    await browser.wait(ec.visibilityOf(useraccountComponentsPage.title), 5000);
    expect(await useraccountComponentsPage.getTitle()).to.eq('jhipsterMfeApplicationApp.useraccount.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(useraccountComponentsPage.entities), ec.visibilityOf(useraccountComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Useraccount page', async () => {
    await useraccountComponentsPage.clickOnCreateButton();
    useraccountUpdatePage = new UseraccountUpdatePage();
    expect(await useraccountUpdatePage.getPageTitle()).to.eq('jhipsterMfeApplicationApp.useraccount.home.createOrEditLabel');
    await useraccountUpdatePage.cancel();
  });

  /* it('should create and save Useraccounts', async () => {
        const nbButtonsBeforeCreate = await useraccountComponentsPage.countDeleteButtons();

        await useraccountComponentsPage.clickOnCreateButton();

        await promise.all([
            useraccountUpdatePage.accounttypeSelectLastOption(),
            useraccountUpdatePage.setCommentInput('comment'),
            useraccountUpdatePage.setCreationdateInput('2000-12-31'),
            useraccountUpdatePage.setLastupdatedInput('2000-12-31'),
            useraccountUpdatePage.userSelectLastOption(),
            // useraccountUpdatePage.businessunitSelectLastOption(),
            // useraccountUpdatePage.sitesSelectLastOption(),
        ]);

        await useraccountUpdatePage.save();
        expect(await useraccountUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await useraccountComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Useraccount', async () => {
        const nbButtonsBeforeDelete = await useraccountComponentsPage.countDeleteButtons();
        await useraccountComponentsPage.clickOnLastDeleteButton();

        useraccountDeleteDialog = new UseraccountDeleteDialog();
        expect(await useraccountDeleteDialog.getDialogTitle())
            .to.eq('jhipsterMfeApplicationApp.useraccount.delete.question');
        await useraccountDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(useraccountComponentsPage.title), 5000);

        expect(await useraccountComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
