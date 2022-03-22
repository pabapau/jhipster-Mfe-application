import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SiteService } from '../service/site.service';
import { ISite, Site } from '../site.model';
import { IBusinessunit } from 'app/entities/businessunit/businessunit.model';
import { BusinessunitService } from 'app/entities/businessunit/service/businessunit.service';

import { SiteUpdateComponent } from './site-update.component';

describe('Site Management Update Component', () => {
  let comp: SiteUpdateComponent;
  let fixture: ComponentFixture<SiteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let siteService: SiteService;
  let businessunitService: BusinessunitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SiteUpdateComponent],
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
      .overrideTemplate(SiteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SiteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    siteService = TestBed.inject(SiteService);
    businessunitService = TestBed.inject(BusinessunitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Businessunit query and add missing value', () => {
      const site: ISite = { id: 456 };
      const businessunit: IBusinessunit = { id: 75556 };
      site.businessunit = businessunit;

      const businessunitCollection: IBusinessunit[] = [{ id: 3105 }];
      jest.spyOn(businessunitService, 'query').mockReturnValue(of(new HttpResponse({ body: businessunitCollection })));
      const additionalBusinessunits = [businessunit];
      const expectedCollection: IBusinessunit[] = [...additionalBusinessunits, ...businessunitCollection];
      jest.spyOn(businessunitService, 'addBusinessunitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ site });
      comp.ngOnInit();

      expect(businessunitService.query).toHaveBeenCalled();
      expect(businessunitService.addBusinessunitToCollectionIfMissing).toHaveBeenCalledWith(
        businessunitCollection,
        ...additionalBusinessunits
      );
      expect(comp.businessunitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const site: ISite = { id: 456 };
      const businessunit: IBusinessunit = { id: 27667 };
      site.businessunit = businessunit;

      activatedRoute.data = of({ site });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(site));
      expect(comp.businessunitsSharedCollection).toContain(businessunit);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Site>>();
      const site = { id: 123 };
      jest.spyOn(siteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ site });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: site }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(siteService.update).toHaveBeenCalledWith(site);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Site>>();
      const site = new Site();
      jest.spyOn(siteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ site });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: site }));
      saveSubject.complete();

      // THEN
      expect(siteService.create).toHaveBeenCalledWith(site);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Site>>();
      const site = { id: 123 };
      jest.spyOn(siteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ site });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(siteService.update).toHaveBeenCalledWith(site);
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
  });
});
