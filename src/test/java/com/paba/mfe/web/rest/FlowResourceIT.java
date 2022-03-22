package com.paba.mfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paba.mfe.IntegrationTest;
import com.paba.mfe.domain.Businessunit;
import com.paba.mfe.domain.Flow;
import com.paba.mfe.domain.Site;
import com.paba.mfe.domain.enumeration.Buildstate;
import com.paba.mfe.domain.enumeration.Flowusecase;
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

    private static final Flowusecase DEFAULT_FLOWUSECASE = Flowusecase.A2A;
    private static final Flowusecase UPDATED_FLOWUSECASE = Flowusecase.A2B;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATIONDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATIONDATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LASTUPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LASTUPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final Buildstate DEFAULT_BUILDSTATE = Buildstate.NOTBUILD;
    private static final Buildstate UPDATED_BUILDSTATE = Buildstate.REQUIRED;

    private static final Integer DEFAULT_BUILDCOUNT = 1;
    private static final Integer UPDATED_BUILDCOUNT = 2;

    private static final String DEFAULT_BUILDCOMMENT = "AAAAAAAAAA";
    private static final String UPDATED_BUILDCOMMENT = "BBBBBBBBBB";

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
            .flowusecase(DEFAULT_FLOWUSECASE)
            .description(DEFAULT_DESCRIPTION)
            .creationdate(DEFAULT_CREATIONDATE)
            .lastupdated(DEFAULT_LASTUPDATED)
            .buildstate(DEFAULT_BUILDSTATE)
            .buildcount(DEFAULT_BUILDCOUNT)
            .buildcomment(DEFAULT_BUILDCOMMENT);
        // Add required entity
        Businessunit businessunit;
        if (TestUtil.findAll(em, Businessunit.class).isEmpty()) {
            businessunit = BusinessunitResourceIT.createEntity(em);
            em.persist(businessunit);
            em.flush();
        } else {
            businessunit = TestUtil.findAll(em, Businessunit.class).get(0);
        }
        flow.setBusinessunit(businessunit);
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
            .flowusecase(UPDATED_FLOWUSECASE)
            .description(UPDATED_DESCRIPTION)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildstate(UPDATED_BUILDSTATE)
            .buildcount(UPDATED_BUILDCOUNT)
            .buildcomment(UPDATED_BUILDCOMMENT);
        // Add required entity
        Businessunit businessunit;
        if (TestUtil.findAll(em, Businessunit.class).isEmpty()) {
            businessunit = BusinessunitResourceIT.createUpdatedEntity(em);
            em.persist(businessunit);
            em.flush();
        } else {
            businessunit = TestUtil.findAll(em, Businessunit.class).get(0);
        }
        flow.setBusinessunit(businessunit);
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
        assertThat(testFlow.getFlowusecase()).isEqualTo(DEFAULT_FLOWUSECASE);
        assertThat(testFlow.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlow.getCreationdate()).isEqualTo(DEFAULT_CREATIONDATE);
        assertThat(testFlow.getLastupdated()).isEqualTo(DEFAULT_LASTUPDATED);
        assertThat(testFlow.getBuildstate()).isEqualTo(DEFAULT_BUILDSTATE);
        assertThat(testFlow.getBuildcount()).isEqualTo(DEFAULT_BUILDCOUNT);
        assertThat(testFlow.getBuildcomment()).isEqualTo(DEFAULT_BUILDCOMMENT);
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
    void checkFlowusecaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowRepository.findAll().size();
        // set the field null
        flow.setFlowusecase(null);

        // Create the Flow, which fails.

        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flow)))
            .andExpect(status().isBadRequest());

        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowRepository.findAll().size();
        // set the field null
        flow.setCreationdate(null);

        // Create the Flow, which fails.

        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flow)))
            .andExpect(status().isBadRequest());

        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastupdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowRepository.findAll().size();
        // set the field null
        flow.setLastupdated(null);

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
            .andExpect(jsonPath("$.[*].flowusecase").value(hasItem(DEFAULT_FLOWUSECASE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationdate").value(hasItem(DEFAULT_CREATIONDATE.toString())))
            .andExpect(jsonPath("$.[*].lastupdated").value(hasItem(DEFAULT_LASTUPDATED.toString())))
            .andExpect(jsonPath("$.[*].buildstate").value(hasItem(DEFAULT_BUILDSTATE.toString())))
            .andExpect(jsonPath("$.[*].buildcount").value(hasItem(DEFAULT_BUILDCOUNT)))
            .andExpect(jsonPath("$.[*].buildcomment").value(hasItem(DEFAULT_BUILDCOMMENT)));
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
            .andExpect(jsonPath("$.flowusecase").value(DEFAULT_FLOWUSECASE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationdate").value(DEFAULT_CREATIONDATE.toString()))
            .andExpect(jsonPath("$.lastupdated").value(DEFAULT_LASTUPDATED.toString()))
            .andExpect(jsonPath("$.buildstate").value(DEFAULT_BUILDSTATE.toString()))
            .andExpect(jsonPath("$.buildcount").value(DEFAULT_BUILDCOUNT))
            .andExpect(jsonPath("$.buildcomment").value(DEFAULT_BUILDCOMMENT));
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
            .flowusecase(UPDATED_FLOWUSECASE)
            .description(UPDATED_DESCRIPTION)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildstate(UPDATED_BUILDSTATE)
            .buildcount(UPDATED_BUILDCOUNT)
            .buildcomment(UPDATED_BUILDCOMMENT);

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
        assertThat(testFlow.getFlowusecase()).isEqualTo(UPDATED_FLOWUSECASE);
        assertThat(testFlow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlow.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testFlow.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testFlow.getBuildstate()).isEqualTo(UPDATED_BUILDSTATE);
        assertThat(testFlow.getBuildcount()).isEqualTo(UPDATED_BUILDCOUNT);
        assertThat(testFlow.getBuildcomment()).isEqualTo(UPDATED_BUILDCOMMENT);
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
            .flowusecase(UPDATED_FLOWUSECASE)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildstate(UPDATED_BUILDSTATE)
            .buildcount(UPDATED_BUILDCOUNT);

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
        assertThat(testFlow.getFlowusecase()).isEqualTo(UPDATED_FLOWUSECASE);
        assertThat(testFlow.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlow.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testFlow.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testFlow.getBuildstate()).isEqualTo(UPDATED_BUILDSTATE);
        assertThat(testFlow.getBuildcount()).isEqualTo(UPDATED_BUILDCOUNT);
        assertThat(testFlow.getBuildcomment()).isEqualTo(DEFAULT_BUILDCOMMENT);
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
            .flowusecase(UPDATED_FLOWUSECASE)
            .description(UPDATED_DESCRIPTION)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildstate(UPDATED_BUILDSTATE)
            .buildcount(UPDATED_BUILDCOUNT)
            .buildcomment(UPDATED_BUILDCOMMENT);

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
        assertThat(testFlow.getFlowusecase()).isEqualTo(UPDATED_FLOWUSECASE);
        assertThat(testFlow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlow.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testFlow.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testFlow.getBuildstate()).isEqualTo(UPDATED_BUILDSTATE);
        assertThat(testFlow.getBuildcount()).isEqualTo(UPDATED_BUILDCOUNT);
        assertThat(testFlow.getBuildcomment()).isEqualTo(UPDATED_BUILDCOMMENT);
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
