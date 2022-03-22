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
  xprottypeSelect = element(by.id('field_xprottype'));
  xroleSelect = element(by.id('field_xrole'));
  commentInput = element(by.id('field_comment'));
  accessAddressInput = element(by.id('field_accessAddress'));
  accessServicePointInput = element(by.id('field_accessServicePoint'));
  creationdateInput = element(by.id('field_creationdate'));
  lastupdatedInput = element(by.id('field_lastupdated'));
  buildstateSelect = element(by.id('field_buildstate'));
  buildcountInput = element(by.id('field_buildcount'));
  buildcommentInput = element(by.id('field_buildcomment'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setXprottypeSelect(xprottype: string): Promise<void> {
    await this.xprottypeSelect.sendKeys(xprottype);
  }

  async getXprottypeSelect(): Promise<string> {
    return await this.xprottypeSelect.element(by.css('option:checked')).getText();
  }

  async xprottypeSelectLastOption(): Promise<void> {
    await this.xprottypeSelect.all(by.tagName('option')).last().click();
  }

  async setXroleSelect(xrole: string): Promise<void> {
    await this.xroleSelect.sendKeys(xrole);
  }

  async getXroleSelect(): Promise<string> {
    return await this.xroleSelect.element(by.css('option:checked')).getText();
  }

  async xroleSelectLastOption(): Promise<void> {
    await this.xroleSelect.all(by.tagName('option')).last().click();
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
