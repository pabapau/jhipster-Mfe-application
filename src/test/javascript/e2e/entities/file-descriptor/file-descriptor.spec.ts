import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FileDescriptorComponentsPage, FileDescriptorDeleteDialog, FileDescriptorUpdatePage } from './file-descriptor.page-object';

const expect = chai.expect;

describe('FileDescriptor e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fileDescriptorComponentsPage: FileDescriptorComponentsPage;
  let fileDescriptorUpdatePage: FileDescriptorUpdatePage;
  let fileDescriptorDeleteDialog: FileDescriptorDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FileDescriptors', async () => {
    await navBarPage.goToEntity('file-descriptor');
    fileDescriptorComponentsPage = new FileDescriptorComponentsPage();
    await browser.wait(ec.visibilityOf(fileDescriptorComponentsPage.title), 5000);
    expect(await fileDescriptorComponentsPage.getTitle()).to.eq('jhipsterMfeApplicationApp.fileDescriptor.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(fileDescriptorComponentsPage.entities), ec.visibilityOf(fileDescriptorComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FileDescriptor page', async () => {
    await fileDescriptorComponentsPage.clickOnCreateButton();
    fileDescriptorUpdatePage = new FileDescriptorUpdatePage();
    expect(await fileDescriptorUpdatePage.getPageTitle()).to.eq('jhipsterMfeApplicationApp.fileDescriptor.home.createOrEditLabel');
    await fileDescriptorUpdatePage.cancel();
  });

  it('should create and save FileDescriptors', async () => {
    const nbButtonsBeforeCreate = await fileDescriptorComponentsPage.countDeleteButtons();

    await fileDescriptorComponentsPage.clickOnCreateButton();

    await promise.all([
      fileDescriptorUpdatePage.setFileIdentInput('fileIdent'),
      fileDescriptorUpdatePage.flowUseCaseSelectLastOption(),
      fileDescriptorUpdatePage.setDescriptionInput('description'),
      fileDescriptorUpdatePage.setCreationDateInput('2000-12-31'),
      fileDescriptorUpdatePage.setLastUpdatedInput('2000-12-31'),
      fileDescriptorUpdatePage.buildStateSelectLastOption(),
      fileDescriptorUpdatePage.setBuildCountInput('5'),
      fileDescriptorUpdatePage.setBuildCommentInput('buildComment'),
      fileDescriptorUpdatePage.flowSelectLastOption(),
    ]);

    await fileDescriptorUpdatePage.save();
    expect(await fileDescriptorUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fileDescriptorComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FileDescriptor', async () => {
    const nbButtonsBeforeDelete = await fileDescriptorComponentsPage.countDeleteButtons();
    await fileDescriptorComponentsPage.clickOnLastDeleteButton();

    fileDescriptorDeleteDialog = new FileDescriptorDeleteDialog();
    expect(await fileDescriptorDeleteDialog.getDialogTitle()).to.eq('jhipsterMfeApplicationApp.fileDescriptor.delete.question');
    await fileDescriptorDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fileDescriptorComponentsPage.title), 5000);

    expect(await fileDescriptorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
