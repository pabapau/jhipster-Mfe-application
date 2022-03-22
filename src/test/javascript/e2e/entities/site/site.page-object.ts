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
  sitetypeSelect = element(by.id('field_sitetype'));
  descriptionInput = element(by.id('field_description'));
  sitenodeInput = element(by.id('field_sitenode'));
  creationdateInput = element(by.id('field_creationdate'));
  lastupdatedInput = element(by.id('field_lastupdated'));
  buildstateSelect = element(by.id('field_buildstate'));
  buildcountInput = element(by.id('field_buildcount'));
  buildcommentInput = element(by.id('field_buildcomment'));

  businessunitSelect = element(by.id('field_businessunit'));

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

  async setSitetypeSelect(sitetype: string): Promise<void> {
    await this.sitetypeSelect.sendKeys(sitetype);
  }

  async getSitetypeSelect(): Promise<string> {
    return await this.sitetypeSelect.element(by.css('option:checked')).getText();
  }

  async sitetypeSelectLastOption(): Promise<void> {
    await this.sitetypeSelect.all(by.tagName('option')).last().click();
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setSitenodeInput(sitenode: string): Promise<void> {
    await this.sitenodeInput.sendKeys(sitenode);
  }

  async getSitenodeInput(): Promise<string> {
    return await this.sitenodeInput.getAttribute('value');
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

  async setBuildstateSelect(buildstate: string): Promise<void> {
    await this.buildstateSelect.sendKeys(buildstate);
  }

  async getBuildstateSelect(): Promise<string> {
    return await this.buildstateSelect.element(by.css('option:checked')).getText();
  }

  async buildstateSelectLastOption(): Promise<void> {
    await this.buildstateSelect.all(by.tagName('option')).last().click();
  }

  async setBuildcountInput(buildcount: string): Promise<void> {
    await this.buildcountInput.sendKeys(buildcount);
  }

  async getBuildcountInput(): Promise<string> {
    return await this.buildcountInput.getAttribute('value');
  }

  async setBuildcommentInput(buildcomment: string): Promise<void> {
    await this.buildcommentInput.sendKeys(buildcomment);
  }

  async getBuildcommentInput(): Promise<string> {
    return await this.buildcommentInput.getAttribute('value');
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
