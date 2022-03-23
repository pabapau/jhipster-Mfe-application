package com.paba.mfe.repository;

import com.paba.mfe.domain.UserAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UserAccountRepositoryWithBagRelationships {
    Optional<UserAccount> fetchBagRelationships(Optional<UserAccount> userAccount);

    List<UserAccount> fetchBagRelationships(List<UserAccount> userAccounts);

    Page<UserAccount> fetchBagRelationships(Page<UserAccount> userAccounts);
}
