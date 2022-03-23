import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { AccountType } from 'app/entities/enumerations/account-type.model';
import { IUserAccount, UserAccount } from '../user-account.model';

import { UserAccountService } from './user-account.service';

describe('UserAccount Service', () => {
  let service: UserAccountService;
  let httpMock: HttpTestingController;
  let elemDefault: IUserAccount;
  let expectedResult: IUserAccount | IUserAccount[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserAccountService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      accountType: AccountType.TRF,
      comment: 'AAAAAAA',
      creationDate: currentDate,
      lastUpdated: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          creationDate: currentDate.format(DATE_FORMAT),
          lastUpdated: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a UserAccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          creationDate: currentDate.format(DATE_FORMAT),
          lastUpdated: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationDate: currentDate,
          lastUpdated: currentDate,
        },
        returnedFromService
      );

      service.create(new UserAccount()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserAccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountType: 'BBBBBB',
          comment: 'BBBBBB',
          creationDate: currentDate.format(DATE_FORMAT),
          lastUpdated: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationDate: currentDate,
          lastUpdated: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserAccount', () => {
      const patchObject = Object.assign(
        {
          comment: 'BBBBBB',
        },
        new UserAccount()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          creationDate: currentDate,
          lastUpdated: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserAccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountType: 'BBBBBB',
          comment: 'BBBBBB',
          creationDate: currentDate.format(DATE_FORMAT),
          lastUpdated: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationDate: currentDate,
          lastUpdated: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a UserAccount', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUserAccountToCollectionIfMissing', () => {
      it('should add a UserAccount to an empty array', () => {
        const userAccount: IUserAccount = { id: 123 };
        expectedResult = service.addUserAccountToCollectionIfMissing([], userAccount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAccount);
      });

      it('should not add a UserAccount to an array that contains it', () => {
        const userAccount: IUserAccount = { id: 123 };
        const userAccountCollection: IUserAccount[] = [
          {
            ...userAccount,
          },
          { id: 456 },
        ];
        expectedResult = service.addUserAccountToCollectionIfMissing(userAccountCollection, userAccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserAccount to an array that doesn't contain it", () => {
        const userAccount: IUserAccount = { id: 123 };
        const userAccountCollection: IUserAccount[] = [{ id: 456 }];
        expectedResult = service.addUserAccountToCollectionIfMissing(userAccountCollection, userAccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAccount);
      });

      it('should add only unique UserAccount to an array', () => {
        const userAccountArray: IUserAccount[] = [{ id: 123 }, { id: 456 }, { id: 12100 }];
        const userAccountCollection: IUserAccount[] = [{ id: 123 }];
        expectedResult = service.addUserAccountToCollectionIfMissing(userAccountCollection, ...userAccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userAccount: IUserAccount = { id: 123 };
        const userAccount2: IUserAccount = { id: 456 };
        expectedResult = service.addUserAccountToCollectionIfMissing([], userAccount, userAccount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAccount);
        expect(expectedResult).toContain(userAccount2);
      });

      it('should accept null and undefined values', () => {
        const userAccount: IUserAccount = { id: 123 };
        expectedResult = service.addUserAccountToCollectionIfMissing([], null, userAccount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAccount);
      });

      it('should return initial array if no UserAccount is added', () => {
        const userAccountCollection: IUserAccount[] = [{ id: 123 }];
        expectedResult = service.addUserAccountToCollectionIfMissing(userAccountCollection, undefined, null);
        expect(expectedResult).toEqual(userAccountCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
