package com.paba.mfe.web.rest;

import com.paba.mfe.domain.UserAccount;
import com.paba.mfe.repository.UserAccountRepository;
import com.paba.mfe.service.UserAccountService;
import com.paba.mfe.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.paba.mfe.domain.UserAccount}.
 */
@RestController
@RequestMapping("/api")
public class UserAccountResource {

    private final Logger log = LoggerFactory.getLogger(UserAccountResource.class);

    private static final String ENTITY_NAME = "userAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAccountService userAccountService;

    private final UserAccountRepository userAccountRepository;

    public UserAccountResource(UserAccountService userAccountService, UserAccountRepository userAccountRepository) {
        this.userAccountService = userAccountService;
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * {@code POST  /user-accounts} : Create a new userAccount.
     *
     * @param userAccount the userAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAccount, or with status {@code 400 (Bad Request)} if the userAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-accounts")
    public ResponseEntity<UserAccount> createUserAccount(@Valid @RequestBody UserAccount userAccount) throws URISyntaxException {
        log.debug("REST request to save UserAccount : {}", userAccount);
        if (userAccount.getId() != null) {
            throw new BadRequestAlertException("A new userAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAccount result = userAccountService.save(userAccount);
        return ResponseEntity
            .created(new URI("/api/user-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-accounts/:id} : Updates an existing userAccount.
     *
     * @param id the id of the userAccount to save.
     * @param userAccount the userAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccount,
     * or with status {@code 400 (Bad Request)} if the userAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-accounts/{id}")
    public ResponseEntity<UserAccount> updateUserAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAccount userAccount
    ) throws URISyntaxException {
        log.debug("REST request to update UserAccount : {}, {}", id, userAccount);
        if (userAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAccount result = userAccountService.save(userAccount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-accounts/:id} : Partial updates given fields of an existing userAccount, field will ignore if it is null
     *
     * @param id the id of the userAccount to save.
     * @param userAccount the userAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccount,
     * or with status {@code 400 (Bad Request)} if the userAccount is not valid,
     * or with status {@code 404 (Not Found)} if the userAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAccount> partialUpdateUserAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAccount userAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAccount partially : {}, {}", id, userAccount);
        if (userAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAccount> result = userAccountService.partialUpdate(userAccount);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /user-accounts} : get all the userAccounts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAccounts in body.
     */
    @GetMapping("/user-accounts")
    public ResponseEntity<List<UserAccount>> getAllUserAccounts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of UserAccounts");
        Page<UserAccount> page;
        if (eagerload) {
            page = userAccountService.findAllWithEagerRelationships(pageable);
        } else {
            page = userAccountService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-accounts/:id} : get the "id" userAccount.
     *
     * @param id the id of the userAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-accounts/{id}")
    public ResponseEntity<UserAccount> getUserAccount(@PathVariable Long id) {
        log.debug("REST request to get UserAccount : {}", id);
        Optional<UserAccount> userAccount = userAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAccount);
    }

    /**
     * {@code DELETE  /user-accounts/:id} : delete the "id" userAccount.
     *
     * @param id the id of the userAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-accounts/{id}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable Long id) {
        log.debug("REST request to delete UserAccount : {}", id);
        userAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
