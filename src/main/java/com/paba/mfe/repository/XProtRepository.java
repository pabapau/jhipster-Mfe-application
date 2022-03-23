package com.paba.mfe.repository;

import com.paba.mfe.domain.XProt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the XProt entity.
 */
@Repository
public interface XProtRepository extends JpaRepository<XProt, Long> {
    default Optional<XProt> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<XProt> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<XProt> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct xProt from XProt xProt left join fetch xProt.onNode",
        countQuery = "select count(distinct xProt) from XProt xProt"
    )
    Page<XProt> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct xProt from XProt xProt left join fetch xProt.onNode")
    List<XProt> findAllWithToOneRelationships();

    @Query("select xProt from XProt xProt left join fetch xProt.onNode where xProt.id =:id")
    Optional<XProt> findOneWithToOneRelationships(@Param("id") Long id);
}
