package com.paba.mfe.service.impl;

import com.paba.mfe.domain.Site;
import com.paba.mfe.repository.SiteRepository;
import com.paba.mfe.service.SiteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Site}.
 */
@Service
@Transactional
public class SiteServiceImpl implements SiteService {

    private final Logger log = LoggerFactory.getLogger(SiteServiceImpl.class);

    private final SiteRepository siteRepository;

    public SiteServiceImpl(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    public Site save(Site site) {
        log.debug("Request to save Site : {}", site);
        return siteRepository.save(site);
    }

    @Override
    public Optional<Site> partialUpdate(Site site) {
        log.debug("Request to partially update Site : {}", site);

        return siteRepository
            .findById(site.getId())
            .map(existingSite -> {
                if (site.getName() != null) {
                    existingSite.setName(site.getName());
                }
                if (site.getSitetype() != null) {
                    existingSite.setSitetype(site.getSitetype());
                }
                if (site.getDescription() != null) {
                    existingSite.setDescription(site.getDescription());
                }
                if (site.getSitenode() != null) {
                    existingSite.setSitenode(site.getSitenode());
                }
                if (site.getCreationdate() != null) {
                    existingSite.setCreationdate(site.getCreationdate());
                }
                if (site.getLastupdated() != null) {
                    existingSite.setLastupdated(site.getLastupdated());
                }
                if (site.getBuildstate() != null) {
                    existingSite.setBuildstate(site.getBuildstate());
                }
                if (site.getBuildcount() != null) {
                    existingSite.setBuildcount(site.getBuildcount());
                }
                if (site.getBuildcomment() != null) {
                    existingSite.setBuildcomment(site.getBuildcomment());
                }

                return existingSite;
            })
            .map(siteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Site> findAll(Pageable pageable) {
        log.debug("Request to get all Sites");
        return siteRepository.findAll(pageable);
    }

    public Page<Site> findAllWithEagerRelationships(Pageable pageable) {
        return siteRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Site> findOne(Long id) {
        log.debug("Request to get Site : {}", id);
        return siteRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Site : {}", id);
        siteRepository.deleteById(id);
    }
}
