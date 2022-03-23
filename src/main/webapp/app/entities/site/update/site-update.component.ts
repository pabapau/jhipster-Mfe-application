import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISite, Site } from '../site.model';
import { SiteService } from '../service/site.service';
import { IBusinessUnit } from 'app/entities/business-unit/business-unit.model';
import { BusinessUnitService } from 'app/entities/business-unit/service/business-unit.service';
import { SiteType } from 'app/entities/enumerations/site-type.model';
import { BuildState } from 'app/entities/enumerations/build-state.model';

@Component({
  selector: 'jhi-site-update',
  templateUrl: './site-update.component.html',
})
export class SiteUpdateComponent implements OnInit {
  isSaving = false;
  siteTypeValues = Object.keys(SiteType);
  buildStateValues = Object.keys(BuildState);

  businessUnitsSharedCollection: IBusinessUnit[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    siteType: [null, [Validators.required]],
    description: [],
    creationDate: [null, [Validators.required]],
    lastUpdated: [null, [Validators.required]],
    buildState: [],
    buildCount: [],
    buildComment: [],
    businessUnit: [null, Validators.required],
  });

  constructor(
    protected siteService: SiteService,
    protected businessUnitService: BusinessUnitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ site }) => {
      this.updateForm(site);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const site = this.createFromForm();
    if (site.id !== undefined) {
      this.subscribeToSaveResponse(this.siteService.update(site));
    } else {
      this.subscribeToSaveResponse(this.siteService.create(site));
    }
  }

  trackBusinessUnitById(index: number, item: IBusinessUnit): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISite>>): void {
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

  protected updateForm(site: ISite): void {
    this.editForm.patchValue({
      id: site.id,
      name: site.name,
      siteType: site.siteType,
      description: site.description,
      creationDate: site.creationDate,
      lastUpdated: site.lastUpdated,
      buildState: site.buildState,
      buildCount: site.buildCount,
      buildComment: site.buildComment,
      businessUnit: site.businessUnit,
    });

    this.businessUnitsSharedCollection = this.businessUnitService.addBusinessUnitToCollectionIfMissing(
      this.businessUnitsSharedCollection,
      site.businessUnit
    );
  }

  protected loadRelationshipsOptions(): void {
    this.businessUnitService
      .query()
      .pipe(map((res: HttpResponse<IBusinessUnit[]>) => res.body ?? []))
      .pipe(
        map((businessUnits: IBusinessUnit[]) =>
          this.businessUnitService.addBusinessUnitToCollectionIfMissing(businessUnits, this.editForm.get('businessUnit')!.value)
        )
      )
      .subscribe((businessUnits: IBusinessUnit[]) => (this.businessUnitsSharedCollection = businessUnits));
  }

  protected createFromForm(): ISite {
    return {
      ...new Site(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      siteType: this.editForm.get(['siteType'])!.value,
      description: this.editForm.get(['description'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value,
      buildState: this.editForm.get(['buildState'])!.value,
      buildCount: this.editForm.get(['buildCount'])!.value,
      buildComment: this.editForm.get(['buildComment'])!.value,
      businessUnit: this.editForm.get(['businessUnit'])!.value,
    };
  }
}
