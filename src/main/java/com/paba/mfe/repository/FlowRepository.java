package com.paba.mfe.repository;

import com.paba.mfe.domain.Flow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Flow entity.
 */
@Repository
public interface FlowRepository extends JpaRepository<Flow, Long> {
    default Optional<Flow> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Flow> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Flow> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct flow from Flow flow left join fetch flow.businessUnit left join fetch flow.origin left join fetch flow.destination",
        countQuery = "select count(distinct flow) from Flow flow"
    )
    Page<Flow> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct flow from Flow flow left join fetch flow.businessUnit left join fetch flow.origin left join fetch flow.destination"
    )
    List<Flow> findAllWithToOneRelationships();

    @Query(
        "select flow from Flow flow left join fetch flow.businessUnit left join fetch flow.origin left join fetch flow.destination where flow.id =:id"
    )
    Optional<Flow> findOneWithToOneRelationships(@Param("id") Long id);
}
