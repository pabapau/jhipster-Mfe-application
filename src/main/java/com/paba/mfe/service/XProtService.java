package com.paba.mfe.service;

import com.paba.mfe.domain.XProt;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link XProt}.
 */
public interface XProtService {
    /**
     * Save a xProt.
     *
     * @param xProt the entity to save.
     * @return the persisted entity.
     */
    XProt save(XProt xProt);

    /**
     * Partially updates a xProt.
     *
     * @param xProt the entity to update partially.
     * @return the persisted entity.
     */
    Optional<XProt> partialUpdate(XProt xProt);

    /**
     * Get all the xProts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<XProt> findAll(Pageable pageable);

    /**
     * Get all the xProts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<XProt> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" xProt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<XProt> findOne(Long id);

    /**
     * Delete the "id" xProt.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
