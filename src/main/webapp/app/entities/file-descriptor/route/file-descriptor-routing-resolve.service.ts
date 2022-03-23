import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFileDescriptor, FileDescriptor } from '../file-descriptor.model';
import { FileDescriptorService } from '../service/file-descriptor.service';

@Injectable({ providedIn: 'root' })
export class FileDescriptorRoutingResolveService implements Resolve<IFileDescriptor> {
  constructor(protected service: FileDescriptorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileDescriptor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fileDescriptor: HttpResponse<FileDescriptor>) => {
          if (fileDescriptor.body) {
            return of(fileDescriptor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FileDescriptor());
  }
}
