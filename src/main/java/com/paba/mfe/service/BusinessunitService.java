package com.paba.mfe.service;

import com.paba.mfe.domain.Businessunit;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Businessunit}.
 */
public interface BusinessunitService {
    /**
     * Save a businessunit.
     *
     * @param businessunit the entity to save.
     * @return the persisted entity.
     */
    Businessunit save(Businessunit businessunit);

    /**
     * Partially updates a businessunit.
     *
     * @param businessunit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Businessunit> partialUpdate(Businessunit businessunit);

    /**
     * Get all the businessunits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Businessunit> findAll(Pageable pageable);

    /**
     * Get the "id" businessunit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Businessunit> findOne(Long id);

    /**
     * Delete the "id" businessunit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
