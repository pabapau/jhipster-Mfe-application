import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { XProtService } from '../service/x-prot.service';
import { IXProt, XProt } from '../x-prot.model';

import { XProtUpdateComponent } from './x-prot-update.component';

describe('XProt Management Update Component', () => {
  let comp: XProtUpdateComponent;
  let fixture: ComponentFixture<XProtUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let xProtService: XProtService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const xProt: IXProt = { id: 456 };

      activatedRoute.data = of({ xProt });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(xProt));
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
});
