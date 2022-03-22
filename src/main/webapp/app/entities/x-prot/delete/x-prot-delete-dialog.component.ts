import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IXProt } from '../x-prot.model';
import { XProtService } from '../service/x-prot.service';

@Component({
  templateUrl: './x-prot-delete-dialog.component.html',
})
export class XProtDeleteDialogComponent {
  xProt?: IXProt;

  constructor(protected xProtService: XProtService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.xProtService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
