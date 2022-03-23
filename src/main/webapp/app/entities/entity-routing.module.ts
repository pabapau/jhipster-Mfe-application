import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'business-unit',
        data: { pageTitle: 'jhipsterMfeApplicationApp.businessUnit.home.title' },
        loadChildren: () => import('./business-unit/business-unit.module').then(m => m.BusinessUnitModule),
      },
      {
        path: 'user-account',
        data: { pageTitle: 'jhipsterMfeApplicationApp.userAccount.home.title' },
        loadChildren: () => import('./user-account/user-account.module').then(m => m.UserAccountModule),
      },
      {
        path: 'site',
        data: { pageTitle: 'jhipsterMfeApplicationApp.site.home.title' },
        loadChildren: () => import('./site/site.module').then(m => m.SiteModule),
      },
      {
        path: 'x-prot',
        data: { pageTitle: 'jhipsterMfeApplicationApp.xProt.home.title' },
        loadChildren: () => import('./x-prot/x-prot.module').then(m => m.XProtModule),
      },
      {
        path: 'flow',
        data: { pageTitle: 'jhipsterMfeApplicationApp.flow.home.title' },
        loadChildren: () => import('./flow/flow.module').then(m => m.FlowModule),
      },
      {
        path: 'file-descriptor',
        data: { pageTitle: 'jhipsterMfeApplicationApp.fileDescriptor.home.title' },
        loadChildren: () => import('./file-descriptor/file-descriptor.module').then(m => m.FileDescriptorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
