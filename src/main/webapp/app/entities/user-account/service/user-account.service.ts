import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserAccount, getUserAccountIdentifier } from '../user-account.model';

export type EntityResponseType = HttpResponse<IUserAccount>;
export type EntityArrayResponseType = HttpResponse<IUserAccount[]>;

@Injectable({ providedIn: 'root' })
export class UserAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userAccount: IUserAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userAccount);
    return this.http
      .post<IUserAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(userAccount: IUserAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userAccount);
    return this.http
      .put<IUserAccount>(`${this.resourceUrl}/${getUserAccountIdentifier(userAccount) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(userAccount: IUserAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userAccount);
    return this.http
      .patch<IUserAccount>(`${this.resourceUrl}/${getUserAccountIdentifier(userAccount) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserAccountToCollectionIfMissing(
    userAccountCollection: IUserAccount[],
    ...userAccountsToCheck: (IUserAccount | null | undefined)[]
  ): IUserAccount[] {
    const userAccounts: IUserAccount[] = userAccountsToCheck.filter(isPresent);
    if (userAccounts.length > 0) {
      const userAccountCollectionIdentifiers = userAccountCollection.map(userAccountItem => getUserAccountIdentifier(userAccountItem)!);
      const userAccountsToAdd = userAccounts.filter(userAccountItem => {
        const userAccountIdentifier = getUserAccountIdentifier(userAccountItem);
        if (userAccountIdentifier == null || userAccountCollectionIdentifiers.includes(userAccountIdentifier)) {
          return false;
        }
        userAccountCollectionIdentifiers.push(userAccountIdentifier);
        return true;
      });
      return [...userAccountsToAdd, ...userAccountCollection];
    }
    return userAccountCollection;
  }

  protected convertDateFromClient(userAccount: IUserAccount): IUserAccount {
    return Object.assign({}, userAccount, {
      creationDate: userAccount.creationDate?.isValid() ? userAccount.creationDate.format(DATE_FORMAT) : undefined,
      lastUpdated: userAccount.lastUpdated?.isValid() ? userAccount.lastUpdated.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((userAccount: IUserAccount) => {
        userAccount.creationDate = userAccount.creationDate ? dayjs(userAccount.creationDate) : undefined;
        userAccount.lastUpdated = userAccount.lastUpdated ? dayjs(userAccount.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
