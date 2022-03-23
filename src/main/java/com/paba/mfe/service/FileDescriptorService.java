package com.paba.mfe.service;

import com.paba.mfe.domain.FileDescriptor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FileDescriptor}.
 */
public interface FileDescriptorService {
    /**
     * Save a fileDescriptor.
     *
     * @param fileDescriptor the entity to save.
     * @return the persisted entity.
     */
    FileDescriptor save(FileDescriptor fileDescriptor);

    /**
     * Partially updates a fileDescriptor.
     *
     * @param fileDescriptor the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileDescriptor> partialUpdate(FileDescriptor fileDescriptor);

    /**
     * Get all the fileDescriptors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FileDescriptor> findAll(Pageable pageable);
    /**
     * Get all the FileDescriptor where IsSourceFor is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<FileDescriptor> findAllWhereIsSourceForIsNull();
    /**
     * Get all the FileDescriptor where IsDestFor is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<FileDescriptor> findAllWhereIsDestForIsNull();

    /**
     * Get the "id" fileDescriptor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileDescriptor> findOne(Long id);

    /**
     * Delete the "id" fileDescriptor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
