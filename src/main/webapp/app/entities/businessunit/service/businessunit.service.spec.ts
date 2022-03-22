import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBusinessunit, Businessunit } from '../businessunit.model';

import { BusinessunitService } from './businessunit.service';

describe('Businessunit Service', () => {
  let service: BusinessunitService;
  let httpMock: HttpTestingController;
  let elemDefault: IBusinessunit;
  let expectedResult: IBusinessunit | IBusinessunit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BusinessunitService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      code: 'AAAAAAA',
      name: 'AAAAAAA',
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Businessunit', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Businessunit()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Businessunit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Businessunit', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        new Businessunit()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Businessunit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Businessunit', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBusinessunitToCollectionIfMissing', () => {
      it('should add a Businessunit to an empty array', () => {
        const businessunit: IBusinessunit = { id: 123 };
        expectedResult = service.addBusinessunitToCollectionIfMissing([], businessunit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessunit);
      });

      it('should not add a Businessunit to an array that contains it', () => {
        const businessunit: IBusinessunit = { id: 123 };
        const businessunitCollection: IBusinessunit[] = [
          {
            ...businessunit,
          },
          { id: 456 },
        ];
        expectedResult = service.addBusinessunitToCollectionIfMissing(businessunitCollection, businessunit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Businessunit to an array that doesn't contain it", () => {
        const businessunit: IBusinessunit = { id: 123 };
        const businessunitCollection: IBusinessunit[] = [{ id: 456 }];
        expectedResult = service.addBusinessunitToCollectionIfMissing(businessunitCollection, businessunit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessunit);
      });

      it('should add only unique Businessunit to an array', () => {
        const businessunitArray: IBusinessunit[] = [{ id: 123 }, { id: 456 }, { id: 6006 }];
        const businessunitCollection: IBusinessunit[] = [{ id: 123 }];
        expectedResult = service.addBusinessunitToCollectionIfMissing(businessunitCollection, ...businessunitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const businessunit: IBusinessunit = { id: 123 };
        const businessunit2: IBusinessunit = { id: 456 };
        expectedResult = service.addBusinessunitToCollectionIfMissing([], businessunit, businessunit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessunit);
        expect(expectedResult).toContain(businessunit2);
      });

      it('should accept null and undefined values', () => {
        const businessunit: IBusinessunit = { id: 123 };
        expectedResult = service.addBusinessunitToCollectionIfMissing([], null, businessunit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessunit);
      });

      it('should return initial array if no Businessunit is added', () => {
        const businessunitCollection: IBusinessunit[] = [{ id: 123 }];
        expectedResult = service.addBusinessunitToCollectionIfMissing(businessunitCollection, undefined, null);
        expect(expectedResult).toEqual(businessunitCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
