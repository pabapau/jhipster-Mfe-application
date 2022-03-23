import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FlowComponent } from '../list/flow.component';
import { FlowDetailComponent } from '../detail/flow-detail.component';
import { FlowUpdateComponent } from '../update/flow-update.component';
import { FlowRoutingResolveService } from './flow-routing-resolve.service';

const flowRoute: Routes = [
  {
    path: '',
    component: FlowComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FlowDetailComponent,
    resolve: {
      flow: FlowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FlowUpdateComponent,
    resolve: {
      flow: FlowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FlowUpdateComponent,
    resolve: {
      flow: FlowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(flowRoute)],
  exports: [RouterModule],
})
export class FlowRoutingModule {}
