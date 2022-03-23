import { element, by, ElementFinder } from 'protractor';

export class FileDescriptorComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-file-descriptor div table .btn-danger'));
  title = element.all(by.css('jhi-file-descriptor div h2#page-heading span')).first();
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

export class FileDescriptorUpdatePage {
  pageTitle = element(by.id('jhi-file-descriptor-heading'));
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

  flowSelect = element(by.id('field_flow'));

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

  async flowSelectLastOption(): Promise<void> {
    await this.flowSelect.all(by.tagName('option')).last().click();
  }

  async flowSelectOption(option: string): Promise<void> {
    await this.flowSelect.sendKeys(option);
  }

  getFlowSelect(): ElementFinder {
    return this.flowSelect;
  }

  async getFlowSelectedOption(): Promise<string> {
    return await this.flowSelect.element(by.css('option:checked')).getText();
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

export class FileDescriptorDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fileDescriptor-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fileDescriptor'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
