package com.paba.mfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paba.mfe.IntegrationTest;
import com.paba.mfe.domain.XProt;
import com.paba.mfe.domain.enumeration.BuildState;
import com.paba.mfe.domain.enumeration.XProtType;
import com.paba.mfe.domain.enumeration.XRole;
import com.paba.mfe.repository.XProtRepository;
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
 * Integration tests for the {@link XProtResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class XProtResourceIT {

    private static final XProtType DEFAULT_XPROT_TYPE = XProtType.PESITANY;
    private static final XProtType UPDATED_XPROT_TYPE = XProtType.PESITSSL;

    private static final XRole DEFAULT_X_ROLE = XRole.CLI;
    private static final XRole UPDATED_X_ROLE = XRole.SRV;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_ACCESS_SERVICE_POINT = 1;
    private static final Integer UPDATED_ACCESS_SERVICE_POINT = 2;

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

    private static final String ENTITY_API_URL = "/api/x-prots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private XProtRepository xProtRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restXProtMockMvc;

    private XProt xProt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XProt createEntity(EntityManager em) {
        XProt xProt = new XProt()
            .xprotType(DEFAULT_XPROT_TYPE)
            .xRole(DEFAULT_X_ROLE)
            .comment(DEFAULT_COMMENT)
            .accessAddress(DEFAULT_ACCESS_ADDRESS)
            .accessServicePoint(DEFAULT_ACCESS_SERVICE_POINT)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .buildState(DEFAULT_BUILD_STATE)
            .buildCount(DEFAULT_BUILD_COUNT)
            .buildComment(DEFAULT_BUILD_COMMENT);
        return xProt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XProt createUpdatedEntity(EntityManager em) {
        XProt xProt = new XProt()
            .xprotType(UPDATED_XPROT_TYPE)
            .xRole(UPDATED_X_ROLE)
            .comment(UPDATED_COMMENT)
            .accessAddress(UPDATED_ACCESS_ADDRESS)
            .accessServicePoint(UPDATED_ACCESS_SERVICE_POINT)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);
        return xProt;
    }

    @BeforeEach
    public void initTest() {
        xProt = createEntity(em);
    }

    @Test
    @Transactional
    void createXProt() throws Exception {
        int databaseSizeBeforeCreate = xProtRepository.findAll().size();
        // Create the XProt
        restXProtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isCreated());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeCreate + 1);
        XProt testXProt = xProtList.get(xProtList.size() - 1);
        assertThat(testXProt.getXprotType()).isEqualTo(DEFAULT_XPROT_TYPE);
        assertThat(testXProt.getxRole()).isEqualTo(DEFAULT_X_ROLE);
        assertThat(testXProt.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testXProt.getAccessAddress()).isEqualTo(DEFAULT_ACCESS_ADDRESS);
        assertThat(testXProt.getAccessServicePoint()).isEqualTo(DEFAULT_ACCESS_SERVICE_POINT);
        assertThat(testXProt.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testXProt.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testXProt.getBuildState()).isEqualTo(DEFAULT_BUILD_STATE);
        assertThat(testXProt.getBuildCount()).isEqualTo(DEFAULT_BUILD_COUNT);
        assertThat(testXProt.getBuildComment()).isEqualTo(DEFAULT_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void createXProtWithExistingId() throws Exception {
        // Create the XProt with an existing ID
        xProt.setId(1L);

        int databaseSizeBeforeCreate = xProtRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restXProtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isBadRequest());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkXprotTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = xProtRepository.findAll().size();
        // set the field null
        xProt.setXprotType(null);

        // Create the XProt, which fails.

        restXProtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isBadRequest());

        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkxRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = xProtRepository.findAll().size();
        // set the field null
        xProt.setxRole(null);

        // Create the XProt, which fails.

        restXProtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isBadRequest());

        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = xProtRepository.findAll().size();
        // set the field null
        xProt.setCreationDate(null);

        // Create the XProt, which fails.

        restXProtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isBadRequest());

        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = xProtRepository.findAll().size();
        // set the field null
        xProt.setLastUpdated(null);

        // Create the XProt, which fails.

        restXProtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isBadRequest());

        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllXProts() throws Exception {
        // Initialize the database
        xProtRepository.saveAndFlush(xProt);

        // Get all the xProtList
        restXProtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(xProt.getId().intValue())))
            .andExpect(jsonPath("$.[*].xprotType").value(hasItem(DEFAULT_XPROT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].xRole").value(hasItem(DEFAULT_X_ROLE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].accessAddress").value(hasItem(DEFAULT_ACCESS_ADDRESS)))
            .andExpect(jsonPath("$.[*].accessServicePoint").value(hasItem(DEFAULT_ACCESS_SERVICE_POINT)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].buildState").value(hasItem(DEFAULT_BUILD_STATE.toString())))
            .andExpect(jsonPath("$.[*].buildCount").value(hasItem(DEFAULT_BUILD_COUNT)))
            .andExpect(jsonPath("$.[*].buildComment").value(hasItem(DEFAULT_BUILD_COMMENT)));
    }

    @Test
    @Transactional
    void getXProt() throws Exception {
        // Initialize the database
        xProtRepository.saveAndFlush(xProt);

        // Get the xProt
        restXProtMockMvc
            .perform(get(ENTITY_API_URL_ID, xProt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(xProt.getId().intValue()))
            .andExpect(jsonPath("$.xprotType").value(DEFAULT_XPROT_TYPE.toString()))
            .andExpect(jsonPath("$.xRole").value(DEFAULT_X_ROLE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.accessAddress").value(DEFAULT_ACCESS_ADDRESS))
            .andExpect(jsonPath("$.accessServicePoint").value(DEFAULT_ACCESS_SERVICE_POINT))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.buildState").value(DEFAULT_BUILD_STATE.toString()))
            .andExpect(jsonPath("$.buildCount").value(DEFAULT_BUILD_COUNT))
            .andExpect(jsonPath("$.buildComment").value(DEFAULT_BUILD_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingXProt() throws Exception {
        // Get the xProt
        restXProtMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewXProt() throws Exception {
        // Initialize the database
        xProtRepository.saveAndFlush(xProt);

        int databaseSizeBeforeUpdate = xProtRepository.findAll().size();

        // Update the xProt
        XProt updatedXProt = xProtRepository.findById(xProt.getId()).get();
        // Disconnect from session so that the updates on updatedXProt are not directly saved in db
        em.detach(updatedXProt);
        updatedXProt
            .xprotType(UPDATED_XPROT_TYPE)
            .xRole(UPDATED_X_ROLE)
            .comment(UPDATED_COMMENT)
            .accessAddress(UPDATED_ACCESS_ADDRESS)
            .accessServicePoint(UPDATED_ACCESS_SERVICE_POINT)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);

        restXProtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedXProt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedXProt))
            )
            .andExpect(status().isOk());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeUpdate);
        XProt testXProt = xProtList.get(xProtList.size() - 1);
        assertThat(testXProt.getXprotType()).isEqualTo(UPDATED_XPROT_TYPE);
        assertThat(testXProt.getxRole()).isEqualTo(UPDATED_X_ROLE);
        assertThat(testXProt.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testXProt.getAccessAddress()).isEqualTo(UPDATED_ACCESS_ADDRESS);
        assertThat(testXProt.getAccessServicePoint()).isEqualTo(UPDATED_ACCESS_SERVICE_POINT);
        assertThat(testXProt.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testXProt.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testXProt.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testXProt.getBuildCount()).isEqualTo(UPDATED_BUILD_COUNT);
        assertThat(testXProt.getBuildComment()).isEqualTo(UPDATED_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingXProt() throws Exception {
        int databaseSizeBeforeUpdate = xProtRepository.findAll().size();
        xProt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXProtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xProt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xProt))
            )
            .andExpect(status().isBadRequest());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchXProt() throws Exception {
        int databaseSizeBeforeUpdate = xProtRepository.findAll().size();
        xProt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXProtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xProt))
            )
            .andExpect(status().isBadRequest());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamXProt() throws Exception {
        int databaseSizeBeforeUpdate = xProtRepository.findAll().size();
        xProt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXProtMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateXProtWithPatch() throws Exception {
        // Initialize the database
        xProtRepository.saveAndFlush(xProt);

        int databaseSizeBeforeUpdate = xProtRepository.findAll().size();

        // Update the xProt using partial update
        XProt partialUpdatedXProt = new XProt();
        partialUpdatedXProt.setId(xProt.getId());

        partialUpdatedXProt
            .xprotType(UPDATED_XPROT_TYPE)
            .xRole(UPDATED_X_ROLE)
            .comment(UPDATED_COMMENT)
            .accessAddress(UPDATED_ACCESS_ADDRESS)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildComment(UPDATED_BUILD_COMMENT);

        restXProtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXProt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXProt))
            )
            .andExpect(status().isOk());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeUpdate);
        XProt testXProt = xProtList.get(xProtList.size() - 1);
        assertThat(testXProt.getXprotType()).isEqualTo(UPDATED_XPROT_TYPE);
        assertThat(testXProt.getxRole()).isEqualTo(UPDATED_X_ROLE);
        assertThat(testXProt.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testXProt.getAccessAddress()).isEqualTo(UPDATED_ACCESS_ADDRESS);
        assertThat(testXProt.getAccessServicePoint()).isEqualTo(DEFAULT_ACCESS_SERVICE_POINT);
        assertThat(testXProt.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testXProt.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testXProt.getBuildState()).isEqualTo(DEFAULT_BUILD_STATE);
        assertThat(testXProt.getBuildCount()).isEqualTo(DEFAULT_BUILD_COUNT);
        assertThat(testXProt.getBuildComment()).isEqualTo(UPDATED_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateXProtWithPatch() throws Exception {
        // Initialize the database
        xProtRepository.saveAndFlush(xProt);

        int databaseSizeBeforeUpdate = xProtRepository.findAll().size();

        // Update the xProt using partial update
        XProt partialUpdatedXProt = new XProt();
        partialUpdatedXProt.setId(xProt.getId());

        partialUpdatedXProt
            .xprotType(UPDATED_XPROT_TYPE)
            .xRole(UPDATED_X_ROLE)
            .comment(UPDATED_COMMENT)
            .accessAddress(UPDATED_ACCESS_ADDRESS)
            .accessServicePoint(UPDATED_ACCESS_SERVICE_POINT)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);

        restXProtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXProt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXProt))
            )
            .andExpect(status().isOk());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeUpdate);
        XProt testXProt = xProtList.get(xProtList.size() - 1);
        assertThat(testXProt.getXprotType()).isEqualTo(UPDATED_XPROT_TYPE);
        assertThat(testXProt.getxRole()).isEqualTo(UPDATED_X_ROLE);
        assertThat(testXProt.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testXProt.getAccessAddress()).isEqualTo(UPDATED_ACCESS_ADDRESS);
        assertThat(testXProt.getAccessServicePoint()).isEqualTo(UPDATED_ACCESS_SERVICE_POINT);
        assertThat(testXProt.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testXProt.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testXProt.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testXProt.getBuildCount()).isEqualTo(UPDATED_BUILD_COUNT);
        assertThat(testXProt.getBuildComment()).isEqualTo(UPDATED_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingXProt() throws Exception {
        int databaseSizeBeforeUpdate = xProtRepository.findAll().size();
        xProt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXProtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, xProt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xProt))
            )
            .andExpect(status().isBadRequest());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchXProt() throws Exception {
        int databaseSizeBeforeUpdate = xProtRepository.findAll().size();
        xProt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXProtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xProt))
            )
            .andExpect(status().isBadRequest());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamXProt() throws Exception {
        int databaseSizeBeforeUpdate = xProtRepository.findAll().size();
        xProt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXProtMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the XProt in the database
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteXProt() throws Exception {
        // Initialize the database
        xProtRepository.saveAndFlush(xProt);

        int databaseSizeBeforeDelete = xProtRepository.findAll().size();

        // Delete the xProt
        restXProtMockMvc
            .perform(delete(ENTITY_API_URL_ID, xProt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
