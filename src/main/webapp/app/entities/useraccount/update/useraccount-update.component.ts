import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUseraccount, Useraccount } from '../useraccount.model';
import { UseraccountService } from '../service/useraccount.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBusinessunit } from 'app/entities/businessunit/businessunit.model';
import { BusinessunitService } from 'app/entities/businessunit/service/businessunit.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { Accounttype } from 'app/entities/enumerations/accounttype.model';

@Component({
  selector: 'jhi-useraccount-update',
  templateUrl: './useraccount-update.component.html',
})
export class UseraccountUpdateComponent implements OnInit {
  isSaving = false;
  accounttypeValues = Object.keys(Accounttype);

  usersSharedCollection: IUser[] = [];
  businessunitsSharedCollection: IBusinessunit[] = [];
  sitesSharedCollection: ISite[] = [];

  editForm = this.fb.group({
    id: [],
    accounttype: [null, [Validators.required]],
    comment: [],
    creationdate: [null, [Validators.required]],
    lastupdated: [null, [Validators.required]],
    user: [null, Validators.required],
    businessunits: [],
    sites: [],
  });

  constructor(
    protected useraccountService: UseraccountService,
    protected userService: UserService,
    protected businessunitService: BusinessunitService,
    protected siteService: SiteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ useraccount }) => {
      this.updateForm(useraccount);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const useraccount = this.createFromForm();
    if (useraccount.id !== undefined) {
      this.subscribeToSaveResponse(this.useraccountService.update(useraccount));
    } else {
      this.subscribeToSaveResponse(this.useraccountService.create(useraccount));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackBusinessunitById(index: number, item: IBusinessunit): number {
    return item.id!;
  }

  trackSiteById(index: number, item: ISite): number {
    return item.id!;
  }

  getSelectedBusinessunit(option: IBusinessunit, selectedVals?: IBusinessunit[]): IBusinessunit {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedSite(option: ISite, selectedVals?: ISite[]): ISite {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUseraccount>>): void {
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

  protected updateForm(useraccount: IUseraccount): void {
    this.editForm.patchValue({
      id: useraccount.id,
      accounttype: useraccount.accounttype,
      comment: useraccount.comment,
      creationdate: useraccount.creationdate,
      lastupdated: useraccount.lastupdated,
      user: useraccount.user,
      businessunits: useraccount.businessunits,
      sites: useraccount.sites,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, useraccount.user);
    this.businessunitsSharedCollection = this.businessunitService.addBusinessunitToCollectionIfMissing(
      this.businessunitsSharedCollection,
      ...(useraccount.businessunits ?? [])
    );
    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing(this.sitesSharedCollection, ...(useraccount.sites ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.businessunitService
      .query()
      .pipe(map((res: HttpResponse<IBusinessunit[]>) => res.body ?? []))
      .pipe(
        map((businessunits: IBusinessunit[]) =>
          this.businessunitService.addBusinessunitToCollectionIfMissing(businessunits, ...(this.editForm.get('businessunits')!.value ?? []))
        )
      )
      .subscribe((businessunits: IBusinessunit[]) => (this.businessunitsSharedCollection = businessunits));

    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing(sites, ...(this.editForm.get('sites')!.value ?? []))))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }

  protected createFromForm(): IUseraccount {
    return {
      ...new Useraccount(),
      id: this.editForm.get(['id'])!.value,
      accounttype: this.editForm.get(['accounttype'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      creationdate: this.editForm.get(['creationdate'])!.value,
      lastupdated: this.editForm.get(['lastupdated'])!.value,
      user: this.editForm.get(['user'])!.value,
      businessunits: this.editForm.get(['businessunits'])!.value,
      sites: this.editForm.get(['sites'])!.value,
    };
  }
}
