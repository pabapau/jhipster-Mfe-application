package com.paba.mfe.service;

import com.paba.mfe.domain.Flow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Flow}.
 */
public interface FlowService {
    /**
     * Save a flow.
     *
     * @param flow the entity to save.
     * @return the persisted entity.
     */
    Flow save(Flow flow);

    /**
     * Partially updates a flow.
     *
     * @param flow the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Flow> partialUpdate(Flow flow);

    /**
     * Get all the flows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Flow> findAll(Pageable pageable);
    /**
     * Get all the Flow where FileDescriptor is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Flow> findAllWhereFileDescriptorIsNull();

    /**
     * Get all the flows with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Flow> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" flow.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Flow> findOne(Long id);

    /**
     * Delete the "id" flow.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
