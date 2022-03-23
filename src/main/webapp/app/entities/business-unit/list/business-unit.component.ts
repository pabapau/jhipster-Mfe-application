import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusinessUnit } from '../business-unit.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { BusinessUnitService } from '../service/business-unit.service';
import { BusinessUnitDeleteDialogComponent } from '../delete/business-unit-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-business-unit',
  templateUrl: './business-unit.component.html',
})
export class BusinessUnitComponent implements OnInit {
  businessUnits: IBusinessUnit[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected businessUnitService: BusinessUnitService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.businessUnits = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.businessUnitService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IBusinessUnit[]>) => {
          this.isLoading = false;
          this.paginateBusinessUnits(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.businessUnits = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBusinessUnit): number {
    return item.id!;
  }

  delete(businessUnit: IBusinessUnit): void {
    const modalRef = this.modalService.open(BusinessUnitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.businessUnit = businessUnit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateBusinessUnits(data: IBusinessUnit[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.businessUnits.push(d);
      }
    }
  }
}
