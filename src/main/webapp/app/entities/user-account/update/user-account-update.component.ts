import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUserAccount, UserAccount } from '../user-account.model';
import { UserAccountService } from '../service/user-account.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBusinessUnit } from 'app/entities/business-unit/business-unit.model';
import { BusinessUnitService } from 'app/entities/business-unit/service/business-unit.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { AccountType } from 'app/entities/enumerations/account-type.model';

@Component({
  selector: 'jhi-user-account-update',
  templateUrl: './user-account-update.component.html',
})
export class UserAccountUpdateComponent implements OnInit {
  isSaving = false;
  accountTypeValues = Object.keys(AccountType);

  usersSharedCollection: IUser[] = [];
  businessUnitsSharedCollection: IBusinessUnit[] = [];
  sitesSharedCollection: ISite[] = [];

  editForm = this.fb.group({
    id: [],
    accountType: [null, [Validators.required]],
    comment: [],
    creationDate: [null, [Validators.required]],
    lastUpdated: [null, [Validators.required]],
    user: [null, Validators.required],
    businessUnits: [],
    sites: [],
  });

  constructor(
    protected userAccountService: UserAccountService,
    protected userService: UserService,
    protected businessUnitService: BusinessUnitService,
    protected siteService: SiteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAccount }) => {
      this.updateForm(userAccount);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAccount = this.createFromForm();
    if (userAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.userAccountService.update(userAccount));
    } else {
      this.subscribeToSaveResponse(this.userAccountService.create(userAccount));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackBusinessUnitById(index: number, item: IBusinessUnit): number {
    return item.id!;
  }

  trackSiteById(index: number, item: ISite): number {
    return item.id!;
  }

  getSelectedBusinessUnit(option: IBusinessUnit, selectedVals?: IBusinessUnit[]): IBusinessUnit {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAccount>>): void {
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

  protected updateForm(userAccount: IUserAccount): void {
    this.editForm.patchValue({
      id: userAccount.id,
      accountType: userAccount.accountType,
      comment: userAccount.comment,
      creationDate: userAccount.creationDate,
      lastUpdated: userAccount.lastUpdated,
      user: userAccount.user,
      businessUnits: userAccount.businessUnits,
      sites: userAccount.sites,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, userAccount.user);
    this.businessUnitsSharedCollection = this.businessUnitService.addBusinessUnitToCollectionIfMissing(
      this.businessUnitsSharedCollection,
      ...(userAccount.businessUnits ?? [])
    );
    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing(this.sitesSharedCollection, ...(userAccount.sites ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.businessUnitService
      .query()
      .pipe(map((res: HttpResponse<IBusinessUnit[]>) => res.body ?? []))
      .pipe(
        map((businessUnits: IBusinessUnit[]) =>
          this.businessUnitService.addBusinessUnitToCollectionIfMissing(businessUnits, ...(this.editForm.get('businessUnits')!.value ?? []))
        )
      )
      .subscribe((businessUnits: IBusinessUnit[]) => (this.businessUnitsSharedCollection = businessUnits));

    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing(sites, ...(this.editForm.get('sites')!.value ?? []))))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }

  protected createFromForm(): IUserAccount {
    return {
      ...new UserAccount(),
      id: this.editForm.get(['id'])!.value,
      accountType: this.editForm.get(['accountType'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value,
      user: this.editForm.get(['user'])!.value,
      businessUnits: this.editForm.get(['businessUnits'])!.value,
      sites: this.editForm.get(['sites'])!.value,
    };
  }
}
