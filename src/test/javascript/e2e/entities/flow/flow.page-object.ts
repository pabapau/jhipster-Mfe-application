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
  flowUseCaseSelect = element(by.id('field_flowUseCase'));
  descriptionInput = element(by.id('field_description'));
  creationDateInput = element(by.id('field_creationDate'));
  lastUpdatedInput = element(by.id('field_lastUpdated'));
  buildStateSelect = element(by.id('field_buildState'));
  buildCountInput = element(by.id('field_buildCount'));
  buildCommentInput = element(by.id('field_buildComment'));

  originFileDescriptorSelect = element(by.id('field_originFileDescriptor'));
  destFileDescriptorSelect = element(by.id('field_destFileDescriptor'));
  businessUnitSelect = element(by.id('field_businessUnit'));
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

  async setFlowUseCaseSelect(flowUseCase: string): Promise<void> {
    await this.flowUseCaseSelect.sendKeys(flowUseCase);
  }

  async getFlowUseCaseSelect(): Promise<string> {
    return await this.flowUseCaseSelect.element(by.css('option:checked')).getText();
  }

  async flowUseCaseSelectLastOption(): Promise<void> {
    await this.flowUseCaseSelect.all(by.tagName('option')).last().click();
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

  async originFileDescriptorSelectLastOption(): Promise<void> {
    await this.originFileDescriptorSelect.all(by.tagName('option')).last().click();
  }

  async originFileDescriptorSelectOption(option: string): Promise<void> {
    await this.originFileDescriptorSelect.sendKeys(option);
  }

  getOriginFileDescriptorSelect(): ElementFinder {
    return this.originFileDescriptorSelect;
  }

  async getOriginFileDescriptorSelectedOption(): Promise<string> {
    return await this.originFileDescriptorSelect.element(by.css('option:checked')).getText();
  }

  async destFileDescriptorSelectLastOption(): Promise<void> {
    await this.destFileDescriptorSelect.all(by.tagName('option')).last().click();
  }

  async destFileDescriptorSelectOption(option: string): Promise<void> {
    await this.destFileDescriptorSelect.sendKeys(option);
  }

  getDestFileDescriptorSelect(): ElementFinder {
    return this.destFileDescriptorSelect;
  }

  async getDestFileDescriptorSelectedOption(): Promise<string> {
    return await this.destFileDescriptorSelect.element(by.css('option:checked')).getText();
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
