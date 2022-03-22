package com.paba.mfe.service;

import com.paba.mfe.domain.Useraccount;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Useraccount}.
 */
public interface UseraccountService {
    /**
     * Save a useraccount.
     *
     * @param useraccount the entity to save.
     * @return the persisted entity.
     */
    Useraccount save(Useraccount useraccount);

    /**
     * Partially updates a useraccount.
     *
     * @param useraccount the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Useraccount> partialUpdate(Useraccount useraccount);

    /**
     * Get all the useraccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Useraccount> findAll(Pageable pageable);

    /**
     * Get all the useraccounts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Useraccount> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" useraccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Useraccount> findOne(Long id);

    /**
     * Delete the "id" useraccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
