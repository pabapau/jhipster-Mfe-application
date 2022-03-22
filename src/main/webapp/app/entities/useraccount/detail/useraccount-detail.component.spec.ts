import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UseraccountDetailComponent } from './useraccount-detail.component';

describe('Useraccount Management Detail Component', () => {
  let comp: UseraccountDetailComponent;
  let fixture: ComponentFixture<UseraccountDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UseraccountDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ useraccount: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UseraccountDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UseraccountDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load useraccount on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.useraccount).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
