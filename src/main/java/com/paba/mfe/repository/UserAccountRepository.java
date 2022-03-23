package com.paba.mfe.repository;

import com.paba.mfe.domain.UserAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserAccount entity.
 */
@Repository
public interface UserAccountRepository extends UserAccountRepositoryWithBagRelationships, JpaRepository<UserAccount, Long> {
    @Query("select userAccount from UserAccount userAccount where userAccount.user.login = ?#{principal.username}")
    List<UserAccount> findByUserIsCurrentUser();

    default Optional<UserAccount> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<UserAccount> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<UserAccount> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct userAccount from UserAccount userAccount left join fetch userAccount.user",
        countQuery = "select count(distinct userAccount) from UserAccount userAccount"
    )
    Page<UserAccount> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct userAccount from UserAccount userAccount left join fetch userAccount.user")
    List<UserAccount> findAllWithToOneRelationships();

    @Query("select userAccount from UserAccount userAccount left join fetch userAccount.user where userAccount.id =:id")
    Optional<UserAccount> findOneWithToOneRelationships(@Param("id") Long id);
}
