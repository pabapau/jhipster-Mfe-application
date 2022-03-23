package com.paba.mfe.service;

import com.paba.mfe.domain.BusinessUnit;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link BusinessUnit}.
 */
public interface BusinessUnitService {
    /**
     * Save a businessUnit.
     *
     * @param businessUnit the entity to save.
     * @return the persisted entity.
     */
    BusinessUnit save(BusinessUnit businessUnit);

    /**
     * Partially updates a businessUnit.
     *
     * @param businessUnit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusinessUnit> partialUpdate(BusinessUnit businessUnit);

    /**
     * Get all the businessUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessUnit> findAll(Pageable pageable);

    /**
     * Get the "id" businessUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusinessUnit> findOne(Long id);

    /**
     * Delete the "id" businessUnit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
