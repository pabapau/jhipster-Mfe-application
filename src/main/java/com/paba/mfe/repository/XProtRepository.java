package com.paba.mfe.repository;

import com.paba.mfe.domain.XProt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the XProt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XProtRepository extends JpaRepository<XProt, Long> {}
