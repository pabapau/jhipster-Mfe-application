package com.paba.mfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paba.mfe.IntegrationTest;
import com.paba.mfe.domain.Businessunit;
import com.paba.mfe.domain.Site;
import com.paba.mfe.domain.enumeration.Buildstate;
import com.paba.mfe.domain.enumeration.Sitetype;
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

    private static final Sitetype DEFAULT_SITETYPE = Sitetype.INT;
    private static final Sitetype UPDATED_SITETYPE = Sitetype.EXT;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SITENODE = "AAAAAAAAAA";
    private static final String UPDATED_SITENODE = "BBBBBBBBBB";

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
            .sitetype(DEFAULT_SITETYPE)
            .description(DEFAULT_DESCRIPTION)
            .sitenode(DEFAULT_SITENODE)
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
        site.setBusinessunit(businessunit);
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
            .sitetype(UPDATED_SITETYPE)
            .description(UPDATED_DESCRIPTION)
            .sitenode(UPDATED_SITENODE)
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
        site.setBusinessunit(businessunit);
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
        assertThat(testSite.getSitetype()).isEqualTo(DEFAULT_SITETYPE);
        assertThat(testSite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSite.getSitenode()).isEqualTo(DEFAULT_SITENODE);
        assertThat(testSite.getCreationdate()).isEqualTo(DEFAULT_CREATIONDATE);
        assertThat(testSite.getLastupdated()).isEqualTo(DEFAULT_LASTUPDATED);
        assertThat(testSite.getBuildstate()).isEqualTo(DEFAULT_BUILDSTATE);
        assertThat(testSite.getBuildcount()).isEqualTo(DEFAULT_BUILDCOUNT);
        assertThat(testSite.getBuildcomment()).isEqualTo(DEFAULT_BUILDCOMMENT);
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
    void checkSitetypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setSitetype(null);

        // Create the Site, which fails.

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSitenodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setSitenode(null);

        // Create the Site, which fails.

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setCreationdate(null);

        // Create the Site, which fails.

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastupdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setLastupdated(null);

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
            .andExpect(jsonPath("$.[*].sitetype").value(hasItem(DEFAULT_SITETYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sitenode").value(hasItem(DEFAULT_SITENODE)))
            .andExpect(jsonPath("$.[*].creationdate").value(hasItem(DEFAULT_CREATIONDATE.toString())))
            .andExpect(jsonPath("$.[*].lastupdated").value(hasItem(DEFAULT_LASTUPDATED.toString())))
            .andExpect(jsonPath("$.[*].buildstate").value(hasItem(DEFAULT_BUILDSTATE.toString())))
            .andExpect(jsonPath("$.[*].buildcount").value(hasItem(DEFAULT_BUILDCOUNT)))
            .andExpect(jsonPath("$.[*].buildcomment").value(hasItem(DEFAULT_BUILDCOMMENT)));
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
            .andExpect(jsonPath("$.sitetype").value(DEFAULT_SITETYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sitenode").value(DEFAULT_SITENODE))
            .andExpect(jsonPath("$.creationdate").value(DEFAULT_CREATIONDATE.toString()))
            .andExpect(jsonPath("$.lastupdated").value(DEFAULT_LASTUPDATED.toString()))
            .andExpect(jsonPath("$.buildstate").value(DEFAULT_BUILDSTATE.toString()))
            .andExpect(jsonPath("$.buildcount").value(DEFAULT_BUILDCOUNT))
            .andExpect(jsonPath("$.buildcomment").value(DEFAULT_BUILDCOMMENT));
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
            .sitetype(UPDATED_SITETYPE)
            .description(UPDATED_DESCRIPTION)
            .sitenode(UPDATED_SITENODE)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildstate(UPDATED_BUILDSTATE)
            .buildcount(UPDATED_BUILDCOUNT)
            .buildcomment(UPDATED_BUILDCOMMENT);

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
        assertThat(testSite.getSitetype()).isEqualTo(UPDATED_SITETYPE);
        assertThat(testSite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSite.getSitenode()).isEqualTo(UPDATED_SITENODE);
        assertThat(testSite.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testSite.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testSite.getBuildstate()).isEqualTo(UPDATED_BUILDSTATE);
        assertThat(testSite.getBuildcount()).isEqualTo(UPDATED_BUILDCOUNT);
        assertThat(testSite.getBuildcomment()).isEqualTo(UPDATED_BUILDCOMMENT);
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
            .sitetype(UPDATED_SITETYPE)
            .description(UPDATED_DESCRIPTION)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildstate(UPDATED_BUILDSTATE)
            .buildcount(UPDATED_BUILDCOUNT)
            .buildcomment(UPDATED_BUILDCOMMENT);

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
        assertThat(testSite.getSitetype()).isEqualTo(UPDATED_SITETYPE);
        assertThat(testSite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSite.getSitenode()).isEqualTo(DEFAULT_SITENODE);
        assertThat(testSite.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testSite.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testSite.getBuildstate()).isEqualTo(UPDATED_BUILDSTATE);
        assertThat(testSite.getBuildcount()).isEqualTo(UPDATED_BUILDCOUNT);
        assertThat(testSite.getBuildcomment()).isEqualTo(UPDATED_BUILDCOMMENT);
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
            .sitetype(UPDATED_SITETYPE)
            .description(UPDATED_DESCRIPTION)
            .sitenode(UPDATED_SITENODE)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildstate(UPDATED_BUILDSTATE)
            .buildcount(UPDATED_BUILDCOUNT)
            .buildcomment(UPDATED_BUILDCOMMENT);

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
        assertThat(testSite.getSitetype()).isEqualTo(UPDATED_SITETYPE);
        assertThat(testSite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSite.getSitenode()).isEqualTo(UPDATED_SITENODE);
        assertThat(testSite.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testSite.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testSite.getBuildstate()).isEqualTo(UPDATED_BUILDSTATE);
        assertThat(testSite.getBuildcount()).isEqualTo(UPDATED_BUILDCOUNT);
        assertThat(testSite.getBuildcomment()).isEqualTo(UPDATED_BUILDCOMMENT);
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
