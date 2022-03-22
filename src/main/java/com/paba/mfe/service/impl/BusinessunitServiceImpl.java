package com.paba.mfe.service.impl;

import com.paba.mfe.domain.Businessunit;
import com.paba.mfe.repository.BusinessunitRepository;
import com.paba.mfe.service.BusinessunitService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Businessunit}.
 */
@Service
@Transactional
public class BusinessunitServiceImpl implements BusinessunitService {

    private final Logger log = LoggerFactory.getLogger(BusinessunitServiceImpl.class);

    private final BusinessunitRepository businessunitRepository;

    public BusinessunitServiceImpl(BusinessunitRepository businessunitRepository) {
        this.businessunitRepository = businessunitRepository;
    }

    @Override
    public Businessunit save(Businessunit businessunit) {
        log.debug("Request to save Businessunit : {}", businessunit);
        return businessunitRepository.save(businessunit);
    }

    @Override
    public Optional<Businessunit> partialUpdate(Businessunit businessunit) {
        log.debug("Request to partially update Businessunit : {}", businessunit);

        return businessunitRepository
            .findById(businessunit.getId())
            .map(existingBusinessunit -> {
                if (businessunit.getCode() != null) {
                    existingBusinessunit.setCode(businessunit.getCode());
                }
                if (businessunit.getName() != null) {
                    existingBusinessunit.setName(businessunit.getName());
                }
                if (businessunit.getDescription() != null) {
                    existingBusinessunit.setDescription(businessunit.getDescription());
                }

                return existingBusinessunit;
            })
            .map(businessunitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Businessunit> findAll(Pageable pageable) {
        log.debug("Request to get all Businessunits");
        return businessunitRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Businessunit> findOne(Long id) {
        log.debug("Request to get Businessunit : {}", id);
        return businessunitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Businessunit : {}", id);
        businessunitRepository.deleteById(id);
    }
}
