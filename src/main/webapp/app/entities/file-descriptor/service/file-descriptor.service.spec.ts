import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { FlowUseCase } from 'app/entities/enumerations/flow-use-case.model';
import { BuildState } from 'app/entities/enumerations/build-state.model';
import { IFileDescriptor, FileDescriptor } from '../file-descriptor.model';

import { FileDescriptorService } from './file-descriptor.service';

describe('FileDescriptor Service', () => {
  let service: FileDescriptorService;
  let httpMock: HttpTestingController;
  let elemDefault: IFileDescriptor;
  let expectedResult: IFileDescriptor | IFileDescriptor[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FileDescriptorService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fileIdent: 'AAAAAAA',
      flowUseCase: FlowUseCase.A2A,
      description: 'AAAAAAA',
      creationDate: currentDate,
      lastUpdated: currentDate,
      buildState: BuildState.NOTBUILD,
      buildCount: 0,
      buildComment: 'AAAAAAA',
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

    it('should create a FileDescriptor', () => {
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

      service.create(new FileDescriptor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FileDescriptor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fileIdent: 'BBBBBB',
          flowUseCase: 'BBBBBB',
          description: 'BBBBBB',
          creationDate: currentDate.format(DATE_FORMAT),
          lastUpdated: currentDate.format(DATE_FORMAT),
          buildState: 'BBBBBB',
          buildCount: 1,
          buildComment: 'BBBBBB',
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

    it('should partial update a FileDescriptor', () => {
      const patchObject = Object.assign(
        {
          fileIdent: 'BBBBBB',
          flowUseCase: 'BBBBBB',
          description: 'BBBBBB',
          creationDate: currentDate.format(DATE_FORMAT),
          lastUpdated: currentDate.format(DATE_FORMAT),
          buildState: 'BBBBBB',
          buildCount: 1,
        },
        new FileDescriptor()
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

    it('should return a list of FileDescriptor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fileIdent: 'BBBBBB',
          flowUseCase: 'BBBBBB',
          description: 'BBBBBB',
          creationDate: currentDate.format(DATE_FORMAT),
          lastUpdated: currentDate.format(DATE_FORMAT),
          buildState: 'BBBBBB',
          buildCount: 1,
          buildComment: 'BBBBBB',
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

    it('should delete a FileDescriptor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFileDescriptorToCollectionIfMissing', () => {
      it('should add a FileDescriptor to an empty array', () => {
        const fileDescriptor: IFileDescriptor = { id: 123 };
        expectedResult = service.addFileDescriptorToCollectionIfMissing([], fileDescriptor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileDescriptor);
      });

      it('should not add a FileDescriptor to an array that contains it', () => {
        const fileDescriptor: IFileDescriptor = { id: 123 };
        const fileDescriptorCollection: IFileDescriptor[] = [
          {
            ...fileDescriptor,
          },
          { id: 456 },
        ];
        expectedResult = service.addFileDescriptorToCollectionIfMissing(fileDescriptorCollection, fileDescriptor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FileDescriptor to an array that doesn't contain it", () => {
        const fileDescriptor: IFileDescriptor = { id: 123 };
        const fileDescriptorCollection: IFileDescriptor[] = [{ id: 456 }];
        expectedResult = service.addFileDescriptorToCollectionIfMissing(fileDescriptorCollection, fileDescriptor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileDescriptor);
      });

      it('should add only unique FileDescriptor to an array', () => {
        const fileDescriptorArray: IFileDescriptor[] = [{ id: 123 }, { id: 456 }, { id: 5438 }];
        const fileDescriptorCollection: IFileDescriptor[] = [{ id: 123 }];
        expectedResult = service.addFileDescriptorToCollectionIfMissing(fileDescriptorCollection, ...fileDescriptorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fileDescriptor: IFileDescriptor = { id: 123 };
        const fileDescriptor2: IFileDescriptor = { id: 456 };
        expectedResult = service.addFileDescriptorToCollectionIfMissing([], fileDescriptor, fileDescriptor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileDescriptor);
        expect(expectedResult).toContain(fileDescriptor2);
      });

      it('should accept null and undefined values', () => {
        const fileDescriptor: IFileDescriptor = { id: 123 };
        expectedResult = service.addFileDescriptorToCollectionIfMissing([], null, fileDescriptor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileDescriptor);
      });

      it('should return initial array if no FileDescriptor is added', () => {
        const fileDescriptorCollection: IFileDescriptor[] = [{ id: 123 }];
        expectedResult = service.addFileDescriptorToCollectionIfMissing(fileDescriptorCollection, undefined, null);
        expect(expectedResult).toEqual(fileDescriptorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
