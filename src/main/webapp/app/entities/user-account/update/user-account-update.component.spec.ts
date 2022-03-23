import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserAccountService } from '../service/user-account.service';
import { IUserAccount, UserAccount } from '../user-account.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBusinessUnit } from 'app/entities/business-unit/business-unit.model';
import { BusinessUnitService } from 'app/entities/business-unit/service/business-unit.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

import { UserAccountUpdateComponent } from './user-account-update.component';

describe('UserAccount Management Update Component', () => {
  let comp: UserAccountUpdateComponent;
  let fixture: ComponentFixture<UserAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userAccountService: UserAccountService;
  let userService: UserService;
  let businessUnitService: BusinessUnitService;
  let siteService: SiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserAccountUpdateComponent],
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
      .overrideTemplate(UserAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userAccountService = TestBed.inject(UserAccountService);
    userService = TestBed.inject(UserService);
    businessUnitService = TestBed.inject(BusinessUnitService);
    siteService = TestBed.inject(SiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const userAccount: IUserAccount = { id: 456 };
      const user: IUser = { id: 48980 };
      userAccount.user = user;

      const userCollection: IUser[] = [{ id: 35502 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAccount });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessUnit query and add missing value', () => {
      const userAccount: IUserAccount = { id: 456 };
      const businessUnits: IBusinessUnit[] = [{ id: 1556 }];
      userAccount.businessUnits = businessUnits;

      const businessUnitCollection: IBusinessUnit[] = [{ id: 21826 }];
      jest.spyOn(businessUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: businessUnitCollection })));
      const additionalBusinessUnits = [...businessUnits];
      const expectedCollection: IBusinessUnit[] = [...additionalBusinessUnits, ...businessUnitCollection];
      jest.spyOn(businessUnitService, 'addBusinessUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAccount });
      comp.ngOnInit();

      expect(businessUnitService.query).toHaveBeenCalled();
      expect(businessUnitService.addBusinessUnitToCollectionIfMissing).toHaveBeenCalledWith(
        businessUnitCollection,
        ...additionalBusinessUnits
      );
      expect(comp.businessUnitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Site query and add missing value', () => {
      const userAccount: IUserAccount = { id: 456 };
      const sites: ISite[] = [{ id: 74302 }];
      userAccount.sites = sites;

      const siteCollection: ISite[] = [{ id: 73969 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [...sites];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAccount });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(siteCollection, ...additionalSites);
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userAccount: IUserAccount = { id: 456 };
      const user: IUser = { id: 59705 };
      userAccount.user = user;
      const businessUnits: IBusinessUnit = { id: 21165 };
      userAccount.businessUnits = [businessUnits];
      const sites: ISite = { id: 96835 };
      userAccount.sites = [sites];

      activatedRoute.data = of({ userAccount });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(userAccount));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.businessUnitsSharedCollection).toContain(businessUnits);
      expect(comp.sitesSharedCollection).toContain(sites);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserAccount>>();
      const userAccount = { id: 123 };
      jest.spyOn(userAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAccount }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(userAccountService.update).toHaveBeenCalledWith(userAccount);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserAccount>>();
      const userAccount = new UserAccount();
      jest.spyOn(userAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAccount }));
      saveSubject.complete();

      // THEN
      expect(userAccountService.create).toHaveBeenCalledWith(userAccount);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserAccount>>();
      const userAccount = { id: 123 };
      jest.spyOn(userAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userAccountService.update).toHaveBeenCalledWith(userAccount);
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

  describe('Getting selected relationships', () => {
    describe('getSelectedBusinessUnit', () => {
      it('Should return option if no BusinessUnit is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBusinessUnit(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected BusinessUnit for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBusinessUnit(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this BusinessUnit is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBusinessUnit(option, [selected]);
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
