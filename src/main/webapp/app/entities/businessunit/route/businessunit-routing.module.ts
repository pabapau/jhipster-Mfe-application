import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BusinessunitComponent } from '../list/businessunit.component';
import { BusinessunitDetailComponent } from '../detail/businessunit-detail.component';
import { BusinessunitUpdateComponent } from '../update/businessunit-update.component';
import { BusinessunitRoutingResolveService } from './businessunit-routing-resolve.service';

const businessunitRoute: Routes = [
  {
    path: '',
    component: BusinessunitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessunitDetailComponent,
    resolve: {
      businessunit: BusinessunitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusinessunitUpdateComponent,
    resolve: {
      businessunit: BusinessunitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessunitUpdateComponent,
    resolve: {
      businessunit: BusinessunitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(businessunitRoute)],
  exports: [RouterModule],
})
export class BusinessunitRoutingModule {}
