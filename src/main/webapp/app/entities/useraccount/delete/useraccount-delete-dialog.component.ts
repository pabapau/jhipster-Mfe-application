import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUseraccount } from '../useraccount.model';
import { UseraccountService } from '../service/useraccount.service';

@Component({
  templateUrl: './useraccount-delete-dialog.component.html',
})
export class UseraccountDeleteDialogComponent {
  useraccount?: IUseraccount;

  constructor(protected useraccountService: UseraccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.useraccountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
