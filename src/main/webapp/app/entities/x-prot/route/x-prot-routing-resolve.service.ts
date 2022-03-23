import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IXProt, XProt } from '../x-prot.model';
import { XProtService } from '../service/x-prot.service';

@Injectable({ providedIn: 'root' })
export class XProtRoutingResolveService implements Resolve<IXProt> {
  constructor(protected service: XProtService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IXProt> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((xProt: HttpResponse<XProt>) => {
          if (xProt.body) {
            return of(xProt.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new XProt());
  }
}
