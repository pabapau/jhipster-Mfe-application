import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { XProtComponent } from '../list/x-prot.component';
import { XProtDetailComponent } from '../detail/x-prot-detail.component';
import { XProtUpdateComponent } from '../update/x-prot-update.component';
import { XProtRoutingResolveService } from './x-prot-routing-resolve.service';

const xProtRoute: Routes = [
  {
    path: '',
    component: XProtComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: XProtDetailComponent,
    resolve: {
      xProt: XProtRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: XProtUpdateComponent,
    resolve: {
      xProt: XProtRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: XProtUpdateComponent,
    resolve: {
      xProt: XProtRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(xProtRoute)],
  exports: [RouterModule],
})
export class XProtRoutingModule {}
