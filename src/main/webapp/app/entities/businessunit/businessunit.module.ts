import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BusinessunitComponent } from './list/businessunit.component';
import { BusinessunitDetailComponent } from './detail/businessunit-detail.component';
import { BusinessunitUpdateComponent } from './update/businessunit-update.component';
import { BusinessunitDeleteDialogComponent } from './delete/businessunit-delete-dialog.component';
import { BusinessunitRoutingModule } from './route/businessunit-routing.module';

@NgModule({
  imports: [SharedModule, BusinessunitRoutingModule],
  declarations: [BusinessunitComponent, BusinessunitDetailComponent, BusinessunitUpdateComponent, BusinessunitDeleteDialogComponent],
  entryComponents: [BusinessunitDeleteDialogComponent],
})
export class BusinessunitModule {}
