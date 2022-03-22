import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusinessunit, getBusinessunitIdentifier } from '../businessunit.model';

export type EntityResponseType = HttpResponse<IBusinessunit>;
export type EntityArrayResponseType = HttpResponse<IBusinessunit[]>;

@Injectable({ providedIn: 'root' })
export class BusinessunitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/businessunits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(businessunit: IBusinessunit): Observable<EntityResponseType> {
    return this.http.post<IBusinessunit>(this.resourceUrl, businessunit, { observe: 'response' });
  }

  update(businessunit: IBusinessunit): Observable<EntityResponseType> {
    return this.http.put<IBusinessunit>(`${this.resourceUrl}/${getBusinessunitIdentifier(businessunit) as number}`, businessunit, {
      observe: 'response',
    });
  }

  partialUpdate(businessunit: IBusinessunit): Observable<EntityResponseType> {
    return this.http.patch<IBusinessunit>(`${this.resourceUrl}/${getBusinessunitIdentifier(businessunit) as number}`, businessunit, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusinessunit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusinessunit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBusinessunitToCollectionIfMissing(
    businessunitCollection: IBusinessunit[],
    ...businessunitsToCheck: (IBusinessunit | null | undefined)[]
  ): IBusinessunit[] {
    const businessunits: IBusinessunit[] = businessunitsToCheck.filter(isPresent);
    if (businessunits.length > 0) {
      const businessunitCollectionIdentifiers = businessunitCollection.map(
        businessunitItem => getBusinessunitIdentifier(businessunitItem)!
      );
      const businessunitsToAdd = businessunits.filter(businessunitItem => {
        const businessunitIdentifier = getBusinessunitIdentifier(businessunitItem);
        if (businessunitIdentifier == null || businessunitCollectionIdentifiers.includes(businessunitIdentifier)) {
          return false;
        }
        businessunitCollectionIdentifiers.push(businessunitIdentifier);
        return true;
      });
      return [...businessunitsToAdd, ...businessunitCollection];
    }
    return businessunitCollection;
  }
}
