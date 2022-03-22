import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBusinessunit } from '../businessunit.model';

@Component({
  selector: 'jhi-businessunit-detail',
  templateUrl: './businessunit-detail.component.html',
})
export class BusinessunitDetailComponent implements OnInit {
  businessunit: IBusinessunit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ businessunit }) => {
      this.businessunit = businessunit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
