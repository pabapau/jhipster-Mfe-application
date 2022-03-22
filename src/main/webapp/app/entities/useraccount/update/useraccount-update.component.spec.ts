import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UseraccountService } from '../service/useraccount.service';
import { IUseraccount, Useraccount } from '../useraccount.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBusinessunit } from 'app/entities/businessunit/businessunit.model';
import { BusinessunitService } from 'app/entities/businessunit/service/businessunit.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

import { UseraccountUpdateComponent } from './useraccount-update.component';

describe('Useraccount Management Update Component', () => {
  let comp: UseraccountUpdateComponent;
  let fixture: ComponentFixture<UseraccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let useraccountService: UseraccountService;
  let userService: UserService;
  let businessunitService: BusinessunitService;
  let siteService: SiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UseraccountUpdateComponent],
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
      .overrideTemplate(UseraccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UseraccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    useraccountService = TestBed.inject(UseraccountService);
    userService = TestBed.inject(UserService);
    businessunitService = TestBed.inject(BusinessunitService);
    siteService = TestBed.inject(SiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const useraccount: IUseraccount = { id: 456 };
      const user: IUser = { id: 68404 };
      useraccount.user = user;

      const userCollection: IUser[] = [{ id: 56592 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ useraccount });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Businessunit query and add missing value', () => {
      const useraccount: IUseraccount = { id: 456 };
      const businessunits: IBusinessunit[] = [{ id: 93082 }];
      useraccount.businessunits = businessunits;

      const businessunitCollection: IBusinessunit[] = [{ id: 91265 }];
      jest.spyOn(businessunitService, 'query').mockReturnValue(of(new HttpResponse({ body: businessunitCollection })));
      const additionalBusinessunits = [...businessunits];
      const expectedCollection: IBusinessunit[] = [...additionalBusinessunits, ...businessunitCollection];
      jest.spyOn(businessunitService, 'addBusinessunitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ useraccount });
      comp.ngOnInit();

      expect(businessunitService.query).toHaveBeenCalled();
      expect(businessunitService.addBusinessunitToCollectionIfMissing).toHaveBeenCalledWith(
        businessunitCollection,
        ...additionalBusinessunits
      );
      expect(comp.businessunitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Site query and add missing value', () => {
      const useraccount: IUseraccount = { id: 456 };
      const sites: ISite[] = [{ id: 37964 }];
      useraccount.sites = sites;

      const siteCollection: ISite[] = [{ id: 56539 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [...sites];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ useraccount });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(siteCollection, ...additionalSites);
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const useraccount: IUseraccount = { id: 456 };
      const user: IUser = { id: 41724 };
      useraccount.user = user;
      const businessunits: IBusinessunit = { id: 23436 };
      useraccount.businessunits = [businessunits];
      const sites: ISite = { id: 37422 };
      useraccount.sites = [sites];

      activatedRoute.data = of({ useraccount });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(useraccount));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.businessunitsSharedCollection).toContain(businessunits);
      expect(comp.sitesSharedCollection).toContain(sites);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Useraccount>>();
      const useraccount = { id: 123 };
      jest.spyOn(useraccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ useraccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: useraccount }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(useraccountService.update).toHaveBeenCalledWith(useraccount);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Useraccount>>();
      const useraccount = new Useraccount();
      jest.spyOn(useraccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ useraccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: useraccount }));
      saveSubject.complete();

      // THEN
      expect(useraccountService.create).toHaveBeenCalledWith(useraccount);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Useraccount>>();
      const useraccount = { id: 123 };
      jest.spyOn(useraccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ useraccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(useraccountService.update).toHaveBeenCalledWith(useraccount);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

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

  describe('Getting selected relationships', () => {
    describe('getSelectedBusinessunit', () => {
      it('Should return option if no Businessunit is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBusinessunit(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Businessunit for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBusinessunit(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Businessunit is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBusinessunit(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedSite', () => {
      it('Should return option if no Site is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedSite(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Site for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedSite(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Site is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedSite(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
