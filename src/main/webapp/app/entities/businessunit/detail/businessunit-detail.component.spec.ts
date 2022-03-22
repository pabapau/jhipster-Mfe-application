import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BusinessunitDetailComponent } from './businessunit-detail.component';

describe('Businessunit Management Detail Component', () => {
  let comp: BusinessunitDetailComponent;
  let fixture: ComponentFixture<BusinessunitDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BusinessunitDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ businessunit: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BusinessunitDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BusinessunitDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load businessunit on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.businessunit).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
