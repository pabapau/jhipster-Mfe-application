package com.paba.mfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paba.mfe.IntegrationTest;
import com.paba.mfe.domain.Businessunit;
import com.paba.mfe.repository.BusinessunitRepository;
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
 * Integration tests for the {@link BusinessunitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusinessunitResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/businessunits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusinessunitRepository businessunitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessunitMockMvc;

    private Businessunit businessunit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Businessunit createEntity(EntityManager em) {
        Businessunit businessunit = new Businessunit().code(DEFAULT_CODE).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return businessunit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Businessunit createUpdatedEntity(EntityManager em) {
        Businessunit businessunit = new Businessunit().code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return businessunit;
    }

    @BeforeEach
    public void initTest() {
        businessunit = createEntity(em);
    }

    @Test
    @Transactional
    void createBusinessunit() throws Exception {
        int databaseSizeBeforeCreate = businessunitRepository.findAll().size();
        // Create the Businessunit
        restBusinessunitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessunit)))
            .andExpect(status().isCreated());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeCreate + 1);
        Businessunit testBusinessunit = businessunitList.get(businessunitList.size() - 1);
        assertThat(testBusinessunit.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBusinessunit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusinessunit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createBusinessunitWithExistingId() throws Exception {
        // Create the Businessunit with an existing ID
        businessunit.setId(1L);

        int databaseSizeBeforeCreate = businessunitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessunitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessunit)))
            .andExpect(status().isBadRequest());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessunitRepository.findAll().size();
        // set the field null
        businessunit.setCode(null);

        // Create the Businessunit, which fails.

        restBusinessunitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessunit)))
            .andExpect(status().isBadRequest());

        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessunitRepository.findAll().size();
        // set the field null
        businessunit.setName(null);

        // Create the Businessunit, which fails.

        restBusinessunitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessunit)))
            .andExpect(status().isBadRequest());

        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinessunits() throws Exception {
        // Initialize the database
        businessunitRepository.saveAndFlush(businessunit);

        // Get all the businessunitList
        restBusinessunitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessunit.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBusinessunit() throws Exception {
        // Initialize the database
        businessunitRepository.saveAndFlush(businessunit);

        // Get the businessunit
        restBusinessunitMockMvc
            .perform(get(ENTITY_API_URL_ID, businessunit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessunit.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingBusinessunit() throws Exception {
        // Get the businessunit
        restBusinessunitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusinessunit() throws Exception {
        // Initialize the database
        businessunitRepository.saveAndFlush(businessunit);

        int databaseSizeBeforeUpdate = businessunitRepository.findAll().size();

        // Update the businessunit
        Businessunit updatedBusinessunit = businessunitRepository.findById(businessunit.getId()).get();
        // Disconnect from session so that the updates on updatedBusinessunit are not directly saved in db
        em.detach(updatedBusinessunit);
        updatedBusinessunit.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restBusinessunitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBusinessunit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBusinessunit))
            )
            .andExpect(status().isOk());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeUpdate);
        Businessunit testBusinessunit = businessunitList.get(businessunitList.size() - 1);
        assertThat(testBusinessunit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBusinessunit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessunit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingBusinessunit() throws Exception {
        int databaseSizeBeforeUpdate = businessunitRepository.findAll().size();
        businessunit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessunitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessunit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessunit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessunit() throws Exception {
        int databaseSizeBeforeUpdate = businessunitRepository.findAll().size();
        businessunit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessunitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessunit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessunit() throws Exception {
        int databaseSizeBeforeUpdate = businessunitRepository.findAll().size();
        businessunit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessunitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessunit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessunitWithPatch() throws Exception {
        // Initialize the database
        businessunitRepository.saveAndFlush(businessunit);

        int databaseSizeBeforeUpdate = businessunitRepository.findAll().size();

        // Update the businessunit using partial update
        Businessunit partialUpdatedBusinessunit = new Businessunit();
        partialUpdatedBusinessunit.setId(businessunit.getId());

        partialUpdatedBusinessunit.description(UPDATED_DESCRIPTION);

        restBusinessunitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessunit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessunit))
            )
            .andExpect(status().isOk());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeUpdate);
        Businessunit testBusinessunit = businessunitList.get(businessunitList.size() - 1);
        assertThat(testBusinessunit.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBusinessunit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusinessunit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateBusinessunitWithPatch() throws Exception {
        // Initialize the database
        businessunitRepository.saveAndFlush(businessunit);

        int databaseSizeBeforeUpdate = businessunitRepository.findAll().size();

        // Update the businessunit using partial update
        Businessunit partialUpdatedBusinessunit = new Businessunit();
        partialUpdatedBusinessunit.setId(businessunit.getId());

        partialUpdatedBusinessunit.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restBusinessunitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessunit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessunit))
            )
            .andExpect(status().isOk());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeUpdate);
        Businessunit testBusinessunit = businessunitList.get(businessunitList.size() - 1);
        assertThat(testBusinessunit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBusinessunit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessunit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingBusinessunit() throws Exception {
        int databaseSizeBeforeUpdate = businessunitRepository.findAll().size();
        businessunit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessunitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessunit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessunit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessunit() throws Exception {
        int databaseSizeBeforeUpdate = businessunitRepository.findAll().size();
        businessunit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessunitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessunit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessunit() throws Exception {
        int databaseSizeBeforeUpdate = businessunitRepository.findAll().size();
        businessunit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessunitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(businessunit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Businessunit in the database
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusinessunit() throws Exception {
        // Initialize the database
        businessunitRepository.saveAndFlush(businessunit);

        int databaseSizeBeforeDelete = businessunitRepository.findAll().size();

        // Delete the businessunit
        restBusinessunitMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessunit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Businessunit> businessunitList = businessunitRepository.findAll();
        assertThat(businessunitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
