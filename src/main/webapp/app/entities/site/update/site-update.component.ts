import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISite, Site } from '../site.model';
import { SiteService } from '../service/site.service';
import { IBusinessunit } from 'app/entities/businessunit/businessunit.model';
import { BusinessunitService } from 'app/entities/businessunit/service/businessunit.service';
import { Sitetype } from 'app/entities/enumerations/sitetype.model';
import { Buildstate } from 'app/entities/enumerations/buildstate.model';

@Component({
  selector: 'jhi-site-update',
  templateUrl: './site-update.component.html',
})
export class SiteUpdateComponent implements OnInit {
  isSaving = false;
  sitetypeValues = Object.keys(Sitetype);
  buildstateValues = Object.keys(Buildstate);

  businessunitsSharedCollection: IBusinessunit[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    sitetype: [null, [Validators.required]],
    description: [],
    sitenode: [null, [Validators.required]],
    creationdate: [null, [Validators.required]],
    lastupdated: [null, [Validators.required]],
    buildstate: [],
    buildcount: [],
    buildcomment: [],
    businessunit: [null, Validators.required],
  });

  constructor(
    protected siteService: SiteService,
    protected businessunitService: BusinessunitService,
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

  trackBusinessunitById(index: number, item: IBusinessunit): number {
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
      sitetype: site.sitetype,
      description: site.description,
      sitenode: site.sitenode,
      creationdate: site.creationdate,
      lastupdated: site.lastupdated,
      buildstate: site.buildstate,
      buildcount: site.buildcount,
      buildcomment: site.buildcomment,
      businessunit: site.businessunit,
    });

    this.businessunitsSharedCollection = this.businessunitService.addBusinessunitToCollectionIfMissing(
      this.businessunitsSharedCollection,
      site.businessunit
    );
  }

  protected loadRelationshipsOptions(): void {
    this.businessunitService
      .query()
      .pipe(map((res: HttpResponse<IBusinessunit[]>) => res.body ?? []))
      .pipe(
        map((businessunits: IBusinessunit[]) =>
          this.businessunitService.addBusinessunitToCollectionIfMissing(businessunits, this.editForm.get('businessunit')!.value)
        )
      )
      .subscribe((businessunits: IBusinessunit[]) => (this.businessunitsSharedCollection = businessunits));
  }

  protected createFromForm(): ISite {
    return {
      ...new Site(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      sitetype: this.editForm.get(['sitetype'])!.value,
      description: this.editForm.get(['description'])!.value,
      sitenode: this.editForm.get(['sitenode'])!.value,
      creationdate: this.editForm.get(['creationdate'])!.value,
      lastupdated: this.editForm.get(['lastupdated'])!.value,
      buildstate: this.editForm.get(['buildstate'])!.value,
      buildcount: this.editForm.get(['buildcount'])!.value,
      buildcomment: this.editForm.get(['buildcomment'])!.value,
      businessunit: this.editForm.get(['businessunit'])!.value,
    };
  }
}
