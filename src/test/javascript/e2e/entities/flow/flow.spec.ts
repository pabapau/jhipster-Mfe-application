import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  FlowComponentsPage,
  /* FlowDeleteDialog, */
  FlowUpdatePage,
} from './flow.page-object';

const expect = chai.expect;

describe('Flow e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let flowComponentsPage: FlowComponentsPage;
  let flowUpdatePage: FlowUpdatePage;
  /* let flowDeleteDialog: FlowDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Flows', async () => {
    await navBarPage.goToEntity('flow');
    flowComponentsPage = new FlowComponentsPage();
    await browser.wait(ec.visibilityOf(flowComponentsPage.title), 5000);
    expect(await flowComponentsPage.getTitle()).to.eq('jhipsterMfeApplicationApp.flow.home.title');
    await browser.wait(ec.or(ec.visibilityOf(flowComponentsPage.entities), ec.visibilityOf(flowComponentsPage.noResult)), 1000);
  });

  it('should load create Flow page', async () => {
    await flowComponentsPage.clickOnCreateButton();
    flowUpdatePage = new FlowUpdatePage();
    expect(await flowUpdatePage.getPageTitle()).to.eq('jhipsterMfeApplicationApp.flow.home.createOrEditLabel');
    await flowUpdatePage.cancel();
  });

  /* it('should create and save Flows', async () => {
        const nbButtonsBeforeCreate = await flowComponentsPage.countDeleteButtons();

        await flowComponentsPage.clickOnCreateButton();

        await promise.all([
            flowUpdatePage.setFileIdentInput('fileIdent'),
            flowUpdatePage.flowusecaseSelectLastOption(),
            flowUpdatePage.setDescriptionInput('description'),
            flowUpdatePage.setCreationdateInput('2000-12-31'),
            flowUpdatePage.setLastupdatedInput('2000-12-31'),
            flowUpdatePage.buildstateSelectLastOption(),
            flowUpdatePage.setBuildcountInput('5'),
            flowUpdatePage.setBuildcommentInput('buildcomment'),
            flowUpdatePage.businessunitSelectLastOption(),
            flowUpdatePage.originSelectLastOption(),
            flowUpdatePage.destinationSelectLastOption(),
        ]);

        await flowUpdatePage.save();
        expect(await flowUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await flowComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Flow', async () => {
        const nbButtonsBeforeDelete = await flowComponentsPage.countDeleteButtons();
        await flowComponentsPage.clickOnLastDeleteButton();

        flowDeleteDialog = new FlowDeleteDialog();
        expect(await flowDeleteDialog.getDialogTitle())
            .to.eq('jhipsterMfeApplicationApp.flow.delete.question');
        await flowDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(flowComponentsPage.title), 5000);

        expect(await flowComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
