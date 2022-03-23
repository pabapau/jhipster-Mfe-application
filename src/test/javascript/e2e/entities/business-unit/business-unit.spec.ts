import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BusinessUnitComponentsPage, BusinessUnitDeleteDialog, BusinessUnitUpdatePage } from './business-unit.page-object';

const expect = chai.expect;

describe('BusinessUnit e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let businessUnitComponentsPage: BusinessUnitComponentsPage;
  let businessUnitUpdatePage: BusinessUnitUpdatePage;
  let businessUnitDeleteDialog: BusinessUnitDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load BusinessUnits', async () => {
    await navBarPage.goToEntity('business-unit');
    businessUnitComponentsPage = new BusinessUnitComponentsPage();
    await browser.wait(ec.visibilityOf(businessUnitComponentsPage.title), 5000);
    expect(await businessUnitComponentsPage.getTitle()).to.eq('jhipsterMfeApplicationApp.businessUnit.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(businessUnitComponentsPage.entities), ec.visibilityOf(businessUnitComponentsPage.noResult)),
      1000
    );
  });

  it('should load create BusinessUnit page', async () => {
    await businessUnitComponentsPage.clickOnCreateButton();
    businessUnitUpdatePage = new BusinessUnitUpdatePage();
    expect(await businessUnitUpdatePage.getPageTitle()).to.eq('jhipsterMfeApplicationApp.businessUnit.home.createOrEditLabel');
    await businessUnitUpdatePage.cancel();
  });

  it('should create and save BusinessUnits', async () => {
    const nbButtonsBeforeCreate = await businessUnitComponentsPage.countDeleteButtons();

    await businessUnitComponentsPage.clickOnCreateButton();

    await promise.all([
      businessUnitUpdatePage.setCodeInput('code'),
      businessUnitUpdatePage.setNameInput('name'),
      businessUnitUpdatePage.setDescriptionInput('description'),
    ]);

    await businessUnitUpdatePage.save();
    expect(await businessUnitUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await businessUnitComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last BusinessUnit', async () => {
    const nbButtonsBeforeDelete = await businessUnitComponentsPage.countDeleteButtons();
    await businessUnitComponentsPage.clickOnLastDeleteButton();

    businessUnitDeleteDialog = new BusinessUnitDeleteDialog();
    expect(await businessUnitDeleteDialog.getDialogTitle()).to.eq('jhipsterMfeApplicationApp.businessUnit.delete.question');
    await businessUnitDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(businessUnitComponentsPage.title), 5000);

    expect(await businessUnitComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
