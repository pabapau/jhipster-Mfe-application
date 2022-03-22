import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FlowService } from '../service/flow.service';
import { IFlow, Flow } from '../flow.model';
import { IBusinessunit } from 'app/entities/businessunit/businessunit.model';
import { BusinessunitService } from 'app/entities/businessunit/service/businessunit.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

import { FlowUpdateComponent } from './flow-update.component';

describe('Flow Management Update Component', () => {
  let comp: FlowUpdateComponent;
  let fixture: ComponentFixture<FlowUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let flowService: FlowService;
  let businessunitService: BusinessunitService;
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
    businessunitService = TestBed.inject(BusinessunitService);
    siteService = TestBed.inject(SiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Businessunit query and add missing value', () => {
      const flow: IFlow = { id: 456 };
      const businessunit: IBusinessunit = { id: 1859 };
      flow.businessunit = businessunit;

      const businessunitCollection: IBusinessunit[] = [{ id: 90890 }];
      jest.spyOn(businessunitService, 'query').mockReturnValue(of(new HttpResponse({ body: businessunitCollection })));
      const additionalBusinessunits = [businessunit];
      const expectedCollection: IBusinessunit[] = [...additionalBusinessunits, ...businessunitCollection];
      jest.spyOn(businessunitService, 'addBusinessunitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flow });
      comp.ngOnInit();

      expect(businessunitService.query).toHaveBeenCalled();
      expect(businessunitService.addBusinessunitToCollectionIfMissing).toHaveBeenCalledWith(
        businessunitCollection,
        ...additionalBusinessunits
      );
      expect(comp.businessunitsSharedCollection).toEqual(expectedCollection);
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
      const businessunit: IBusinessunit = { id: 99685 };
      flow.businessunit = businessunit;
      const origin: ISite = { id: 4509 };
      flow.origin = origin;
      const destination: ISite = { id: 41527 };
      flow.destination = destination;

      activatedRoute.data = of({ flow });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(flow));
      expect(comp.businessunitsSharedCollection).toContain(businessunit);
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
    describe('trackBusinessunitById', () => {
      it('Should return tracked Businessunit primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBusinessunitById(0, entity);
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
