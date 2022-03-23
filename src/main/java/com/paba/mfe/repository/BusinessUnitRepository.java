package com.paba.mfe.repository;

import com.paba.mfe.domain.BusinessUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusinessUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {}
