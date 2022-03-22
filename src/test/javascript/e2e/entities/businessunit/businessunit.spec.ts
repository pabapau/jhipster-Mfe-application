import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BusinessunitComponentsPage, BusinessunitDeleteDialog, BusinessunitUpdatePage } from './businessunit.page-object';

const expect = chai.expect;

describe('Businessunit e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let businessunitComponentsPage: BusinessunitComponentsPage;
  let businessunitUpdatePage: BusinessunitUpdatePage;
  let businessunitDeleteDialog: BusinessunitDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Businessunits', async () => {
    await navBarPage.goToEntity('businessunit');
    businessunitComponentsPage = new BusinessunitComponentsPage();
    await browser.wait(ec.visibilityOf(businessunitComponentsPage.title), 5000);
    expect(await businessunitComponentsPage.getTitle()).to.eq('jhipsterMfeApplicationApp.businessunit.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(businessunitComponentsPage.entities), ec.visibilityOf(businessunitComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Businessunit page', async () => {
    await businessunitComponentsPage.clickOnCreateButton();
    businessunitUpdatePage = new BusinessunitUpdatePage();
    expect(await businessunitUpdatePage.getPageTitle()).to.eq('jhipsterMfeApplicationApp.businessunit.home.createOrEditLabel');
    await businessunitUpdatePage.cancel();
  });

  it('should create and save Businessunits', async () => {
    const nbButtonsBeforeCreate = await businessunitComponentsPage.countDeleteButtons();

    await businessunitComponentsPage.clickOnCreateButton();

    await promise.all([
      businessunitUpdatePage.setCodeInput('code'),
      businessunitUpdatePage.setNameInput('name'),
      businessunitUpdatePage.setDescriptionInput('description'),
    ]);

    await businessunitUpdatePage.save();
    expect(await businessunitUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await businessunitComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Businessunit', async () => {
    const nbButtonsBeforeDelete = await businessunitComponentsPage.countDeleteButtons();
    await businessunitComponentsPage.clickOnLastDeleteButton();

    businessunitDeleteDialog = new BusinessunitDeleteDialog();
    expect(await businessunitDeleteDialog.getDialogTitle()).to.eq('jhipsterMfeApplicationApp.businessunit.delete.question');
    await businessunitDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(businessunitComponentsPage.title), 5000);

    expect(await businessunitComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
