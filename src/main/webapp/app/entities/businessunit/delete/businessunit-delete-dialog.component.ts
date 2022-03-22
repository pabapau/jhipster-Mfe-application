import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusinessunit } from '../businessunit.model';
import { BusinessunitService } from '../service/businessunit.service';

@Component({
  templateUrl: './businessunit-delete-dialog.component.html',
})
export class BusinessunitDeleteDialogComponent {
  businessunit?: IBusinessunit;

  constructor(protected businessunitService: BusinessunitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.businessunitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
