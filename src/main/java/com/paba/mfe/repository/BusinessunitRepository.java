package com.paba.mfe.repository;

import com.paba.mfe.domain.Businessunit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Businessunit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessunitRepository extends JpaRepository<Businessunit, Long> {}
