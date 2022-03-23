import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFileDescriptor } from '../file-descriptor.model';

@Component({
  selector: 'jhi-file-descriptor-detail',
  templateUrl: './file-descriptor-detail.component.html',
})
export class FileDescriptorDetailComponent implements OnInit {
  fileDescriptor: IFileDescriptor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileDescriptor }) => {
      this.fileDescriptor = fileDescriptor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
