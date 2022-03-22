import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BusinessunitService } from '../service/businessunit.service';
import { IBusinessunit, Businessunit } from '../businessunit.model';

import { BusinessunitUpdateComponent } from './businessunit-update.component';

describe('Businessunit Management Update Component', () => {
  let comp: BusinessunitUpdateComponent;
  let fixture: ComponentFixture<BusinessunitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let businessunitService: BusinessunitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BusinessunitUpdateComponent],
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
      .overrideTemplate(BusinessunitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusinessunitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    businessunitService = TestBed.inject(BusinessunitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const businessunit: IBusinessunit = { id: 456 };

      activatedRoute.data = of({ businessunit });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(businessunit));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Businessunit>>();
      const businessunit = { id: 123 };
      jest.spyOn(businessunitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessunit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessunit }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(businessunitService.update).toHaveBeenCalledWith(businessunit);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Businessunit>>();
      const businessunit = new Businessunit();
      jest.spyOn(businessunitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessunit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessunit }));
      saveSubject.complete();

      // THEN
      expect(businessunitService.create).toHaveBeenCalledWith(businessunit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Businessunit>>();
      const businessunit = { id: 123 };
      jest.spyOn(businessunitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessunit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(businessunitService.update).toHaveBeenCalledWith(businessunit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
