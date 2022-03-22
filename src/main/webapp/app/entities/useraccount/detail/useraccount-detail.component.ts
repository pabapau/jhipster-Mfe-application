import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUseraccount } from '../useraccount.model';

@Component({
  selector: 'jhi-useraccount-detail',
  templateUrl: './useraccount-detail.component.html',
})
export class UseraccountDetailComponent implements OnInit {
  useraccount: IUseraccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ useraccount }) => {
      this.useraccount = useraccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
