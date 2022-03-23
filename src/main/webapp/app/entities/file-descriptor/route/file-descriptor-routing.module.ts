import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FileDescriptorComponent } from '../list/file-descriptor.component';
import { FileDescriptorDetailComponent } from '../detail/file-descriptor-detail.component';
import { FileDescriptorUpdateComponent } from '../update/file-descriptor-update.component';
import { FileDescriptorRoutingResolveService } from './file-descriptor-routing-resolve.service';

const fileDescriptorRoute: Routes = [
  {
    path: '',
    component: FileDescriptorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileDescriptorDetailComponent,
    resolve: {
      fileDescriptor: FileDescriptorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileDescriptorUpdateComponent,
    resolve: {
      fileDescriptor: FileDescriptorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileDescriptorUpdateComponent,
    resolve: {
      fileDescriptor: FileDescriptorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fileDescriptorRoute)],
  exports: [RouterModule],
})
export class FileDescriptorRoutingModule {}
