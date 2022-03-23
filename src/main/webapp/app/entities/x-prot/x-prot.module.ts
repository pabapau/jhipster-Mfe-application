import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { XProtComponent } from './list/x-prot.component';
import { XProtDetailComponent } from './detail/x-prot-detail.component';
import { XProtUpdateComponent } from './update/x-prot-update.component';
import { XProtDeleteDialogComponent } from './delete/x-prot-delete-dialog.component';
import { XProtRoutingModule } from './route/x-prot-routing.module';

@NgModule({
  imports: [SharedModule, XProtRoutingModule],
  declarations: [XProtComponent, XProtDetailComponent, XProtUpdateComponent, XProtDeleteDialogComponent],
  entryComponents: [XProtDeleteDialogComponent],
})
export class XProtModule {}
