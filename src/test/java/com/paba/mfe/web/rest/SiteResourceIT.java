package com.paba.mfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paba.mfe.IntegrationTest;
import com.paba.mfe.domain.BusinessUnit;
import com.paba.mfe.domain.Site;
import com.paba.mfe.domain.enumeration.BuildState;
import com.paba.mfe.domain.enumeration.OsType;
import com.paba.mfe.domain.enumeration.SiteType;
import com.paba.mfe.repository.SiteRepository;
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
 * Integration tests for the {@link SiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SiteResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final SiteType DEFAULT_SITE_TYPE = SiteType.INT;
    private static final SiteType UPDATED_SITE_TYPE = SiteType.EXT;

    private static final OsType DEFAULT_OS_TYPE = OsType.WIN;
    private static final OsType UPDATED_OS_TYPE = OsType.AIX;

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

    private static final String ENTITY_API_URL = "/api/sites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteMockMvc;

    private Site site;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createEntity(EntityManager em) {
        Site site = new Site()
            .name(DEFAULT_NAME)
            .siteType(DEFAULT_SITE_TYPE)
            .osType(DEFAULT_OS_TYPE)
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
        site.setBusinessUnit(businessUnit);
        return site;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createUpdatedEntity(EntityManager em) {
        Site site = new Site()
            .name(UPDATED_NAME)
            .siteType(UPDATED_SITE_TYPE)
            .osType(UPDATED_OS_TYPE)
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
        site.setBusinessUnit(businessUnit);
        return site;
    }

    @BeforeEach
    public void initTest() {
        site = createEntity(em);
    }

    @Test
    @Transactional
    void createSite() throws Exception {
        int databaseSizeBeforeCreate = siteRepository.findAll().size();
        // Create the Site
        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isCreated());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate + 1);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSite.getSiteType()).isEqualTo(DEFAULT_SITE_TYPE);
        assertThat(testSite.getOsType()).isEqualTo(DEFAULT_OS_TYPE);
        assertThat(testSite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSite.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testSite.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testSite.getBuildState()).isEqualTo(DEFAULT_BUILD_STATE);
        assertThat(testSite.getBuildCount()).isEqualTo(DEFAULT_BUILD_COUNT);
        assertThat(testSite.getBuildComment()).isEqualTo(DEFAULT_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void createSiteWithExistingId() throws Exception {
        // Create the Site with an existing ID
        site.setId(1L);

        int databaseSizeBeforeCreate = siteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setName(null);

        // Create the Site, which fails.

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setSiteType(null);

        // Create the Site, which fails.

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setCreationDate(null);

        // Create the Site, which fails.

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setLastUpdated(null);

        // Create the Site, which fails.

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSites() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].siteType").value(hasItem(DEFAULT_SITE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].osType").value(hasItem(DEFAULT_OS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].buildState").value(hasItem(DEFAULT_BUILD_STATE.toString())))
            .andExpect(jsonPath("$.[*].buildCount").value(hasItem(DEFAULT_BUILD_COUNT)))
            .andExpect(jsonPath("$.[*].buildComment").value(hasItem(DEFAULT_BUILD_COMMENT)));
    }

    @Test
    @Transactional
    void getSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get the site
        restSiteMockMvc
            .perform(get(ENTITY_API_URL_ID, site.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(site.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.siteType").value(DEFAULT_SITE_TYPE.toString()))
            .andExpect(jsonPath("$.osType").value(DEFAULT_OS_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.buildState").value(DEFAULT_BUILD_STATE.toString()))
            .andExpect(jsonPath("$.buildCount").value(DEFAULT_BUILD_COUNT))
            .andExpect(jsonPath("$.buildComment").value(DEFAULT_BUILD_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingSite() throws Exception {
        // Get the site
        restSiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site
        Site updatedSite = siteRepository.findById(site.getId()).get();
        // Disconnect from session so that the updates on updatedSite are not directly saved in db
        em.detach(updatedSite);
        updatedSite
            .name(UPDATED_NAME)
            .siteType(UPDATED_SITE_TYPE)
            .osType(UPDATED_OS_TYPE)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);

        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSite.getSiteType()).isEqualTo(UPDATED_SITE_TYPE);
        assertThat(testSite.getOsType()).isEqualTo(UPDATED_OS_TYPE);
        assertThat(testSite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSite.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSite.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testSite.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testSite.getBuildCount()).isEqualTo(UPDATED_BUILD_COUNT);
        assertThat(testSite.getBuildComment()).isEqualTo(UPDATED_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, site.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(site))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(site))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite
            .siteType(UPDATED_SITE_TYPE)
            .osType(UPDATED_OS_TYPE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSite.getSiteType()).isEqualTo(UPDATED_SITE_TYPE);
        assertThat(testSite.getOsType()).isEqualTo(UPDATED_OS_TYPE);
        assertThat(testSite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSite.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSite.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testSite.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testSite.getBuildCount()).isEqualTo(UPDATED_BUILD_COUNT);
        assertThat(testSite.getBuildComment()).isEqualTo(UPDATED_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite
            .name(UPDATED_NAME)
            .siteType(UPDATED_SITE_TYPE)
            .osType(UPDATED_OS_TYPE)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSite.getSiteType()).isEqualTo(UPDATED_SITE_TYPE);
        assertThat(testSite.getOsType()).isEqualTo(UPDATED_OS_TYPE);
        assertThat(testSite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSite.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSite.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testSite.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testSite.getBuildCount()).isEqualTo(UPDATED_BUILD_COUNT);
        assertThat(testSite.getBuildComment()).isEqualTo(UPDATED_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, site.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(site))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(site))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeDelete = siteRepository.findAll().size();

        // Delete the site
        restSiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, site.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
