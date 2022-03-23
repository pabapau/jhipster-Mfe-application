import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IXProt } from '../x-prot.model';

@Component({
  selector: 'jhi-x-prot-detail',
  templateUrl: './x-prot-detail.component.html',
})
export class XProtDetailComponent implements OnInit {
  xProt: IXProt | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xProt }) => {
      this.xProt = xProt;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
