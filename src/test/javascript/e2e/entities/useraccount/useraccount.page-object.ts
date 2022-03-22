import { element, by, ElementFinder } from 'protractor';

export class UseraccountComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-useraccount div table .btn-danger'));
  title = element.all(by.css('jhi-useraccount div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class UseraccountUpdatePage {
  pageTitle = element(by.id('jhi-useraccount-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  accounttypeSelect = element(by.id('field_accounttype'));
  commentInput = element(by.id('field_comment'));
  creationdateInput = element(by.id('field_creationdate'));
  lastupdatedInput = element(by.id('field_lastupdated'));

  userSelect = element(by.id('field_user'));
  businessunitSelect = element(by.id('field_businessunit'));
  sitesSelect = element(by.id('field_sites'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAccounttypeSelect(accounttype: string): Promise<void> {
    await this.accounttypeSelect.sendKeys(accounttype);
  }

  async getAccounttypeSelect(): Promise<string> {
    return await this.accounttypeSelect.element(by.css('option:checked')).getText();
  }

  async accounttypeSelectLastOption(): Promise<void> {
    await this.accounttypeSelect.all(by.tagName('option')).last().click();
  }

  async setCommentInput(comment: string): Promise<void> {
    await this.commentInput.sendKeys(comment);
  }

  async getCommentInput(): Promise<string> {
    return await this.commentInput.getAttribute('value');
  }

  async setCreationdateInput(creationdate: string): Promise<void> {
    await this.creationdateInput.sendKeys(creationdate);
  }

  async getCreationdateInput(): Promise<string> {
    return await this.creationdateInput.getAttribute('value');
  }

  async setLastupdatedInput(lastupdated: string): Promise<void> {
    await this.lastupdatedInput.sendKeys(lastupdated);
  }

  async getLastupdatedInput(): Promise<string> {
    return await this.lastupdatedInput.getAttribute('value');
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect.all(by.tagName('option')).last().click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async businessunitSelectLastOption(): Promise<void> {
    await this.businessunitSelect.all(by.tagName('option')).last().click();
  }

  async businessunitSelectOption(option: string): Promise<void> {
    await this.businessunitSelect.sendKeys(option);
  }

  getBusinessunitSelect(): ElementFinder {
    return this.businessunitSelect;
  }

  async getBusinessunitSelectedOption(): Promise<string> {
    return await this.businessunitSelect.element(by.css('option:checked')).getText();
  }

  async sitesSelectLastOption(): Promise<void> {
    await this.sitesSelect.all(by.tagName('option')).last().click();
  }

  async sitesSelectOption(option: string): Promise<void> {
    await this.sitesSelect.sendKeys(option);
  }

  getSitesSelect(): ElementFinder {
    return this.sitesSelect;
  }

  async getSitesSelectedOption(): Promise<string> {
    return await this.sitesSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class UseraccountDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-useraccount-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-useraccount'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
