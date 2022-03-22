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
                if (xProt.getXprottype() != null) {
                    existingXProt.setXprottype(xProt.getXprottype());
                }
                if (xProt.getXrole() != null) {
                    existingXProt.setXrole(xProt.getXrole());
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
                if (xProt.getCreationdate() != null) {
                    existingXProt.setCreationdate(xProt.getCreationdate());
                }
                if (xProt.getLastupdated() != null) {
                    existingXProt.setLastupdated(xProt.getLastupdated());
                }
                if (xProt.getBuildstate() != null) {
                    existingXProt.setBuildstate(xProt.getBuildstate());
                }
                if (xProt.getBuildcount() != null) {
                    existingXProt.setBuildcount(xProt.getBuildcount());
                }
                if (xProt.getBuildcomment() != null) {
                    existingXProt.setBuildcomment(xProt.getBuildcomment());
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

    @Override
    @Transactional(readOnly = true)
    public Optional<XProt> findOne(Long id) {
        log.debug("Request to get XProt : {}", id);
        return xProtRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete XProt : {}", id);
        xProtRepository.deleteById(id);
    }
}
