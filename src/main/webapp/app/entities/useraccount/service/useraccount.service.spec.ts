import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Accounttype } from 'app/entities/enumerations/accounttype.model';
import { IUseraccount, Useraccount } from '../useraccount.model';

import { UseraccountService } from './useraccount.service';

describe('Useraccount Service', () => {
  let service: UseraccountService;
  let httpMock: HttpTestingController;
  let elemDefault: IUseraccount;
  let expectedResult: IUseraccount | IUseraccount[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UseraccountService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      accounttype: Accounttype.TRF,
      comment: 'AAAAAAA',
      creationdate: currentDate,
      lastupdated: currentDate,
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

    it('should create a Useraccount', () => {
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

      service.create(new Useraccount()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Useraccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accounttype: 'BBBBBB',
          comment: 'BBBBBB',
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

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Useraccount', () => {
      const patchObject = Object.assign(
        {
          accounttype: 'BBBBBB',
        },
        new Useraccount()
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

    it('should return a list of Useraccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accounttype: 'BBBBBB',
          comment: 'BBBBBB',
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

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Useraccount', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUseraccountToCollectionIfMissing', () => {
      it('should add a Useraccount to an empty array', () => {
        const useraccount: IUseraccount = { id: 123 };
        expectedResult = service.addUseraccountToCollectionIfMissing([], useraccount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(useraccount);
      });

      it('should not add a Useraccount to an array that contains it', () => {
        const useraccount: IUseraccount = { id: 123 };
        const useraccountCollection: IUseraccount[] = [
          {
            ...useraccount,
          },
          { id: 456 },
        ];
        expectedResult = service.addUseraccountToCollectionIfMissing(useraccountCollection, useraccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Useraccount to an array that doesn't contain it", () => {
        const useraccount: IUseraccount = { id: 123 };
        const useraccountCollection: IUseraccount[] = [{ id: 456 }];
        expectedResult = service.addUseraccountToCollectionIfMissing(useraccountCollection, useraccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(useraccount);
      });

      it('should add only unique Useraccount to an array', () => {
        const useraccountArray: IUseraccount[] = [{ id: 123 }, { id: 456 }, { id: 78285 }];
        const useraccountCollection: IUseraccount[] = [{ id: 123 }];
        expectedResult = service.addUseraccountToCollectionIfMissing(useraccountCollection, ...useraccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const useraccount: IUseraccount = { id: 123 };
        const useraccount2: IUseraccount = { id: 456 };
        expectedResult = service.addUseraccountToCollectionIfMissing([], useraccount, useraccount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(useraccount);
        expect(expectedResult).toContain(useraccount2);
      });

      it('should accept null and undefined values', () => {
        const useraccount: IUseraccount = { id: 123 };
        expectedResult = service.addUseraccountToCollectionIfMissing([], null, useraccount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(useraccount);
      });

      it('should return initial array if no Useraccount is added', () => {
        const useraccountCollection: IUseraccount[] = [{ id: 123 }];
        expectedResult = service.addUseraccountToCollectionIfMissing(useraccountCollection, undefined, null);
        expect(expectedResult).toEqual(useraccountCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
