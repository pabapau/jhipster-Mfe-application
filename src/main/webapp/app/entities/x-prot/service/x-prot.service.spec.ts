import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { XProttype } from 'app/entities/enumerations/x-prottype.model';
import { Xrole } from 'app/entities/enumerations/xrole.model';
import { Buildstate } from 'app/entities/enumerations/buildstate.model';
import { IXProt, XProt } from '../x-prot.model';

import { XProtService } from './x-prot.service';

describe('XProt Service', () => {
  let service: XProtService;
  let httpMock: HttpTestingController;
  let elemDefault: IXProt;
  let expectedResult: IXProt | IXProt[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(XProtService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      xprottype: XProttype.PESITANY,
      xrole: Xrole.CLI,
      comment: 'AAAAAAA',
      accessAddress: 'AAAAAAA',
      accessServicePoint: 0,
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

    it('should create a XProt', () => {
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

      service.create(new XProt()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a XProt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          xprottype: 'BBBBBB',
          xrole: 'BBBBBB',
          comment: 'BBBBBB',
          accessAddress: 'BBBBBB',
          accessServicePoint: 1,
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

    it('should partial update a XProt', () => {
      const patchObject = Object.assign(
        {
          accessAddress: 'BBBBBB',
          accessServicePoint: 1,
          lastupdated: currentDate.format(DATE_FORMAT),
          buildcomment: 'BBBBBB',
        },
        new XProt()
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

    it('should return a list of XProt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          xprottype: 'BBBBBB',
          xrole: 'BBBBBB',
          comment: 'BBBBBB',
          accessAddress: 'BBBBBB',
          accessServicePoint: 1,
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

    it('should delete a XProt', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addXProtToCollectionIfMissing', () => {
      it('should add a XProt to an empty array', () => {
        const xProt: IXProt = { id: 123 };
        expectedResult = service.addXProtToCollectionIfMissing([], xProt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xProt);
      });

      it('should not add a XProt to an array that contains it', () => {
        const xProt: IXProt = { id: 123 };
        const xProtCollection: IXProt[] = [
          {
            ...xProt,
          },
          { id: 456 },
        ];
        expectedResult = service.addXProtToCollectionIfMissing(xProtCollection, xProt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a XProt to an array that doesn't contain it", () => {
        const xProt: IXProt = { id: 123 };
        const xProtCollection: IXProt[] = [{ id: 456 }];
        expectedResult = service.addXProtToCollectionIfMissing(xProtCollection, xProt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xProt);
      });

      it('should add only unique XProt to an array', () => {
        const xProtArray: IXProt[] = [{ id: 123 }, { id: 456 }, { id: 17138 }];
        const xProtCollection: IXProt[] = [{ id: 123 }];
        expectedResult = service.addXProtToCollectionIfMissing(xProtCollection, ...xProtArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const xProt: IXProt = { id: 123 };
        const xProt2: IXProt = { id: 456 };
        expectedResult = service.addXProtToCollectionIfMissing([], xProt, xProt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xProt);
        expect(expectedResult).toContain(xProt2);
      });

      it('should accept null and undefined values', () => {
        const xProt: IXProt = { id: 123 };
        expectedResult = service.addXProtToCollectionIfMissing([], null, xProt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xProt);
      });

      it('should return initial array if no XProt is added', () => {
        const xProtCollection: IXProt[] = [{ id: 123 }];
        expectedResult = service.addXProtToCollectionIfMissing(xProtCollection, undefined, null);
        expect(expectedResult).toEqual(xProtCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
