package com.paba.mfe.service.impl;

import com.paba.mfe.domain.UserAccount;
import com.paba.mfe.repository.UserAccountRepository;
import com.paba.mfe.service.UserAccountService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserAccount}.
 */
@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    private final Logger log = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    private final UserAccountRepository userAccountRepository;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        log.debug("Request to save UserAccount : {}", userAccount);
        return userAccountRepository.save(userAccount);
    }

    @Override
    public Optional<UserAccount> partialUpdate(UserAccount userAccount) {
        log.debug("Request to partially update UserAccount : {}", userAccount);

        return userAccountRepository
            .findById(userAccount.getId())
            .map(existingUserAccount -> {
                if (userAccount.getAccountType() != null) {
                    existingUserAccount.setAccountType(userAccount.getAccountType());
                }
                if (userAccount.getComment() != null) {
                    existingUserAccount.setComment(userAccount.getComment());
                }
                if (userAccount.getCreationDate() != null) {
                    existingUserAccount.setCreationDate(userAccount.getCreationDate());
                }
                if (userAccount.getLastUpdated() != null) {
                    existingUserAccount.setLastUpdated(userAccount.getLastUpdated());
                }

                return existingUserAccount;
            })
            .map(userAccountRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAccount> findAll(Pageable pageable) {
        log.debug("Request to get all UserAccounts");
        return userAccountRepository.findAll(pageable);
    }

    public Page<UserAccount> findAllWithEagerRelationships(Pageable pageable) {
        return userAccountRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAccount> findOne(Long id) {
        log.debug("Request to get UserAccount : {}", id);
        return userAccountRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAccount : {}", id);
        userAccountRepository.deleteById(id);
    }
}
