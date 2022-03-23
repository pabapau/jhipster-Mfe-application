package com.paba.mfe.repository;

import com.paba.mfe.domain.UserAccount;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UserAccountRepositoryWithBagRelationshipsImpl implements UserAccountRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<UserAccount> fetchBagRelationships(Optional<UserAccount> userAccount) {
        return userAccount.map(this::fetchBusinessUnits).map(this::fetchSites);
    }

    @Override
    public Page<UserAccount> fetchBagRelationships(Page<UserAccount> userAccounts) {
        return new PageImpl<>(
            fetchBagRelationships(userAccounts.getContent()),
            userAccounts.getPageable(),
            userAccounts.getTotalElements()
        );
    }

    @Override
    public List<UserAccount> fetchBagRelationships(List<UserAccount> userAccounts) {
        return Optional.of(userAccounts).map(this::fetchBusinessUnits).map(this::fetchSites).get();
    }

    UserAccount fetchBusinessUnits(UserAccount result) {
        return entityManager
            .createQuery(
                "select userAccount from UserAccount userAccount left join fetch userAccount.businessUnits where userAccount is :userAccount",
                UserAccount.class
            )
            .setParameter("userAccount", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<UserAccount> fetchBusinessUnits(List<UserAccount> userAccounts) {
        return entityManager
            .createQuery(
                "select distinct userAccount from UserAccount userAccount left join fetch userAccount.businessUnits where userAccount in :userAccounts",
                UserAccount.class
            )
            .setParameter("userAccounts", userAccounts)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    UserAccount fetchSites(UserAccount result) {
        return entityManager
            .createQuery(
                "select userAccount from UserAccount userAccount left join fetch userAccount.sites where userAccount is :userAccount",
                UserAccount.class
            )
            .setParameter("userAccount", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<UserAccount> fetchSites(List<UserAccount> userAccounts) {
        return entityManager
            .createQuery(
                "select distinct userAccount from UserAccount userAccount left join fetch userAccount.sites where userAccount in :userAccounts",
                UserAccount.class
            )
            .setParameter("userAccounts", userAccounts)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
