import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UseraccountComponent } from './list/useraccount.component';
import { UseraccountDetailComponent } from './detail/useraccount-detail.component';
import { UseraccountUpdateComponent } from './update/useraccount-update.component';
import { UseraccountDeleteDialogComponent } from './delete/useraccount-delete-dialog.component';
import { UseraccountRoutingModule } from './route/useraccount-routing.module';

@NgModule({
  imports: [SharedModule, UseraccountRoutingModule],
  declarations: [UseraccountComponent, UseraccountDetailComponent, UseraccountUpdateComponent, UseraccountDeleteDialogComponent],
  entryComponents: [UseraccountDeleteDialogComponent],
})
export class UseraccountModule {}
