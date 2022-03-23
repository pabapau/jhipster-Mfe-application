import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { XProtComponentsPage, XProtDeleteDialog, XProtUpdatePage } from './x-prot.page-object';

const expect = chai.expect;

describe('XProt e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let xProtComponentsPage: XProtComponentsPage;
  let xProtUpdatePage: XProtUpdatePage;
  let xProtDeleteDialog: XProtDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load XProts', async () => {
    await navBarPage.goToEntity('x-prot');
    xProtComponentsPage = new XProtComponentsPage();
    await browser.wait(ec.visibilityOf(xProtComponentsPage.title), 5000);
    expect(await xProtComponentsPage.getTitle()).to.eq('jhipsterMfeApplicationApp.xProt.home.title');
    await browser.wait(ec.or(ec.visibilityOf(xProtComponentsPage.entities), ec.visibilityOf(xProtComponentsPage.noResult)), 1000);
  });

  it('should load create XProt page', async () => {
    await xProtComponentsPage.clickOnCreateButton();
    xProtUpdatePage = new XProtUpdatePage();
    expect(await xProtUpdatePage.getPageTitle()).to.eq('jhipsterMfeApplicationApp.xProt.home.createOrEditLabel');
    await xProtUpdatePage.cancel();
  });

  it('should create and save XProts', async () => {
    const nbButtonsBeforeCreate = await xProtComponentsPage.countDeleteButtons();

    await xProtComponentsPage.clickOnCreateButton();

    await promise.all([
      xProtUpdatePage.xprotTypeSelectLastOption(),
      xProtUpdatePage.xRoleSelectLastOption(),
      xProtUpdatePage.setCommentInput('comment'),
      xProtUpdatePage.setAccessAddressInput('accessAddress'),
      xProtUpdatePage.setAccessServicePointInput('5'),
      xProtUpdatePage.setCreationDateInput('2000-12-31'),
      xProtUpdatePage.setLastUpdatedInput('2000-12-31'),
      xProtUpdatePage.buildStateSelectLastOption(),
      xProtUpdatePage.setBuildCountInput('5'),
      xProtUpdatePage.setBuildCommentInput('buildComment'),
      xProtUpdatePage.onNodeSelectLastOption(),
    ]);

    await xProtUpdatePage.save();
    expect(await xProtUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await xProtComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last XProt', async () => {
    const nbButtonsBeforeDelete = await xProtComponentsPage.countDeleteButtons();
    await xProtComponentsPage.clickOnLastDeleteButton();

    xProtDeleteDialog = new XProtDeleteDialog();
    expect(await xProtDeleteDialog.getDialogTitle()).to.eq('jhipsterMfeApplicationApp.xProt.delete.question');
    await xProtDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(xProtComponentsPage.title), 5000);

    expect(await xProtComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
