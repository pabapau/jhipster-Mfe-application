package com.paba.mfe.service.impl;

import com.paba.mfe.domain.Useraccount;
import com.paba.mfe.repository.UseraccountRepository;
import com.paba.mfe.service.UseraccountService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Useraccount}.
 */
@Service
@Transactional
public class UseraccountServiceImpl implements UseraccountService {

    private final Logger log = LoggerFactory.getLogger(UseraccountServiceImpl.class);

    private final UseraccountRepository useraccountRepository;

    public UseraccountServiceImpl(UseraccountRepository useraccountRepository) {
        this.useraccountRepository = useraccountRepository;
    }

    @Override
    public Useraccount save(Useraccount useraccount) {
        log.debug("Request to save Useraccount : {}", useraccount);
        return useraccountRepository.save(useraccount);
    }

    @Override
    public Optional<Useraccount> partialUpdate(Useraccount useraccount) {
        log.debug("Request to partially update Useraccount : {}", useraccount);

        return useraccountRepository
            .findById(useraccount.getId())
            .map(existingUseraccount -> {
                if (useraccount.getAccounttype() != null) {
                    existingUseraccount.setAccounttype(useraccount.getAccounttype());
                }
                if (useraccount.getComment() != null) {
                    existingUseraccount.setComment(useraccount.getComment());
                }
                if (useraccount.getCreationdate() != null) {
                    existingUseraccount.setCreationdate(useraccount.getCreationdate());
                }
                if (useraccount.getLastupdated() != null) {
                    existingUseraccount.setLastupdated(useraccount.getLastupdated());
                }

                return existingUseraccount;
            })
            .map(useraccountRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Useraccount> findAll(Pageable pageable) {
        log.debug("Request to get all Useraccounts");
        return useraccountRepository.findAll(pageable);
    }

    public Page<Useraccount> findAllWithEagerRelationships(Pageable pageable) {
        return useraccountRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Useraccount> findOne(Long id) {
        log.debug("Request to get Useraccount : {}", id);
        return useraccountRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Useraccount : {}", id);
        useraccountRepository.deleteById(id);
    }
}
