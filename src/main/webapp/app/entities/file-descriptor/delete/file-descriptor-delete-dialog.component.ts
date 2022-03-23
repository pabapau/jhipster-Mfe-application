import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileDescriptor } from '../file-descriptor.model';
import { FileDescriptorService } from '../service/file-descriptor.service';

@Component({
  templateUrl: './file-descriptor-delete-dialog.component.html',
})
export class FileDescriptorDeleteDialogComponent {
  fileDescriptor?: IFileDescriptor;

  constructor(protected fileDescriptorService: FileDescriptorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileDescriptorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
