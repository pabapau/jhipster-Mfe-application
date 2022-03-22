import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IXProt, getXProtIdentifier } from '../x-prot.model';

export type EntityResponseType = HttpResponse<IXProt>;
export type EntityArrayResponseType = HttpResponse<IXProt[]>;

@Injectable({ providedIn: 'root' })
export class XProtService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/x-prots');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(xProt: IXProt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(xProt);
    return this.http
      .post<IXProt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(xProt: IXProt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(xProt);
    return this.http
      .put<IXProt>(`${this.resourceUrl}/${getXProtIdentifier(xProt) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(xProt: IXProt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(xProt);
    return this.http
      .patch<IXProt>(`${this.resourceUrl}/${getXProtIdentifier(xProt) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IXProt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IXProt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addXProtToCollectionIfMissing(xProtCollection: IXProt[], ...xProtsToCheck: (IXProt | null | undefined)[]): IXProt[] {
    const xProts: IXProt[] = xProtsToCheck.filter(isPresent);
    if (xProts.length > 0) {
      const xProtCollectionIdentifiers = xProtCollection.map(xProtItem => getXProtIdentifier(xProtItem)!);
      const xProtsToAdd = xProts.filter(xProtItem => {
        const xProtIdentifier = getXProtIdentifier(xProtItem);
        if (xProtIdentifier == null || xProtCollectionIdentifiers.includes(xProtIdentifier)) {
          return false;
        }
        xProtCollectionIdentifiers.push(xProtIdentifier);
        return true;
      });
      return [...xProtsToAdd, ...xProtCollection];
    }
    return xProtCollection;
  }

  protected convertDateFromClient(xProt: IXProt): IXProt {
    return Object.assign({}, xProt, {
      creationdate: xProt.creationdate?.isValid() ? xProt.creationdate.format(DATE_FORMAT) : undefined,
      lastupdated: xProt.lastupdated?.isValid() ? xProt.lastupdated.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((xProt: IXProt) => {
        xProt.creationdate = xProt.creationdate ? dayjs(xProt.creationdate) : undefined;
        xProt.lastupdated = xProt.lastupdated ? dayjs(xProt.lastupdated) : undefined;
      });
    }
    return res;
  }
}
