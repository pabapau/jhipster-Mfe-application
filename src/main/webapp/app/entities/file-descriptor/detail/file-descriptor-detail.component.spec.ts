import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FileDescriptorDetailComponent } from './file-descriptor-detail.component';

describe('FileDescriptor Management Detail Component', () => {
  let comp: FileDescriptorDetailComponent;
  let fixture: ComponentFixture<FileDescriptorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FileDescriptorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fileDescriptor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FileDescriptorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FileDescriptorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fileDescriptor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fileDescriptor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
