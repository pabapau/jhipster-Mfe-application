package com.paba.mfe.service.impl;

import com.paba.mfe.domain.Flow;
import com.paba.mfe.repository.FlowRepository;
import com.paba.mfe.service.FlowService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Flow}.
 */
@Service
@Transactional
public class FlowServiceImpl implements FlowService {

    private final Logger log = LoggerFactory.getLogger(FlowServiceImpl.class);

    private final FlowRepository flowRepository;

    public FlowServiceImpl(FlowRepository flowRepository) {
        this.flowRepository = flowRepository;
    }

    @Override
    public Flow save(Flow flow) {
        log.debug("Request to save Flow : {}", flow);
        return flowRepository.save(flow);
    }

    @Override
    public Optional<Flow> partialUpdate(Flow flow) {
        log.debug("Request to partially update Flow : {}", flow);

        return flowRepository
            .findById(flow.getId())
            .map(existingFlow -> {
                if (flow.getFileIdent() != null) {
                    existingFlow.setFileIdent(flow.getFileIdent());
                }
                if (flow.getFlowusecase() != null) {
                    existingFlow.setFlowusecase(flow.getFlowusecase());
                }
                if (flow.getDescription() != null) {
                    existingFlow.setDescription(flow.getDescription());
                }
                if (flow.getCreationdate() != null) {
                    existingFlow.setCreationdate(flow.getCreationdate());
                }
                if (flow.getLastupdated() != null) {
                    existingFlow.setLastupdated(flow.getLastupdated());
                }
                if (flow.getBuildstate() != null) {
                    existingFlow.setBuildstate(flow.getBuildstate());
                }
                if (flow.getBuildcount() != null) {
                    existingFlow.setBuildcount(flow.getBuildcount());
                }
                if (flow.getBuildcomment() != null) {
                    existingFlow.setBuildcomment(flow.getBuildcomment());
                }

                return existingFlow;
            })
            .map(flowRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Flow> findAll(Pageable pageable) {
        log.debug("Request to get all Flows");
        return flowRepository.findAll(pageable);
    }

    public Page<Flow> findAllWithEagerRelationships(Pageable pageable) {
        return flowRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Flow> findOne(Long id) {
        log.debug("Request to get Flow : {}", id);
        return flowRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Flow : {}", id);
        flowRepository.deleteById(id);
    }
}
