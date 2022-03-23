import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileDescriptor } from '../file-descriptor.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { FileDescriptorService } from '../service/file-descriptor.service';
import { FileDescriptorDeleteDialogComponent } from '../delete/file-descriptor-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-file-descriptor',
  templateUrl: './file-descriptor.component.html',
})
export class FileDescriptorComponent implements OnInit {
  fileDescriptors: IFileDescriptor[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected fileDescriptorService: FileDescriptorService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.fileDescriptors = [];
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

    this.fileDescriptorService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IFileDescriptor[]>) => {
          this.isLoading = false;
          this.paginateFileDescriptors(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.fileDescriptors = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFileDescriptor): number {
    return item.id!;
  }

  delete(fileDescriptor: IFileDescriptor): void {
    const modalRef = this.modalService.open(FileDescriptorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fileDescriptor = fileDescriptor;
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

  protected paginateFileDescriptors(data: IFileDescriptor[] | null, headers: HttpHeaders): void {
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
        this.fileDescriptors.push(d);
      }
    }
  }
}
