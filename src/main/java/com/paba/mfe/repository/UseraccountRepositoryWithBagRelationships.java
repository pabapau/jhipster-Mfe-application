package com.paba.mfe.repository;

import com.paba.mfe.domain.Useraccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UseraccountRepositoryWithBagRelationships {
    Optional<Useraccount> fetchBagRelationships(Optional<Useraccount> useraccount);

    List<Useraccount> fetchBagRelationships(List<Useraccount> useraccounts);

    Page<Useraccount> fetchBagRelationships(Page<Useraccount> useraccounts);
}
