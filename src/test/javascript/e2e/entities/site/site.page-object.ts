import { element, by, ElementFinder } from 'protractor';

export class SiteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-site div table .btn-danger'));
  title = element.all(by.css('jhi-site div h2#page-heading span')).first();
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

export class SiteUpdatePage {
  pageTitle = element(by.id('jhi-site-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  siteTypeSelect = element(by.id('field_siteType'));
  osTypeSelect = element(by.id('field_osType'));
  descriptionInput = element(by.id('field_description'));
  creationDateInput = element(by.id('field_creationDate'));
  lastUpdatedInput = element(by.id('field_lastUpdated'));
  buildStateSelect = element(by.id('field_buildState'));
  buildCountInput = element(by.id('field_buildCount'));
  buildCommentInput = element(by.id('field_buildComment'));

  businessUnitSelect = element(by.id('field_businessUnit'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setSiteTypeSelect(siteType: string): Promise<void> {
    await this.siteTypeSelect.sendKeys(siteType);
  }

  async getSiteTypeSelect(): Promise<string> {
    return await this.siteTypeSelect.element(by.css('option:checked')).getText();
  }

  async siteTypeSelectLastOption(): Promise<void> {
    await this.siteTypeSelect.all(by.tagName('option')).last().click();
  }

  async setOsTypeSelect(osType: string): Promise<void> {
    await this.osTypeSelect.sendKeys(osType);
  }

  async getOsTypeSelect(): Promise<string> {
    return await this.osTypeSelect.element(by.css('option:checked')).getText();
  }

  async osTypeSelectLastOption(): Promise<void> {
    await this.osTypeSelect.all(by.tagName('option')).last().click();
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
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

export class SiteDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-site-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-site'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
