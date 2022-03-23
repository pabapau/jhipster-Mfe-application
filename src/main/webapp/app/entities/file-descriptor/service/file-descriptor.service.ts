import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFileDescriptor, getFileDescriptorIdentifier } from '../file-descriptor.model';

export type EntityResponseType = HttpResponse<IFileDescriptor>;
export type EntityArrayResponseType = HttpResponse<IFileDescriptor[]>;

@Injectable({ providedIn: 'root' })
export class FileDescriptorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/file-descriptors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fileDescriptor: IFileDescriptor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileDescriptor);
    return this.http
      .post<IFileDescriptor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fileDescriptor: IFileDescriptor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileDescriptor);
    return this.http
      .put<IFileDescriptor>(`${this.resourceUrl}/${getFileDescriptorIdentifier(fileDescriptor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fileDescriptor: IFileDescriptor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileDescriptor);
    return this.http
      .patch<IFileDescriptor>(`${this.resourceUrl}/${getFileDescriptorIdentifier(fileDescriptor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFileDescriptor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFileDescriptor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFileDescriptorToCollectionIfMissing(
    fileDescriptorCollection: IFileDescriptor[],
    ...fileDescriptorsToCheck: (IFileDescriptor | null | undefined)[]
  ): IFileDescriptor[] {
    const fileDescriptors: IFileDescriptor[] = fileDescriptorsToCheck.filter(isPresent);
    if (fileDescriptors.length > 0) {
      const fileDescriptorCollectionIdentifiers = fileDescriptorCollection.map(
        fileDescriptorItem => getFileDescriptorIdentifier(fileDescriptorItem)!
      );
      const fileDescriptorsToAdd = fileDescriptors.filter(fileDescriptorItem => {
        const fileDescriptorIdentifier = getFileDescriptorIdentifier(fileDescriptorItem);
        if (fileDescriptorIdentifier == null || fileDescriptorCollectionIdentifiers.includes(fileDescriptorIdentifier)) {
          return false;
        }
        fileDescriptorCollectionIdentifiers.push(fileDescriptorIdentifier);
        return true;
      });
      return [...fileDescriptorsToAdd, ...fileDescriptorCollection];
    }
    return fileDescriptorCollection;
  }

  protected convertDateFromClient(fileDescriptor: IFileDescriptor): IFileDescriptor {
    return Object.assign({}, fileDescriptor, {
      creationDate: fileDescriptor.creationDate?.isValid() ? fileDescriptor.creationDate.format(DATE_FORMAT) : undefined,
      lastUpdated: fileDescriptor.lastUpdated?.isValid() ? fileDescriptor.lastUpdated.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? dayjs(res.body.creationDate) : undefined;
      res.body.lastUpdated = res.body.lastUpdated ? dayjs(res.body.lastUpdated) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fileDescriptor: IFileDescriptor) => {
        fileDescriptor.creationDate = fileDescriptor.creationDate ? dayjs(fileDescriptor.creationDate) : undefined;
        fileDescriptor.lastUpdated = fileDescriptor.lastUpdated ? dayjs(fileDescriptor.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
