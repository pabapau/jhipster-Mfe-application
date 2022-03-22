import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'businessunit',
        data: { pageTitle: 'jhipsterMfeApplicationApp.businessunit.home.title' },
        loadChildren: () => import('./businessunit/businessunit.module').then(m => m.BusinessunitModule),
      },
      {
        path: 'useraccount',
        data: { pageTitle: 'jhipsterMfeApplicationApp.useraccount.home.title' },
        loadChildren: () => import('./useraccount/useraccount.module').then(m => m.UseraccountModule),
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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
