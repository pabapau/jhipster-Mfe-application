package com.paba.mfe.repository;

import com.paba.mfe.domain.Useraccount;
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
public class UseraccountRepositoryWithBagRelationshipsImpl implements UseraccountRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Useraccount> fetchBagRelationships(Optional<Useraccount> useraccount) {
        return useraccount.map(this::fetchBusinessunits).map(this::fetchSites);
    }

    @Override
    public Page<Useraccount> fetchBagRelationships(Page<Useraccount> useraccounts) {
        return new PageImpl<>(
            fetchBagRelationships(useraccounts.getContent()),
            useraccounts.getPageable(),
            useraccounts.getTotalElements()
        );
    }

    @Override
    public List<Useraccount> fetchBagRelationships(List<Useraccount> useraccounts) {
        return Optional.of(useraccounts).map(this::fetchBusinessunits).map(this::fetchSites).get();
    }

    Useraccount fetchBusinessunits(Useraccount result) {
        return entityManager
            .createQuery(
                "select useraccount from Useraccount useraccount left join fetch useraccount.businessunits where useraccount is :useraccount",
                Useraccount.class
            )
            .setParameter("useraccount", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Useraccount> fetchBusinessunits(List<Useraccount> useraccounts) {
        return entityManager
            .createQuery(
                "select distinct useraccount from Useraccount useraccount left join fetch useraccount.businessunits where useraccount in :useraccounts",
                Useraccount.class
            )
            .setParameter("useraccounts", useraccounts)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Useraccount fetchSites(Useraccount result) {
        return entityManager
            .createQuery(
                "select useraccount from Useraccount useraccount left join fetch useraccount.sites where useraccount is :useraccount",
                Useraccount.class
            )
            .setParameter("useraccount", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Useraccount> fetchSites(List<Useraccount> useraccounts) {
        return entityManager
            .createQuery(
                "select distinct useraccount from Useraccount useraccount left join fetch useraccount.sites where useraccount in :useraccounts",
                Useraccount.class
            )
            .setParameter("useraccounts", useraccounts)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
