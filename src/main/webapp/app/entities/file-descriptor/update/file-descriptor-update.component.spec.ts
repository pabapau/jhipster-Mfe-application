import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FileDescriptorService } from '../service/file-descriptor.service';
import { IFileDescriptor, FileDescriptor } from '../file-descriptor.model';
import { IFlow } from 'app/entities/flow/flow.model';
import { FlowService } from 'app/entities/flow/service/flow.service';

import { FileDescriptorUpdateComponent } from './file-descriptor-update.component';

describe('FileDescriptor Management Update Component', () => {
  let comp: FileDescriptorUpdateComponent;
  let fixture: ComponentFixture<FileDescriptorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fileDescriptorService: FileDescriptorService;
  let flowService: FlowService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FileDescriptorUpdateComponent],
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
      .overrideTemplate(FileDescriptorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FileDescriptorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fileDescriptorService = TestBed.inject(FileDescriptorService);
    flowService = TestBed.inject(FlowService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call flow query and add missing value', () => {
      const fileDescriptor: IFileDescriptor = { id: 456 };
      const flow: IFlow = { id: 24426 };
      fileDescriptor.flow = flow;

      const flowCollection: IFlow[] = [{ id: 30896 }];
      jest.spyOn(flowService, 'query').mockReturnValue(of(new HttpResponse({ body: flowCollection })));
      const expectedCollection: IFlow[] = [flow, ...flowCollection];
      jest.spyOn(flowService, 'addFlowToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fileDescriptor });
      comp.ngOnInit();

      expect(flowService.query).toHaveBeenCalled();
      expect(flowService.addFlowToCollectionIfMissing).toHaveBeenCalledWith(flowCollection, flow);
      expect(comp.flowsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fileDescriptor: IFileDescriptor = { id: 456 };
      const flow: IFlow = { id: 12225 };
      fileDescriptor.flow = flow;

      activatedRoute.data = of({ fileDescriptor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fileDescriptor));
      expect(comp.flowsCollection).toContain(flow);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FileDescriptor>>();
      const fileDescriptor = { id: 123 };
      jest.spyOn(fileDescriptorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileDescriptor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileDescriptor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fileDescriptorService.update).toHaveBeenCalledWith(fileDescriptor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FileDescriptor>>();
      const fileDescriptor = new FileDescriptor();
      jest.spyOn(fileDescriptorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileDescriptor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileDescriptor }));
      saveSubject.complete();

      // THEN
      expect(fileDescriptorService.create).toHaveBeenCalledWith(fileDescriptor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FileDescriptor>>();
      const fileDescriptor = { id: 123 };
      jest.spyOn(fileDescriptorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileDescriptor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fileDescriptorService.update).toHaveBeenCalledWith(fileDescriptor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFlowById', () => {
      it('Should return tracked Flow primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFlowById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
