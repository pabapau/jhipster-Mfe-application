package com.paba.mfe.web.rest;

import com.paba.mfe.domain.FileDescriptor;
import com.paba.mfe.repository.FileDescriptorRepository;
import com.paba.mfe.service.FileDescriptorService;
import com.paba.mfe.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.paba.mfe.domain.FileDescriptor}.
 */
@RestController
@RequestMapping("/api")
public class FileDescriptorResource {

    private final Logger log = LoggerFactory.getLogger(FileDescriptorResource.class);

    private static final String ENTITY_NAME = "fileDescriptor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileDescriptorService fileDescriptorService;

    private final FileDescriptorRepository fileDescriptorRepository;

    public FileDescriptorResource(FileDescriptorService fileDescriptorService, FileDescriptorRepository fileDescriptorRepository) {
        this.fileDescriptorService = fileDescriptorService;
        this.fileDescriptorRepository = fileDescriptorRepository;
    }

    /**
     * {@code POST  /file-descriptors} : Create a new fileDescriptor.
     *
     * @param fileDescriptor the fileDescriptor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileDescriptor, or with status {@code 400 (Bad Request)} if the fileDescriptor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-descriptors")
    public ResponseEntity<FileDescriptor> createFileDescriptor(@Valid @RequestBody FileDescriptor fileDescriptor)
        throws URISyntaxException {
        log.debug("REST request to save FileDescriptor : {}", fileDescriptor);
        if (fileDescriptor.getId() != null) {
            throw new BadRequestAlertException("A new fileDescriptor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileDescriptor result = fileDescriptorService.save(fileDescriptor);
        return ResponseEntity
            .created(new URI("/api/file-descriptors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-descriptors/:id} : Updates an existing fileDescriptor.
     *
     * @param id the id of the fileDescriptor to save.
     * @param fileDescriptor the fileDescriptor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDescriptor,
     * or with status {@code 400 (Bad Request)} if the fileDescriptor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileDescriptor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-descriptors/{id}")
    public ResponseEntity<FileDescriptor> updateFileDescriptor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FileDescriptor fileDescriptor
    ) throws URISyntaxException {
        log.debug("REST request to update FileDescriptor : {}, {}", id, fileDescriptor);
        if (fileDescriptor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileDescriptor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileDescriptorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FileDescriptor result = fileDescriptorService.save(fileDescriptor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileDescriptor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /file-descriptors/:id} : Partial updates given fields of an existing fileDescriptor, field will ignore if it is null
     *
     * @param id the id of the fileDescriptor to save.
     * @param fileDescriptor the fileDescriptor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDescriptor,
     * or with status {@code 400 (Bad Request)} if the fileDescriptor is not valid,
     * or with status {@code 404 (Not Found)} if the fileDescriptor is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileDescriptor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/file-descriptors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FileDescriptor> partialUpdateFileDescriptor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FileDescriptor fileDescriptor
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileDescriptor partially : {}, {}", id, fileDescriptor);
        if (fileDescriptor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileDescriptor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileDescriptorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileDescriptor> result = fileDescriptorService.partialUpdate(fileDescriptor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileDescriptor.getId().toString())
        );
    }

    /**
     * {@code GET  /file-descriptors} : get all the fileDescriptors.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileDescriptors in body.
     */
    @GetMapping("/file-descriptors")
    public ResponseEntity<List<FileDescriptor>> getAllFileDescriptors(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("issourcefor-is-null".equals(filter)) {
            log.debug("REST request to get all FileDescriptors where isSourceFor is null");
            return new ResponseEntity<>(fileDescriptorService.findAllWhereIsSourceForIsNull(), HttpStatus.OK);
        }

        if ("isdestfor-is-null".equals(filter)) {
            log.debug("REST request to get all FileDescriptors where isDestFor is null");
            return new ResponseEntity<>(fileDescriptorService.findAllWhereIsDestForIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of FileDescriptors");
        Page<FileDescriptor> page = fileDescriptorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /file-descriptors/:id} : get the "id" fileDescriptor.
     *
     * @param id the id of the fileDescriptor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileDescriptor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-descriptors/{id}")
    public ResponseEntity<FileDescriptor> getFileDescriptor(@PathVariable Long id) {
        log.debug("REST request to get FileDescriptor : {}", id);
        Optional<FileDescriptor> fileDescriptor = fileDescriptorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileDescriptor);
    }

    /**
     * {@code DELETE  /file-descriptors/:id} : delete the "id" fileDescriptor.
     *
     * @param id the id of the fileDescriptor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-descriptors/{id}")
    public ResponseEntity<Void> deleteFileDescriptor(@PathVariable Long id) {
        log.debug("REST request to delete FileDescriptor : {}", id);
        fileDescriptorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
