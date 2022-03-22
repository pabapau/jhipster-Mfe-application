import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IXProt, XProt } from '../x-prot.model';
import { XProtService } from '../service/x-prot.service';
import { XProttype } from 'app/entities/enumerations/x-prottype.model';
import { Xrole } from 'app/entities/enumerations/xrole.model';
import { Buildstate } from 'app/entities/enumerations/buildstate.model';

@Component({
  selector: 'jhi-x-prot-update',
  templateUrl: './x-prot-update.component.html',
})
export class XProtUpdateComponent implements OnInit {
  isSaving = false;
  xProttypeValues = Object.keys(XProttype);
  xroleValues = Object.keys(Xrole);
  buildstateValues = Object.keys(Buildstate);

  editForm = this.fb.group({
    id: [],
    xprottype: [null, [Validators.required]],
    xrole: [null, [Validators.required]],
    comment: [],
    accessAddress: [],
    accessServicePoint: [],
    creationdate: [null, [Validators.required]],
    lastupdated: [null, [Validators.required]],
    buildstate: [],
    buildcount: [],
    buildcomment: [],
  });

  constructor(protected xProtService: XProtService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xProt }) => {
      this.updateForm(xProt);
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
      xprottype: xProt.xprottype,
      xrole: xProt.xrole,
      comment: xProt.comment,
      accessAddress: xProt.accessAddress,
      accessServicePoint: xProt.accessServicePoint,
      creationdate: xProt.creationdate,
      lastupdated: xProt.lastupdated,
      buildstate: xProt.buildstate,
      buildcount: xProt.buildcount,
      buildcomment: xProt.buildcomment,
    });
  }

  protected createFromForm(): IXProt {
    return {
      ...new XProt(),
      id: this.editForm.get(['id'])!.value,
      xprottype: this.editForm.get(['xprottype'])!.value,
      xrole: this.editForm.get(['xrole'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      accessAddress: this.editForm.get(['accessAddress'])!.value,
      accessServicePoint: this.editForm.get(['accessServicePoint'])!.value,
      creationdate: this.editForm.get(['creationdate'])!.value,
      lastupdated: this.editForm.get(['lastupdated'])!.value,
      buildstate: this.editForm.get(['buildstate'])!.value,
      buildcount: this.editForm.get(['buildcount'])!.value,
      buildcomment: this.editForm.get(['buildcomment'])!.value,
    };
  }
}
