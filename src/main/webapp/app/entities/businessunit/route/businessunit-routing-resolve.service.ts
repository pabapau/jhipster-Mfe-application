import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusinessunit, Businessunit } from '../businessunit.model';
import { BusinessunitService } from '../service/businessunit.service';

@Injectable({ providedIn: 'root' })
export class BusinessunitRoutingResolveService implements Resolve<IBusinessunit> {
  constructor(protected service: BusinessunitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusinessunit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((businessunit: HttpResponse<Businessunit>) => {
          if (businessunit.body) {
            return of(businessunit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Businessunit());
  }
}
