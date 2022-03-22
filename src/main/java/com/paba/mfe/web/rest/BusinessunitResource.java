package com.paba.mfe.web.rest;

import com.paba.mfe.domain.Businessunit;
import com.paba.mfe.repository.BusinessunitRepository;
import com.paba.mfe.service.BusinessunitService;
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
 * REST controller for managing {@link com.paba.mfe.domain.Businessunit}.
 */
@RestController
@RequestMapping("/api")
public class BusinessunitResource {

    private final Logger log = LoggerFactory.getLogger(BusinessunitResource.class);

    private static final String ENTITY_NAME = "businessunit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessunitService businessunitService;

    private final BusinessunitRepository businessunitRepository;

    public BusinessunitResource(BusinessunitService businessunitService, BusinessunitRepository businessunitRepository) {
        this.businessunitService = businessunitService;
        this.businessunitRepository = businessunitRepository;
    }

    /**
     * {@code POST  /businessunits} : Create a new businessunit.
     *
     * @param businessunit the businessunit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessunit, or with status {@code 400 (Bad Request)} if the businessunit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/businessunits")
    public ResponseEntity<Businessunit> createBusinessunit(@Valid @RequestBody Businessunit businessunit) throws URISyntaxException {
        log.debug("REST request to save Businessunit : {}", businessunit);
        if (businessunit.getId() != null) {
            throw new BadRequestAlertException("A new businessunit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Businessunit result = businessunitService.save(businessunit);
        return ResponseEntity
            .created(new URI("/api/businessunits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /businessunits/:id} : Updates an existing businessunit.
     *
     * @param id the id of the businessunit to save.
     * @param businessunit the businessunit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessunit,
     * or with status {@code 400 (Bad Request)} if the businessunit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessunit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/businessunits/{id}")
    public ResponseEntity<Businessunit> updateBusinessunit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Businessunit businessunit
    ) throws URISyntaxException {
        log.debug("REST request to update Businessunit : {}, {}", id, businessunit);
        if (businessunit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessunit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessunitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Businessunit result = businessunitService.save(businessunit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessunit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /businessunits/:id} : Partial updates given fields of an existing businessunit, field will ignore if it is null
     *
     * @param id the id of the businessunit to save.
     * @param businessunit the businessunit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessunit,
     * or with status {@code 400 (Bad Request)} if the businessunit is not valid,
     * or with status {@code 404 (Not Found)} if the businessunit is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessunit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/businessunits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Businessunit> partialUpdateBusinessunit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Businessunit businessunit
    ) throws URISyntaxException {
        log.debug("REST request to partial update Businessunit partially : {}, {}", id, businessunit);
        if (businessunit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessunit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessunitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Businessunit> result = businessunitService.partialUpdate(businessunit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessunit.getId().toString())
        );
    }

    /**
     * {@code GET  /businessunits} : get all the businessunits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessunits in body.
     */
    @GetMapping("/businessunits")
    public ResponseEntity<List<Businessunit>> getAllBusinessunits(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Businessunits");
        Page<Businessunit> page = businessunitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /businessunits/:id} : get the "id" businessunit.
     *
     * @param id the id of the businessunit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessunit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/businessunits/{id}")
    public ResponseEntity<Businessunit> getBusinessunit(@PathVariable Long id) {
        log.debug("REST request to get Businessunit : {}", id);
        Optional<Businessunit> businessunit = businessunitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessunit);
    }

    /**
     * {@code DELETE  /businessunits/:id} : delete the "id" businessunit.
     *
     * @param id the id of the businessunit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/businessunits/{id}")
    public ResponseEntity<Void> deleteBusinessunit(@PathVariable Long id) {
        log.debug("REST request to delete Businessunit : {}", id);
        businessunitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
