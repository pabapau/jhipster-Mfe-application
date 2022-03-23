import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FileDescriptorComponent } from './list/file-descriptor.component';
import { FileDescriptorDetailComponent } from './detail/file-descriptor-detail.component';
import { FileDescriptorUpdateComponent } from './update/file-descriptor-update.component';
import { FileDescriptorDeleteDialogComponent } from './delete/file-descriptor-delete-dialog.component';
import { FileDescriptorRoutingModule } from './route/file-descriptor-routing.module';

@NgModule({
  imports: [SharedModule, FileDescriptorRoutingModule],
  declarations: [
    FileDescriptorComponent,
    FileDescriptorDetailComponent,
    FileDescriptorUpdateComponent,
    FileDescriptorDeleteDialogComponent,
  ],
  entryComponents: [FileDescriptorDeleteDialogComponent],
})
export class FileDescriptorModule {}
