package com.paba.mfe.web.rest;

import com.paba.mfe.domain.Useraccount;
import com.paba.mfe.repository.UseraccountRepository;
import com.paba.mfe.service.UseraccountService;
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
 * REST controller for managing {@link com.paba.mfe.domain.Useraccount}.
 */
@RestController
@RequestMapping("/api")
public class UseraccountResource {

    private final Logger log = LoggerFactory.getLogger(UseraccountResource.class);

    private static final String ENTITY_NAME = "useraccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UseraccountService useraccountService;

    private final UseraccountRepository useraccountRepository;

    public UseraccountResource(UseraccountService useraccountService, UseraccountRepository useraccountRepository) {
        this.useraccountService = useraccountService;
        this.useraccountRepository = useraccountRepository;
    }

    /**
     * {@code POST  /useraccounts} : Create a new useraccount.
     *
     * @param useraccount the useraccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new useraccount, or with status {@code 400 (Bad Request)} if the useraccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/useraccounts")
    public ResponseEntity<Useraccount> createUseraccount(@Valid @RequestBody Useraccount useraccount) throws URISyntaxException {
        log.debug("REST request to save Useraccount : {}", useraccount);
        if (useraccount.getId() != null) {
            throw new BadRequestAlertException("A new useraccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Useraccount result = useraccountService.save(useraccount);
        return ResponseEntity
            .created(new URI("/api/useraccounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /useraccounts/:id} : Updates an existing useraccount.
     *
     * @param id the id of the useraccount to save.
     * @param useraccount the useraccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated useraccount,
     * or with status {@code 400 (Bad Request)} if the useraccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the useraccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/useraccounts/{id}")
    public ResponseEntity<Useraccount> updateUseraccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Useraccount useraccount
    ) throws URISyntaxException {
        log.debug("REST request to update Useraccount : {}, {}", id, useraccount);
        if (useraccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, useraccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!useraccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Useraccount result = useraccountService.save(useraccount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, useraccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /useraccounts/:id} : Partial updates given fields of an existing useraccount, field will ignore if it is null
     *
     * @param id the id of the useraccount to save.
     * @param useraccount the useraccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated useraccount,
     * or with status {@code 400 (Bad Request)} if the useraccount is not valid,
     * or with status {@code 404 (Not Found)} if the useraccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the useraccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/useraccounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Useraccount> partialUpdateUseraccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Useraccount useraccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update Useraccount partially : {}, {}", id, useraccount);
        if (useraccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, useraccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!useraccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Useraccount> result = useraccountService.partialUpdate(useraccount);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, useraccount.getId().toString())
        );
    }

    /**
     * {@code GET  /useraccounts} : get all the useraccounts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of useraccounts in body.
     */
    @GetMapping("/useraccounts")
    public ResponseEntity<List<Useraccount>> getAllUseraccounts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Useraccounts");
        Page<Useraccount> page;
        if (eagerload) {
            page = useraccountService.findAllWithEagerRelationships(pageable);
        } else {
            page = useraccountService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /useraccounts/:id} : get the "id" useraccount.
     *
     * @param id the id of the useraccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the useraccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/useraccounts/{id}")
    public ResponseEntity<Useraccount> getUseraccount(@PathVariable Long id) {
        log.debug("REST request to get Useraccount : {}", id);
        Optional<Useraccount> useraccount = useraccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(useraccount);
    }

    /**
     * {@code DELETE  /useraccounts/:id} : delete the "id" useraccount.
     *
     * @param id the id of the useraccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/useraccounts/{id}")
    public ResponseEntity<Void> deleteUseraccount(@PathVariable Long id) {
        log.debug("REST request to delete Useraccount : {}", id);
        useraccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
