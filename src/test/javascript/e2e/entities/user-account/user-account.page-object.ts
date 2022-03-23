import { element, by, ElementFinder } from 'protractor';

export class UserAccountComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-user-account div table .btn-danger'));
  title = element.all(by.css('jhi-user-account div h2#page-heading span')).first();
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

export class UserAccountUpdatePage {
  pageTitle = element(by.id('jhi-user-account-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  accountTypeSelect = element(by.id('field_accountType'));
  commentInput = element(by.id('field_comment'));
  creationDateInput = element(by.id('field_creationDate'));
  lastUpdatedInput = element(by.id('field_lastUpdated'));

  userSelect = element(by.id('field_user'));
  businessUnitSelect = element(by.id('field_businessUnit'));
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

  async setAccountTypeSelect(accountType: string): Promise<void> {
    await this.accountTypeSelect.sendKeys(accountType);
  }

  async getAccountTypeSelect(): Promise<string> {
    return await this.accountTypeSelect.element(by.css('option:checked')).getText();
  }

  async accountTypeSelectLastOption(): Promise<void> {
    await this.accountTypeSelect.all(by.tagName('option')).last().click();
  }

  async setCommentInput(comment: string): Promise<void> {
    await this.commentInput.sendKeys(comment);
  }

  async getCommentInput(): Promise<string> {
    return await this.commentInput.getAttribute('value');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  async setLastUpdatedInput(lastUpdated: string): Promise<void> {
    await this.lastUpdatedInput.sendKeys(lastUpdated);
  }

  async getLastUpdatedInput(): Promise<string> {
    return await this.lastUpdatedInput.getAttribute('value');
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

  async businessUnitSelectLastOption(): Promise<void> {
    await this.businessUnitSelect.all(by.tagName('option')).last().click();
  }

  async businessUnitSelectOption(option: string): Promise<void> {
    await this.businessUnitSelect.sendKeys(option);
  }

  getBusinessUnitSelect(): ElementFinder {
    return this.businessUnitSelect;
  }

  async getBusinessUnitSelectedOption(): Promise<string> {
    return await this.businessUnitSelect.element(by.css('option:checked')).getText();
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

export class UserAccountDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-userAccount-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-userAccount'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
