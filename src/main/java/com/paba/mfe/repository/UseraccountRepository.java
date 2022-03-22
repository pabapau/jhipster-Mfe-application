package com.paba.mfe.repository;

import com.paba.mfe.domain.Useraccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Useraccount entity.
 */
@Repository
public interface UseraccountRepository extends UseraccountRepositoryWithBagRelationships, JpaRepository<Useraccount, Long> {
    @Query("select useraccount from Useraccount useraccount where useraccount.user.login = ?#{principal.username}")
    List<Useraccount> findByUserIsCurrentUser();

    default Optional<Useraccount> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Useraccount> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Useraccount> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct useraccount from Useraccount useraccount left join fetch useraccount.user",
        countQuery = "select count(distinct useraccount) from Useraccount useraccount"
    )
    Page<Useraccount> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct useraccount from Useraccount useraccount left join fetch useraccount.user")
    List<Useraccount> findAllWithToOneRelationships();

    @Query("select useraccount from Useraccount useraccount left join fetch useraccount.user where useraccount.id =:id")
    Optional<Useraccount> findOneWithToOneRelationships(@Param("id") Long id);
}
