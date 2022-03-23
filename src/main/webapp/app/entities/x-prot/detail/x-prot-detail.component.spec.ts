import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XProtDetailComponent } from './x-prot-detail.component';

describe('XProt Management Detail Component', () => {
  let comp: XProtDetailComponent;
  let fixture: ComponentFixture<XProtDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [XProtDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ xProt: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(XProtDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(XProtDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load xProt on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.xProt).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
