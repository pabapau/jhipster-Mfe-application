import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBusinessunit, Businessunit } from '../businessunit.model';
import { BusinessunitService } from '../service/businessunit.service';

@Component({
  selector: 'jhi-businessunit-update',
  templateUrl: './businessunit-update.component.html',
})
export class BusinessunitUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    name: [null, [Validators.required]],
    description: [],
  });

  constructor(protected businessunitService: BusinessunitService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ businessunit }) => {
      this.updateForm(businessunit);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const businessunit = this.createFromForm();
    if (businessunit.id !== undefined) {
      this.subscribeToSaveResponse(this.businessunitService.update(businessunit));
    } else {
      this.subscribeToSaveResponse(this.businessunitService.create(businessunit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusinessunit>>): void {
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

  protected updateForm(businessunit: IBusinessunit): void {
    this.editForm.patchValue({
      id: businessunit.id,
      code: businessunit.code,
      name: businessunit.name,
      description: businessunit.description,
    });
  }

  protected createFromForm(): IBusinessunit {
    return {
      ...new Businessunit(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
