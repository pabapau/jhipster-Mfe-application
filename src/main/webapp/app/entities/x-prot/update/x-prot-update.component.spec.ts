import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { XProtService } from '../service/x-prot.service';
import { IXProt, XProt } from '../x-prot.model';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

import { XProtUpdateComponent } from './x-prot-update.component';

describe('XProt Management Update Component', () => {
  let comp: XProtUpdateComponent;
  let fixture: ComponentFixture<XProtUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let xProtService: XProtService;
  let siteService: SiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [XProtUpdateComponent],
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
      .overrideTemplate(XProtUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(XProtUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    xProtService = TestBed.inject(XProtService);
    siteService = TestBed.inject(SiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Site query and add missing value', () => {
      const xProt: IXProt = { id: 456 };
      const onNode: ISite = { id: 76936 };
      xProt.onNode = onNode;

      const siteCollection: ISite[] = [{ id: 40635 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [onNode];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ xProt });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(siteCollection, ...additionalSites);
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const xProt: IXProt = { id: 456 };
      const onNode: ISite = { id: 31462 };
      xProt.onNode = onNode;

      activatedRoute.data = of({ xProt });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(xProt));
      expect(comp.sitesSharedCollection).toContain(onNode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XProt>>();
      const xProt = { id: 123 };
      jest.spyOn(xProtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xProt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xProt }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(xProtService.update).toHaveBeenCalledWith(xProt);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XProt>>();
      const xProt = new XProt();
      jest.spyOn(xProtService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xProt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xProt }));
      saveSubject.complete();

      // THEN
      expect(xProtService.create).toHaveBeenCalledWith(xProt);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XProt>>();
      const xProt = { id: 123 };
      jest.spyOn(xProtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xProt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(xProtService.update).toHaveBeenCalledWith(xProt);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSiteById', () => {
      it('Should return tracked Site primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSiteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
