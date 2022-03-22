package com.paba.mfe.web.rest;

import com.paba.mfe.domain.XProt;
import com.paba.mfe.repository.XProtRepository;
import com.paba.mfe.service.XProtService;
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
 * REST controller for managing {@link com.paba.mfe.domain.XProt}.
 */
@RestController
@RequestMapping("/api")
public class XProtResource {

    private final Logger log = LoggerFactory.getLogger(XProtResource.class);

    private static final String ENTITY_NAME = "xProt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final XProtService xProtService;

    private final XProtRepository xProtRepository;

    public XProtResource(XProtService xProtService, XProtRepository xProtRepository) {
        this.xProtService = xProtService;
        this.xProtRepository = xProtRepository;
    }

    /**
     * {@code POST  /x-prots} : Create a new xProt.
     *
     * @param xProt the xProt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new xProt, or with status {@code 400 (Bad Request)} if the xProt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/x-prots")
    public ResponseEntity<XProt> createXProt(@Valid @RequestBody XProt xProt) throws URISyntaxException {
        log.debug("REST request to save XProt : {}", xProt);
        if (xProt.getId() != null) {
            throw new BadRequestAlertException("A new xProt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        XProt result = xProtService.save(xProt);
        return ResponseEntity
            .created(new URI("/api/x-prots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /x-prots/:id} : Updates an existing xProt.
     *
     * @param id the id of the xProt to save.
     * @param xProt the xProt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xProt,
     * or with status {@code 400 (Bad Request)} if the xProt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the xProt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/x-prots/{id}")
    public ResponseEntity<XProt> updateXProt(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody XProt xProt)
        throws URISyntaxException {
        log.debug("REST request to update XProt : {}, {}", id, xProt);
        if (xProt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xProt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xProtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        XProt result = xProtService.save(xProt);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, xProt.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /x-prots/:id} : Partial updates given fields of an existing xProt, field will ignore if it is null
     *
     * @param id the id of the xProt to save.
     * @param xProt the xProt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xProt,
     * or with status {@code 400 (Bad Request)} if the xProt is not valid,
     * or with status {@code 404 (Not Found)} if the xProt is not found,
     * or with status {@code 500 (Internal Server Error)} if the xProt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/x-prots/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<XProt> partialUpdateXProt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody XProt xProt
    ) throws URISyntaxException {
        log.debug("REST request to partial update XProt partially : {}, {}", id, xProt);
        if (xProt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xProt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xProtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<XProt> result = xProtService.partialUpdate(xProt);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, xProt.getId().toString())
        );
    }

    /**
     * {@code GET  /x-prots} : get all the xProts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of xProts in body.
     */
    @GetMapping("/x-prots")
    public ResponseEntity<List<XProt>> getAllXProts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of XProts");
        Page<XProt> page = xProtService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /x-prots/:id} : get the "id" xProt.
     *
     * @param id the id of the xProt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the xProt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/x-prots/{id}")
    public ResponseEntity<XProt> getXProt(@PathVariable Long id) {
        log.debug("REST request to get XProt : {}", id);
        Optional<XProt> xProt = xProtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(xProt);
    }

    /**
     * {@code DELETE  /x-prots/:id} : delete the "id" xProt.
     *
     * @param id the id of the xProt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/x-prots/{id}")
    public ResponseEntity<Void> deleteXProt(@PathVariable Long id) {
        log.debug("REST request to delete XProt : {}", id);
        xProtService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
