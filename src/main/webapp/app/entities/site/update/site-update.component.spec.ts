import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SiteService } from '../service/site.service';
import { ISite, Site } from '../site.model';
import { IBusinessUnit } from 'app/entities/business-unit/business-unit.model';
import { BusinessUnitService } from 'app/entities/business-unit/service/business-unit.service';

import { SiteUpdateComponent } from './site-update.component';

describe('Site Management Update Component', () => {
  let comp: SiteUpdateComponent;
  let fixture: ComponentFixture<SiteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let siteService: SiteService;
  let businessUnitService: BusinessUnitService;

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
    businessUnitService = TestBed.inject(BusinessUnitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BusinessUnit query and add missing value', () => {
      const site: ISite = { id: 456 };
      const businessUnit: IBusinessUnit = { id: 75556 };
      site.businessUnit = businessUnit;

      const businessUnitCollection: IBusinessUnit[] = [{ id: 3105 }];
      jest.spyOn(businessUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: businessUnitCollection })));
      const additionalBusinessUnits = [businessUnit];
      const expectedCollection: IBusinessUnit[] = [...additionalBusinessUnits, ...businessUnitCollection];
      jest.spyOn(businessUnitService, 'addBusinessUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ site });
      comp.ngOnInit();

      expect(businessUnitService.query).toHaveBeenCalled();
      expect(businessUnitService.addBusinessUnitToCollectionIfMissing).toHaveBeenCalledWith(
        businessUnitCollection,
        ...additionalBusinessUnits
      );
      expect(comp.businessUnitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const site: ISite = { id: 456 };
      const businessUnit: IBusinessUnit = { id: 27667 };
      site.businessUnit = businessUnit;

      activatedRoute.data = of({ site });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(site));
      expect(comp.businessUnitsSharedCollection).toContain(businessUnit);
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
    describe('trackBusinessUnitById', () => {
      it('Should return tracked BusinessUnit primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBusinessUnitById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
