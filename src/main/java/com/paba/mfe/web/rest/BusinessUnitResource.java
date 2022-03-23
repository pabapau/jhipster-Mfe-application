package com.paba.mfe.web.rest;

import com.paba.mfe.domain.BusinessUnit;
import com.paba.mfe.repository.BusinessUnitRepository;
import com.paba.mfe.service.BusinessUnitService;
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
 * REST controller for managing {@link com.paba.mfe.domain.BusinessUnit}.
 */
@RestController
@RequestMapping("/api")
public class BusinessUnitResource {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitResource.class);

    private static final String ENTITY_NAME = "businessUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessUnitService businessUnitService;

    private final BusinessUnitRepository businessUnitRepository;

    public BusinessUnitResource(BusinessUnitService businessUnitService, BusinessUnitRepository businessUnitRepository) {
        this.businessUnitService = businessUnitService;
        this.businessUnitRepository = businessUnitRepository;
    }

    /**
     * {@code POST  /business-units} : Create a new businessUnit.
     *
     * @param businessUnit the businessUnit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessUnit, or with status {@code 400 (Bad Request)} if the businessUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-units")
    public ResponseEntity<BusinessUnit> createBusinessUnit(@Valid @RequestBody BusinessUnit businessUnit) throws URISyntaxException {
        log.debug("REST request to save BusinessUnit : {}", businessUnit);
        if (businessUnit.getId() != null) {
            throw new BadRequestAlertException("A new businessUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessUnit result = businessUnitService.save(businessUnit);
        return ResponseEntity
            .created(new URI("/api/business-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-units/:id} : Updates an existing businessUnit.
     *
     * @param id the id of the businessUnit to save.
     * @param businessUnit the businessUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessUnit,
     * or with status {@code 400 (Bad Request)} if the businessUnit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-units/{id}")
    public ResponseEntity<BusinessUnit> updateBusinessUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessUnit businessUnit
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessUnit : {}, {}", id, businessUnit);
        if (businessUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessUnit result = businessUnitService.save(businessUnit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessUnit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-units/:id} : Partial updates given fields of an existing businessUnit, field will ignore if it is null
     *
     * @param id the id of the businessUnit to save.
     * @param businessUnit the businessUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessUnit,
     * or with status {@code 400 (Bad Request)} if the businessUnit is not valid,
     * or with status {@code 404 (Not Found)} if the businessUnit is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/business-units/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessUnit> partialUpdateBusinessUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessUnit businessUnit
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessUnit partially : {}, {}", id, businessUnit);
        if (businessUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessUnit> result = businessUnitService.partialUpdate(businessUnit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessUnit.getId().toString())
        );
    }

    /**
     * {@code GET  /business-units} : get all the businessUnits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessUnits in body.
     */
    @GetMapping("/business-units")
    public ResponseEntity<List<BusinessUnit>> getAllBusinessUnits(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of BusinessUnits");
        Page<BusinessUnit> page = businessUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-units/:id} : get the "id" businessUnit.
     *
     * @param id the id of the businessUnit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessUnit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-units/{id}")
    public ResponseEntity<BusinessUnit> getBusinessUnit(@PathVariable Long id) {
        log.debug("REST request to get BusinessUnit : {}", id);
        Optional<BusinessUnit> businessUnit = businessUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessUnit);
    }

    /**
     * {@code DELETE  /business-units/:id} : delete the "id" businessUnit.
     *
     * @param id the id of the businessUnit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-units/{id}")
    public ResponseEntity<Void> deleteBusinessUnit(@PathVariable Long id) {
        log.debug("REST request to delete BusinessUnit : {}", id);
        businessUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
