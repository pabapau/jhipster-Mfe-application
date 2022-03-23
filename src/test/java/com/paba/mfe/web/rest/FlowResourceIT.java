package com.paba.mfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paba.mfe.IntegrationTest;
import com.paba.mfe.domain.BusinessUnit;
import com.paba.mfe.domain.Flow;
import com.paba.mfe.domain.Site;
import com.paba.mfe.domain.enumeration.BuildState;
import com.paba.mfe.domain.enumeration.FlowUseCase;
import com.paba.mfe.repository.FlowRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FlowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlowResourceIT {

    private static final String DEFAULT_FILE_IDENT = "AAAAAAAAAA";
    private static final String UPDATED_FILE_IDENT = "BBBBBBBBBB";

    private static final FlowUseCase DEFAULT_FLOW_USE_CASE = FlowUseCase.A2A;
    private static final FlowUseCase UPDATED_FLOW_USE_CASE = FlowUseCase.A2B;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_UPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final BuildState DEFAULT_BUILD_STATE = BuildState.NOTBUILD;
    private static final BuildState UPDATED_BUILD_STATE = BuildState.REQUIRED;

    private static final Integer DEFAULT_BUILD_COUNT = 1;
    private static final Integer UPDATED_BUILD_COUNT = 2;

    private static final String DEFAULT_BUILD_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_BUILD_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/flows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlowMockMvc;

    private Flow flow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flow createEntity(EntityManager em) {
        Flow flow = new Flow()
            .fileIdent(DEFAULT_FILE_IDENT)
            .flowUseCase(DEFAULT_FLOW_USE_CASE)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .buildState(DEFAULT_BUILD_STATE)
            .buildCount(DEFAULT_BUILD_COUNT)
            .buildComment(DEFAULT_BUILD_COMMENT);
        // Add required entity
        BusinessUnit businessUnit;
        if (TestUtil.findAll(em, BusinessUnit.class).isEmpty()) {
            businessUnit = BusinessUnitResourceIT.createEntity(em);
            em.persist(businessUnit);
            em.flush();
        } else {
            businessUnit = TestUtil.findAll(em, BusinessUnit.class).get(0);
        }
        flow.setBusinessUnit(businessUnit);
        // Add required entity
        Site site;
        if (TestUtil.findAll(em, Site.class).isEmpty()) {
            site = SiteResourceIT.createEntity(em);
            em.persist(site);
            em.flush();
        } else {
            site = TestUtil.findAll(em, Site.class).get(0);
        }
        flow.setOrigin(site);
        // Add required entity
        flow.setDestination(site);
        return flow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flow createUpdatedEntity(EntityManager em) {
        Flow flow = new Flow()
            .fileIdent(UPDATED_FILE_IDENT)
            .flowUseCase(UPDATED_FLOW_USE_CASE)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);
        // Add required entity
        BusinessUnit businessUnit;
        if (TestUtil.findAll(em, BusinessUnit.class).isEmpty()) {
            businessUnit = BusinessUnitResourceIT.createUpdatedEntity(em);
            em.persist(businessUnit);
            em.flush();
        } else {
            businessUnit = TestUtil.findAll(em, BusinessUnit.class).get(0);
        }
        flow.setBusinessUnit(businessUnit);
        // Add required entity
        Site site;
        if (TestUtil.findAll(em, Site.class).isEmpty()) {
            site = SiteResourceIT.createUpdatedEntity(em);
            em.persist(site);
            em.flush();
        } else {
            site = TestUtil.findAll(em, Site.class).get(0);
        }
        flow.setOrigin(site);
        // Add required entity
        flow.setDestination(site);
        return flow;
    }

    @BeforeEach
    public void initTest() {
        flow = createEntity(em);
    }

    @Test
    @Transactional
    void createFlow() throws Exception {
        int databaseSizeBeforeCreate = flowRepository.findAll().size();
        // Create the Flow
        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flow)))
            .andExpect(status().isCreated());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeCreate + 1);
        Flow testFlow = flowList.get(flowList.size() - 1);
        assertThat(testFlow.getFileIdent()).isEqualTo(DEFAULT_FILE_IDENT);
        assertThat(testFlow.getFlowUseCase()).isEqualTo(DEFAULT_FLOW_USE_CASE);
        assertThat(testFlow.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlow.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFlow.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testFlow.getBuildState()).isEqualTo(DEFAULT_BUILD_STATE);
        assertThat(testFlow.getBuildCount()).isEqualTo(DEFAULT_BUILD_COUNT);
        assertThat(testFlow.getBuildComment()).isEqualTo(DEFAULT_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void createFlowWithExistingId() throws Exception {
        // Create the Flow with an existing ID
        flow.setId(1L);

        int databaseSizeBeforeCreate = flowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flow)))
            .andExpect(status().isBadRequest());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFileIdentIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowRepository.findAll().size();
        // set the field null
        flow.setFileIdent(null);

        // Create the Flow, which fails.

        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flow)))
            .andExpect(status().isBadRequest());

        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFlowUseCaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowRepository.findAll().size();
        // set the field null
        flow.setFlowUseCase(null);

        // Create the Flow, which fails.

        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flow)))
            .andExpect(status().isBadRequest());

        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowRepository.findAll().size();
        // set the field null
        flow.setCreationDate(null);

        // Create the Flow, which fails.

        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flow)))
            .andExpect(status().isBadRequest());

        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowRepository.findAll().size();
        // set the field null
        flow.setLastUpdated(null);

        // Create the Flow, which fails.

        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flow)))
            .andExpect(status().isBadRequest());

        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFlows() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        // Get all the flowList
        restFlowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flow.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileIdent").value(hasItem(DEFAULT_FILE_IDENT)))
            .andExpect(jsonPath("$.[*].flowUseCase").value(hasItem(DEFAULT_FLOW_USE_CASE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].buildState").value(hasItem(DEFAULT_BUILD_STATE.toString())))
            .andExpect(jsonPath("$.[*].buildCount").value(hasItem(DEFAULT_BUILD_COUNT)))
            .andExpect(jsonPath("$.[*].buildComment").value(hasItem(DEFAULT_BUILD_COMMENT)));
    }

    @Test
    @Transactional
    void getFlow() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        // Get the flow
        restFlowMockMvc
            .perform(get(ENTITY_API_URL_ID, flow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flow.getId().intValue()))
            .andExpect(jsonPath("$.fileIdent").value(DEFAULT_FILE_IDENT))
            .andExpect(jsonPath("$.flowUseCase").value(DEFAULT_FLOW_USE_CASE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.buildState").value(DEFAULT_BUILD_STATE.toString()))
            .andExpect(jsonPath("$.buildCount").value(DEFAULT_BUILD_COUNT))
            .andExpect(jsonPath("$.buildComment").value(DEFAULT_BUILD_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingFlow() throws Exception {
        // Get the flow
        restFlowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFlow() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        int databaseSizeBeforeUpdate = flowRepository.findAll().size();

        // Update the flow
        Flow updatedFlow = flowRepository.findById(flow.getId()).get();
        // Disconnect from session so that the updates on updatedFlow are not directly saved in db
        em.detach(updatedFlow);
        updatedFlow
            .fileIdent(UPDATED_FILE_IDENT)
            .flowUseCase(UPDATED_FLOW_USE_CASE)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);

        restFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFlow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFlow))
            )
            .andExpect(status().isOk());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
        Flow testFlow = flowList.get(flowList.size() - 1);
        assertThat(testFlow.getFileIdent()).isEqualTo(UPDATED_FILE_IDENT);
        assertThat(testFlow.getFlowUseCase()).isEqualTo(UPDATED_FLOW_USE_CASE);
        assertThat(testFlow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlow.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFlow.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFlow.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testFlow.getBuildCount()).isEqualTo(UPDATED_BUILD_COUNT);
        assertThat(testFlow.getBuildComment()).isEqualTo(UPDATED_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flow)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlowWithPatch() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        int databaseSizeBeforeUpdate = flowRepository.findAll().size();

        // Update the flow using partial update
        Flow partialUpdatedFlow = new Flow();
        partialUpdatedFlow.setId(flow.getId());

        partialUpdatedFlow
            .fileIdent(UPDATED_FILE_IDENT)
            .flowUseCase(UPDATED_FLOW_USE_CASE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT);

        restFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlow))
            )
            .andExpect(status().isOk());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
        Flow testFlow = flowList.get(flowList.size() - 1);
        assertThat(testFlow.getFileIdent()).isEqualTo(UPDATED_FILE_IDENT);
        assertThat(testFlow.getFlowUseCase()).isEqualTo(UPDATED_FLOW_USE_CASE);
        assertThat(testFlow.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlow.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFlow.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFlow.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testFlow.getBuildCount()).isEqualTo(UPDATED_BUILD_COUNT);
        assertThat(testFlow.getBuildComment()).isEqualTo(DEFAULT_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateFlowWithPatch() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        int databaseSizeBeforeUpdate = flowRepository.findAll().size();

        // Update the flow using partial update
        Flow partialUpdatedFlow = new Flow();
        partialUpdatedFlow.setId(flow.getId());

        partialUpdatedFlow
            .fileIdent(UPDATED_FILE_IDENT)
            .flowUseCase(UPDATED_FLOW_USE_CASE)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);

        restFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlow))
            )
            .andExpect(status().isOk());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
        Flow testFlow = flowList.get(flowList.size() - 1);
        assertThat(testFlow.getFileIdent()).isEqualTo(UPDATED_FILE_IDENT);
        assertThat(testFlow.getFlowUseCase()).isEqualTo(UPDATED_FLOW_USE_CASE);
        assertThat(testFlow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlow.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFlow.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFlow.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testFlow.getBuildCount()).isEqualTo(UPDATED_BUILD_COUNT);
        assertThat(testFlow.getBuildComment()).isEqualTo(UPDATED_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flow)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlow() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        int databaseSizeBeforeDelete = flowRepository.findAll().size();

        // Delete the flow
        restFlowMockMvc
            .perform(delete(ENTITY_API_URL_ID, flow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
