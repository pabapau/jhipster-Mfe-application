package com.paba.mfe.service.impl;

import com.paba.mfe.domain.XProt;
import com.paba.mfe.repository.XProtRepository;
import com.paba.mfe.service.XProtService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link XProt}.
 */
@Service
@Transactional
public class XProtServiceImpl implements XProtService {

    private final Logger log = LoggerFactory.getLogger(XProtServiceImpl.class);

    private final XProtRepository xProtRepository;

    public XProtServiceImpl(XProtRepository xProtRepository) {
        this.xProtRepository = xProtRepository;
    }

    @Override
    public XProt save(XProt xProt) {
        log.debug("Request to save XProt : {}", xProt);
        return xProtRepository.save(xProt);
    }

    @Override
    public Optional<XProt> partialUpdate(XProt xProt) {
        log.debug("Request to partially update XProt : {}", xProt);

        return xProtRepository
            .findById(xProt.getId())
            .map(existingXProt -> {
                if (xProt.getXprotType() != null) {
                    existingXProt.setXprotType(xProt.getXprotType());
                }
                if (xProt.getxRole() != null) {
                    existingXProt.setxRole(xProt.getxRole());
                }
                if (xProt.getComment() != null) {
                    existingXProt.setComment(xProt.getComment());
                }
                if (xProt.getAccessAddress() != null) {
                    existingXProt.setAccessAddress(xProt.getAccessAddress());
                }
                if (xProt.getAccessServicePoint() != null) {
                    existingXProt.setAccessServicePoint(xProt.getAccessServicePoint());
                }
                if (xProt.getCreationDate() != null) {
                    existingXProt.setCreationDate(xProt.getCreationDate());
                }
                if (xProt.getLastUpdated() != null) {
                    existingXProt.setLastUpdated(xProt.getLastUpdated());
                }
                if (xProt.getBuildState() != null) {
                    existingXProt.setBuildState(xProt.getBuildState());
                }
                if (xProt.getBuildCount() != null) {
                    existingXProt.setBuildCount(xProt.getBuildCount());
                }
                if (xProt.getBuildComment() != null) {
                    existingXProt.setBuildComment(xProt.getBuildComment());
                }

                return existingXProt;
            })
            .map(xProtRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XProt> findAll(Pageable pageable) {
        log.debug("Request to get all XProts");
        return xProtRepository.findAll(pageable);
    }

    public Page<XProt> findAllWithEagerRelationships(Pageable pageable) {
        return xProtRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<XProt> findOne(Long id) {
        log.debug("Request to get XProt : {}", id);
        return xProtRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete XProt : {}", id);
        xProtRepository.deleteById(id);
    }
}
