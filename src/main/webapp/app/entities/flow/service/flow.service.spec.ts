import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Flowusecase } from 'app/entities/enumerations/flowusecase.model';
import { Buildstate } from 'app/entities/enumerations/buildstate.model';
import { IFlow, Flow } from '../flow.model';

import { FlowService } from './flow.service';

describe('Flow Service', () => {
  let service: FlowService;
  let httpMock: HttpTestingController;
  let elemDefault: IFlow;
  let expectedResult: IFlow | IFlow[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FlowService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fileIdent: 'AAAAAAA',
      flowusecase: Flowusecase.A2A,
      description: 'AAAAAAA',
      creationdate: currentDate,
      lastupdated: currentDate,
      buildstate: Buildstate.NOTBUILD,
      buildcount: 0,
      buildcomment: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          creationdate: currentDate.format(DATE_FORMAT),
          lastupdated: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Flow', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          creationdate: currentDate.format(DATE_FORMAT),
          lastupdated: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationdate: currentDate,
          lastupdated: currentDate,
        },
        returnedFromService
      );

      service.create(new Flow()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Flow', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fileIdent: 'BBBBBB',
          flowusecase: 'BBBBBB',
          description: 'BBBBBB',
          creationdate: currentDate.format(DATE_FORMAT),
          lastupdated: currentDate.format(DATE_FORMAT),
          buildstate: 'BBBBBB',
          buildcount: 1,
          buildcomment: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationdate: currentDate,
          lastupdated: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Flow', () => {
      const patchObject = Object.assign(
        {
          fileIdent: 'BBBBBB',
          buildstate: 'BBBBBB',
        },
        new Flow()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          creationdate: currentDate,
          lastupdated: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Flow', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fileIdent: 'BBBBBB',
          flowusecase: 'BBBBBB',
          description: 'BBBBBB',
          creationdate: currentDate.format(DATE_FORMAT),
          lastupdated: currentDate.format(DATE_FORMAT),
          buildstate: 'BBBBBB',
          buildcount: 1,
          buildcomment: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationdate: currentDate,
          lastupdated: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Flow', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFlowToCollectionIfMissing', () => {
      it('should add a Flow to an empty array', () => {
        const flow: IFlow = { id: 123 };
        expectedResult = service.addFlowToCollectionIfMissing([], flow);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(flow);
      });

      it('should not add a Flow to an array that contains it', () => {
        const flow: IFlow = { id: 123 };
        const flowCollection: IFlow[] = [
          {
            ...flow,
          },
          { id: 456 },
        ];
        expectedResult = service.addFlowToCollectionIfMissing(flowCollection, flow);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Flow to an array that doesn't contain it", () => {
        const flow: IFlow = { id: 123 };
        const flowCollection: IFlow[] = [{ id: 456 }];
        expectedResult = service.addFlowToCollectionIfMissing(flowCollection, flow);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(flow);
      });

      it('should add only unique Flow to an array', () => {
        const flowArray: IFlow[] = [{ id: 123 }, { id: 456 }, { id: 78683 }];
        const flowCollection: IFlow[] = [{ id: 123 }];
        expectedResult = service.addFlowToCollectionIfMissing(flowCollection, ...flowArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const flow: IFlow = { id: 123 };
        const flow2: IFlow = { id: 456 };
        expectedResult = service.addFlowToCollectionIfMissing([], flow, flow2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(flow);
        expect(expectedResult).toContain(flow2);
      });

      it('should accept null and undefined values', () => {
        const flow: IFlow = { id: 123 };
        expectedResult = service.addFlowToCollectionIfMissing([], null, flow, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(flow);
      });

      it('should return initial array if no Flow is added', () => {
        const flowCollection: IFlow[] = [{ id: 123 }];
        expectedResult = service.addFlowToCollectionIfMissing(flowCollection, undefined, null);
        expect(expectedResult).toEqual(flowCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
