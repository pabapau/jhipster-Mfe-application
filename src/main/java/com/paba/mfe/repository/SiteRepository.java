package com.paba.mfe.repository;

import com.paba.mfe.domain.Site;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Site entity.
 */
@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    default Optional<Site> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Site> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Site> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct site from Site site left join fetch site.businessunit",
        countQuery = "select count(distinct site) from Site site"
    )
    Page<Site> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct site from Site site left join fetch site.businessunit")
    List<Site> findAllWithToOneRelationships();

    @Query("select site from Site site left join fetch site.businessunit where site.id =:id")
    Optional<Site> findOneWithToOneRelationships(@Param("id") Long id);
}
