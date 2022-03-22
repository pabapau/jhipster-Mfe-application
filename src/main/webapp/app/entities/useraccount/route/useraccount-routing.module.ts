import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UseraccountComponent } from '../list/useraccount.component';
import { UseraccountDetailComponent } from '../detail/useraccount-detail.component';
import { UseraccountUpdateComponent } from '../update/useraccount-update.component';
import { UseraccountRoutingResolveService } from './useraccount-routing-resolve.service';

const useraccountRoute: Routes = [
  {
    path: '',
    component: UseraccountComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UseraccountDetailComponent,
    resolve: {
      useraccount: UseraccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UseraccountUpdateComponent,
    resolve: {
      useraccount: UseraccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UseraccountUpdateComponent,
    resolve: {
      useraccount: UseraccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(useraccountRoute)],
  exports: [RouterModule],
})
export class UseraccountRoutingModule {}
