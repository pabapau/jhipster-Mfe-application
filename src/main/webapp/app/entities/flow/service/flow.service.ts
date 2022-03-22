import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFlow, getFlowIdentifier } from '../flow.model';

export type EntityResponseType = HttpResponse<IFlow>;
export type EntityArrayResponseType = HttpResponse<IFlow[]>;

@Injectable({ providedIn: 'root' })
export class FlowService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/flows');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(flow: IFlow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flow);
    return this.http
      .post<IFlow>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(flow: IFlow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flow);
    return this.http
      .put<IFlow>(`${this.resourceUrl}/${getFlowIdentifier(flow) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(flow: IFlow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flow);
    return this.http
      .patch<IFlow>(`${this.resourceUrl}/${getFlowIdentifier(flow) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFlow>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFlow[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFlowToCollectionIfMissing(flowCollection: IFlow[], ...flowsToCheck: (IFlow | null | undefined)[]): IFlow[] {
    const flows: IFlow[] = flowsToCheck.filter(isPresent);
    if (flows.length > 0) {
      const flowCollectionIdentifiers = flowCollection.map(flowItem => getFlowIdentifier(flowItem)!);
      const flowsToAdd = flows.filter(flowItem => {
        const flowIdentifier = getFlowIdentifier(flowItem);
        if (flowIdentifier == null || flowCollectionIdentifiers.includes(flowIdentifier)) {
          return false;
        }
        flowCollectionIdentifiers.push(flowIdentifier);
        return true;
      });
      return [...flowsToAdd, ...flowCollection];
    }
    return flowCollection;
  }

  protected convertDateFromClient(flow: IFlow): IFlow {
    return Object.assign({}, flow, {
      creationdate: flow.creationdate?.isValid() ? flow.creationdate.format(DATE_FORMAT) : undefined,
      lastupdated: flow.lastupdated?.isValid() ? flow.lastupdated.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((flow: IFlow) => {
        flow.creationdate = flow.creationdate ? dayjs(flow.creationdate) : undefined;
        flow.lastupdated = flow.lastupdated ? dayjs(flow.lastupdated) : undefined;
      });
    }
    return res;
  }
}
