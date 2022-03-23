package com.paba.mfe.repository;

import com.paba.mfe.domain.FileDescriptor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FileDescriptor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, Long> {}
