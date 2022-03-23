package com.paba.mfe.service;

import com.paba.mfe.domain.UserAccount;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link UserAccount}.
 */
public interface UserAccountService {
    /**
     * Save a userAccount.
     *
     * @param userAccount the entity to save.
     * @return the persisted entity.
     */
    UserAccount save(UserAccount userAccount);

    /**
     * Partially updates a userAccount.
     *
     * @param userAccount the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAccount> partialUpdate(UserAccount userAccount);

    /**
     * Get all the userAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAccount> findAll(Pageable pageable);

    /**
     * Get all the userAccounts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAccount> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAccount> findOne(Long id);

    /**
     * Delete the "id" userAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
