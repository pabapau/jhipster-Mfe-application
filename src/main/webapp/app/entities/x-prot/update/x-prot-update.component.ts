import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IXProt, XProt } from '../x-prot.model';
import { XProtService } from '../service/x-prot.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { XProtType } from 'app/entities/enumerations/x-prot-type.model';
import { XRole } from 'app/entities/enumerations/x-role.model';
import { BuildState } from 'app/entities/enumerations/build-state.model';

@Component({
  selector: 'jhi-x-prot-update',
  templateUrl: './x-prot-update.component.html',
})
export class XProtUpdateComponent implements OnInit {
  isSaving = false;
  xProtTypeValues = Object.keys(XProtType);
  xRoleValues = Object.keys(XRole);
  buildStateValues = Object.keys(BuildState);

  sitesSharedCollection: ISite[] = [];

  editForm = this.fb.group({
    id: [],
    xprotType: [null, [Validators.required]],
    xRole: [null, [Validators.required]],
    comment: [],
    accessAddress: [],
    accessServicePoint: [],
    creationDate: [null, [Validators.required]],
    lastUpdated: [null, [Validators.required]],
    buildState: [],
    buildCount: [],
    buildComment: [],
    onNode: [],
  });

  constructor(
    protected xProtService: XProtService,
    protected siteService: SiteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xProt }) => {
      this.updateForm(xProt);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const xProt = this.createFromForm();
    if (xProt.id !== undefined) {
      this.subscribeToSaveResponse(this.xProtService.update(xProt));
    } else {
      this.subscribeToSaveResponse(this.xProtService.create(xProt));
    }
  }

  trackSiteById(index: number, item: ISite): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IXProt>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(xProt: IXProt): void {
    this.editForm.patchValue({
      id: xProt.id,
      xprotType: xProt.xprotType,
      xRole: xProt.xRole,
      comment: xProt.comment,
      accessAddress: xProt.accessAddress,
      accessServicePoint: xProt.accessServicePoint,
      creationDate: xProt.creationDate,
      lastUpdated: xProt.lastUpdated,
      buildState: xProt.buildState,
      buildCount: xProt.buildCount,
      buildComment: xProt.buildComment,
      onNode: xProt.onNode,
    });

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing(this.sitesSharedCollection, xProt.onNode);
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing(sites, this.editForm.get('onNode')!.value)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }

  protected createFromForm(): IXProt {
    return {
      ...new XProt(),
      id: this.editForm.get(['id'])!.value,
      xprotType: this.editForm.get(['xprotType'])!.value,
      xRole: this.editForm.get(['xRole'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      accessAddress: this.editForm.get(['accessAddress'])!.value,
      accessServicePoint: this.editForm.get(['accessServicePoint'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value,
      buildState: this.editForm.get(['buildState'])!.value,
      buildCount: this.editForm.get(['buildCount'])!.value,
      buildComment: this.editForm.get(['buildComment'])!.value,
      onNode: this.editForm.get(['onNode'])!.value,
    };
  }
}
