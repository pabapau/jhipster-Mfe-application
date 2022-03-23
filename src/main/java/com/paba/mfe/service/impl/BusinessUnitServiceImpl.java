package com.paba.mfe.service.impl;

import com.paba.mfe.domain.BusinessUnit;
import com.paba.mfe.repository.BusinessUnitRepository;
import com.paba.mfe.service.BusinessUnitService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BusinessUnit}.
 */
@Service
@Transactional
public class BusinessUnitServiceImpl implements BusinessUnitService {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitServiceImpl.class);

    private final BusinessUnitRepository businessUnitRepository;

    public BusinessUnitServiceImpl(BusinessUnitRepository businessUnitRepository) {
        this.businessUnitRepository = businessUnitRepository;
    }

    @Override
    public BusinessUnit save(BusinessUnit businessUnit) {
        log.debug("Request to save BusinessUnit : {}", businessUnit);
        return businessUnitRepository.save(businessUnit);
    }

    @Override
    public Optional<BusinessUnit> partialUpdate(BusinessUnit businessUnit) {
        log.debug("Request to partially update BusinessUnit : {}", businessUnit);

        return businessUnitRepository
            .findById(businessUnit.getId())
            .map(existingBusinessUnit -> {
                if (businessUnit.getCode() != null) {
                    existingBusinessUnit.setCode(businessUnit.getCode());
                }
                if (businessUnit.getName() != null) {
                    existingBusinessUnit.setName(businessUnit.getName());
                }
                if (businessUnit.getDescription() != null) {
                    existingBusinessUnit.setDescription(businessUnit.getDescription());
                }

                return existingBusinessUnit;
            })
            .map(businessUnitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessUnit> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessUnits");
        return businessUnitRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusinessUnit> findOne(Long id) {
        log.debug("Request to get BusinessUnit : {}", id);
        return businessUnitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BusinessUnit : {}", id);
        businessUnitRepository.deleteById(id);
    }
}
