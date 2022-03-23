import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FlowService } from '../service/flow.service';
import { IFlow, Flow } from '../flow.model';
import { IFileDescriptor } from 'app/entities/file-descriptor/file-descriptor.model';
import { FileDescriptorService } from 'app/entities/file-descriptor/service/file-descriptor.service';
import { IBusinessUnit } from 'app/entities/business-unit/business-unit.model';
import { BusinessUnitService } from 'app/entities/business-unit/service/business-unit.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

import { FlowUpdateComponent } from './flow-update.component';

describe('Flow Management Update Component', () => {
  let comp: FlowUpdateComponent;
  let fixture: ComponentFixture<FlowUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let flowService: FlowService;
  let fileDescriptorService: FileDescriptorService;
  let businessUnitService: BusinessUnitService;
  let siteService: SiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FlowUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FlowUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FlowUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    flowService = TestBed.inject(FlowService);
    fileDescriptorService = TestBed.inject(FileDescriptorService);
    businessUnitService = TestBed.inject(BusinessUnitService);
    siteService = TestBed.inject(SiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call originFileDescriptor query and add missing value', () => {
      const flow: IFlow = { id: 456 };
      const originFileDescriptor: IFileDescriptor = { id: 33104 };
      flow.originFileDescriptor = originFileDescriptor;

      const originFileDescriptorCollection: IFileDescriptor[] = [{ id: 84687 }];
      jest.spyOn(fileDescriptorService, 'query').mockReturnValue(of(new HttpResponse({ body: originFileDescriptorCollection })));
      const expectedCollection: IFileDescriptor[] = [originFileDescriptor, ...originFileDescriptorCollection];
      jest.spyOn(fileDescriptorService, 'addFileDescriptorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flow });
      comp.ngOnInit();

      expect(fileDescriptorService.query).toHaveBeenCalled();
      expect(fileDescriptorService.addFileDescriptorToCollectionIfMissing).toHaveBeenCalledWith(
        originFileDescriptorCollection,
        originFileDescriptor
      );
      expect(comp.originFileDescriptorsCollection).toEqual(expectedCollection);
    });

    it('Should call destFileDescriptor query and add missing value', () => {
      const flow: IFlow = { id: 456 };
      const destFileDescriptor: IFileDescriptor = { id: 53322 };
      flow.destFileDescriptor = destFileDescriptor;

      const destFileDescriptorCollection: IFileDescriptor[] = [{ id: 70336 }];
      jest.spyOn(fileDescriptorService, 'query').mockReturnValue(of(new HttpResponse({ body: destFileDescriptorCollection })));
      const expectedCollection: IFileDescriptor[] = [destFileDescriptor, ...destFileDescriptorCollection];
      jest.spyOn(fileDescriptorService, 'addFileDescriptorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flow });
      comp.ngOnInit();

      expect(fileDescriptorService.query).toHaveBeenCalled();
      expect(fileDescriptorService.addFileDescriptorToCollectionIfMissing).toHaveBeenCalledWith(
        destFileDescriptorCollection,
        destFileDescriptor
      );
      expect(comp.destFileDescriptorsCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessUnit query and add missing value', () => {
      const flow: IFlow = { id: 456 };
      const businessUnit: IBusinessUnit = { id: 1859 };
      flow.businessUnit = businessUnit;

      const businessUnitCollection: IBusinessUnit[] = [{ id: 90890 }];
      jest.spyOn(businessUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: businessUnitCollection })));
      const additionalBusinessUnits = [businessUnit];
      const expectedCollection: IBusinessUnit[] = [...additionalBusinessUnits, ...businessUnitCollection];
      jest.spyOn(businessUnitService, 'addBusinessUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flow });
      comp.ngOnInit();

      expect(businessUnitService.query).toHaveBeenCalled();
      expect(businessUnitService.addBusinessUnitToCollectionIfMissing).toHaveBeenCalledWith(
        businessUnitCollection,
        ...additionalBusinessUnits
      );
      expect(comp.businessUnitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Site query and add missing value', () => {
      const flow: IFlow = { id: 456 };
      const origin: ISite = { id: 22278 };
      flow.origin = origin;
      const destination: ISite = { id: 70671 };
      flow.destination = destination;

      const siteCollection: ISite[] = [{ id: 50093 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [origin, destination];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flow });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(siteCollection, ...additionalSites);
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const flow: IFlow = { id: 456 };
      const originFileDescriptor: IFileDescriptor = { id: 77152 };
      flow.originFileDescriptor = originFileDescriptor;
      const destFileDescriptor: IFileDescriptor = { id: 27525 };
      flow.destFileDescriptor = destFileDescriptor;
      const businessUnit: IBusinessUnit = { id: 99685 };
      flow.businessUnit = businessUnit;
      const origin: ISite = { id: 4509 };
      flow.origin = origin;
      const destination: ISite = { id: 41527 };
      flow.destination = destination;

      activatedRoute.data = of({ flow });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(flow));
      expect(comp.originFileDescriptorsCollection).toContain(originFileDescriptor);
      expect(comp.destFileDescriptorsCollection).toContain(destFileDescriptor);
      expect(comp.businessUnitsSharedCollection).toContain(businessUnit);
      expect(comp.sitesSharedCollection).toContain(origin);
      expect(comp.sitesSharedCollection).toContain(destination);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Flow>>();
      const flow = { id: 123 };
      jest.spyOn(flowService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flow });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: flow }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(flowService.update).toHaveBeenCalledWith(flow);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Flow>>();
      const flow = new Flow();
      jest.spyOn(flowService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flow });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: flow }));
      saveSubject.complete();

      // THEN
      expect(flowService.create).toHaveBeenCalledWith(flow);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Flow>>();
      const flow = { id: 123 };
      jest.spyOn(flowService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flow });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(flowService.update).toHaveBeenCalledWith(flow);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFileDescriptorById', () => {
      it('Should return tracked FileDescriptor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFileDescriptorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBusinessUnitById', () => {
      it('Should return tracked BusinessUnit primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBusinessUnitById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSiteById', () => {
      it('Should return tracked Site primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSiteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
