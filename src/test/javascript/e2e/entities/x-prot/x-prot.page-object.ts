import { element, by, ElementFinder } from 'protractor';

export class XProtComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-x-prot div table .btn-danger'));
  title = element.all(by.css('jhi-x-prot div h2#page-heading span')).first();
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

export class XProtUpdatePage {
  pageTitle = element(by.id('jhi-x-prot-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  xprotTypeSelect = element(by.id('field_xprotType'));
  xRoleSelect = element(by.id('field_xRole'));
  commentInput = element(by.id('field_comment'));
  accessAddressInput = element(by.id('field_accessAddress'));
  accessServicePointInput = element(by.id('field_accessServicePoint'));
  creationDateInput = element(by.id('field_creationDate'));
  lastUpdatedInput = element(by.id('field_lastUpdated'));
  buildStateSelect = element(by.id('field_buildState'));
  buildCountInput = element(by.id('field_buildCount'));
  buildCommentInput = element(by.id('field_buildComment'));

  onNodeSelect = element(by.id('field_onNode'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setXprotTypeSelect(xprotType: string): Promise<void> {
    await this.xprotTypeSelect.sendKeys(xprotType);
  }

  async getXprotTypeSelect(): Promise<string> {
    return await this.xprotTypeSelect.element(by.css('option:checked')).getText();
  }

  async xprotTypeSelectLastOption(): Promise<void> {
    await this.xprotTypeSelect.all(by.tagName('option')).last().click();
  }

  async setXRoleSelect(xRole: string): Promise<void> {
    await this.xRoleSelect.sendKeys(xRole);
  }

  async getXRoleSelect(): Promise<string> {
    return await this.xRoleSelect.element(by.css('option:checked')).getText();
  }

  async xRoleSelectLastOption(): Promise<void> {
    await this.xRoleSelect.all(by.tagName('option')).last().click();
  }

  async setCommentInput(comment: string): Promise<void> {
    await this.commentInput.sendKeys(comment);
  }

  async getCommentInput(): Promise<string> {
    return await this.commentInput.getAttribute('value');
  }

  async setAccessAddressInput(accessAddress: string): Promise<void> {
    await this.accessAddressInput.sendKeys(accessAddress);
  }

  async getAccessAddressInput(): Promise<string> {
    return await this.accessAddressInput.getAttribute('value');
  }

  async setAccessServicePointInput(accessServicePoint: string): Promise<void> {
    await this.accessServicePointInput.sendKeys(accessServicePoint);
  }

  async getAccessServicePointInput(): Promise<string> {
    return await this.accessServicePointInput.getAttribute('value');
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

  async setBuildStateSelect(buildState: string): Promise<void> {
    await this.buildStateSelect.sendKeys(buildState);
  }

  async getBuildStateSelect(): Promise<string> {
    return await this.buildStateSelect.element(by.css('option:checked')).getText();
  }

  async buildStateSelectLastOption(): Promise<void> {
    await this.buildStateSelect.all(by.tagName('option')).last().click();
  }

  async setBuildCountInput(buildCount: string): Promise<void> {
    await this.buildCountInput.sendKeys(buildCount);
  }

  async getBuildCountInput(): Promise<string> {
    return await this.buildCountInput.getAttribute('value');
  }

  async setBuildCommentInput(buildComment: string): Promise<void> {
    await this.buildCommentInput.sendKeys(buildComment);
  }

  async getBuildCommentInput(): Promise<string> {
    return await this.buildCommentInput.getAttribute('value');
  }

  async onNodeSelectLastOption(): Promise<void> {
    await this.onNodeSelect.all(by.tagName('option')).last().click();
  }

  async onNodeSelectOption(option: string): Promise<void> {
    await this.onNodeSelect.sendKeys(option);
  }

  getOnNodeSelect(): ElementFinder {
    return this.onNodeSelect;
  }

  async getOnNodeSelectedOption(): Promise<string> {
    return await this.onNodeSelect.element(by.css('option:checked')).getText();
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

export class XProtDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-xProt-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-xProt'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
