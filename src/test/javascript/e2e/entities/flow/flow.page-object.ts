import { element, by, ElementFinder } from 'protractor';

export class FlowComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-flow div table .btn-danger'));
  title = element.all(by.css('jhi-flow div h2#page-heading span')).first();
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

export class FlowUpdatePage {
  pageTitle = element(by.id('jhi-flow-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  fileIdentInput = element(by.id('field_fileIdent'));
  flowusecaseSelect = element(by.id('field_flowusecase'));
  descriptionInput = element(by.id('field_description'));
  creationdateInput = element(by.id('field_creationdate'));
  lastupdatedInput = element(by.id('field_lastupdated'));
  buildstateSelect = element(by.id('field_buildstate'));
  buildcountInput = element(by.id('field_buildcount'));
  buildcommentInput = element(by.id('field_buildcomment'));

  businessunitSelect = element(by.id('field_businessunit'));
  originSelect = element(by.id('field_origin'));
  destinationSelect = element(by.id('field_destination'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFileIdentInput(fileIdent: string): Promise<void> {
    await this.fileIdentInput.sendKeys(fileIdent);
  }

  async getFileIdentInput(): Promise<string> {
    return await this.fileIdentInput.getAttribute('value');
  }

  async setFlowusecaseSelect(flowusecase: string): Promise<void> {
    await this.flowusecaseSelect.sendKeys(flowusecase);
  }

  async getFlowusecaseSelect(): Promise<string> {
    return await this.flowusecaseSelect.element(by.css('option:checked')).getText();
  }

  async flowusecaseSelectLastOption(): Promise<void> {
    await this.flowusecaseSelect.all(by.tagName('option')).last().click();
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
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

  async originSelectLastOption(): Promise<void> {
    await this.originSelect.all(by.tagName('option')).last().click();
  }

  async originSelectOption(option: string): Promise<void> {
    await this.originSelect.sendKeys(option);
  }

  getOriginSelect(): ElementFinder {
    return this.originSelect;
  }

  async getOriginSelectedOption(): Promise<string> {
    return await this.originSelect.element(by.css('option:checked')).getText();
  }

  async destinationSelectLastOption(): Promise<void> {
    await this.destinationSelect.all(by.tagName('option')).last().click();
  }

  async destinationSelectOption(option: string): Promise<void> {
    await this.destinationSelect.sendKeys(option);
  }

  getDestinationSelect(): ElementFinder {
    return this.destinationSelect;
  }

  async getDestinationSelectedOption(): Promise<string> {
    return await this.destinationSelect.element(by.css('option:checked')).getText();
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

export class FlowDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-flow-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-flow'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
