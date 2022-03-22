import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUseraccount, getUseraccountIdentifier } from '../useraccount.model';

export type EntityResponseType = HttpResponse<IUseraccount>;
export type EntityArrayResponseType = HttpResponse<IUseraccount[]>;

@Injectable({ providedIn: 'root' })
export class UseraccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/useraccounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(useraccount: IUseraccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(useraccount);
    return this.http
      .post<IUseraccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(useraccount: IUseraccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(useraccount);
    return this.http
      .put<IUseraccount>(`${this.resourceUrl}/${getUseraccountIdentifier(useraccount) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(useraccount: IUseraccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(useraccount);
    return this.http
      .patch<IUseraccount>(`${this.resourceUrl}/${getUseraccountIdentifier(useraccount) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUseraccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUseraccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUseraccountToCollectionIfMissing(
    useraccountCollection: IUseraccount[],
    ...useraccountsToCheck: (IUseraccount | null | undefined)[]
  ): IUseraccount[] {
    const useraccounts: IUseraccount[] = useraccountsToCheck.filter(isPresent);
    if (useraccounts.length > 0) {
      const useraccountCollectionIdentifiers = useraccountCollection.map(useraccountItem => getUseraccountIdentifier(useraccountItem)!);
      const useraccountsToAdd = useraccounts.filter(useraccountItem => {
        const useraccountIdentifier = getUseraccountIdentifier(useraccountItem);
        if (useraccountIdentifier == null || useraccountCollectionIdentifiers.includes(useraccountIdentifier)) {
          return false;
        }
        useraccountCollectionIdentifiers.push(useraccountIdentifier);
        return true;
      });
      return [...useraccountsToAdd, ...useraccountCollection];
    }
    return useraccountCollection;
  }

  protected convertDateFromClient(useraccount: IUseraccount): IUseraccount {
    return Object.assign({}, useraccount, {
      creationdate: useraccount.creationdate?.isValid() ? useraccount.creationdate.format(DATE_FORMAT) : undefined,
      lastupdated: useraccount.lastupdated?.isValid() ? useraccount.lastupdated.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationdate = res.body.creationdate ? dayjs(res.body.creationdate) : undefined;
      res.body.lastupdated = res.body.lastupdated ? dayjs(res.body.lastupdated) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((useraccount: IUseraccount) => {
        useraccount.creationdate = useraccount.creationdate ? dayjs(useraccount.creationdate) : undefined;
        useraccount.lastupdated = useraccount.lastupdated ? dayjs(useraccount.lastupdated) : undefined;
      });
    }
    return res;
  }
}
