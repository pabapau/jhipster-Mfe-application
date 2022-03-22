package com.paba.mfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paba.mfe.IntegrationTest;
import com.paba.mfe.domain.XProt;
import com.paba.mfe.domain.enumeration.Buildstate;
import com.paba.mfe.domain.enumeration.XProttype;
import com.paba.mfe.domain.enumeration.Xrole;
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

    private static final XProttype DEFAULT_XPROTTYPE = XProttype.PESITANY;
    private static final XProttype UPDATED_XPROTTYPE = XProttype.PESITSSL;

    private static final Xrole DEFAULT_XROLE = Xrole.CLI;
    private static final Xrole UPDATED_XROLE = Xrole.SRV;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_ACCESS_SERVICE_POINT = 1;
    private static final Integer UPDATED_ACCESS_SERVICE_POINT = 2;

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
            .xprottype(DEFAULT_XPROTTYPE)
            .xrole(DEFAULT_XROLE)
            .comment(DEFAULT_COMMENT)
            .accessAddress(DEFAULT_ACCESS_ADDRESS)
            .accessServicePoint(DEFAULT_ACCESS_SERVICE_POINT)
            .creationdate(DEFAULT_CREATIONDATE)
            .lastupdated(DEFAULT_LASTUPDATED)
            .buildstate(DEFAULT_BUILDSTATE)
            .buildcount(DEFAULT_BUILDCOUNT)
            .buildcomment(DEFAULT_BUILDCOMMENT);
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
            .xprottype(UPDATED_XPROTTYPE)
            .xrole(UPDATED_XROLE)
            .comment(UPDATED_COMMENT)
            .accessAddress(UPDATED_ACCESS_ADDRESS)
            .accessServicePoint(UPDATED_ACCESS_SERVICE_POINT)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildstate(UPDATED_BUILDSTATE)
            .buildcount(UPDATED_BUILDCOUNT)
            .buildcomment(UPDATED_BUILDCOMMENT);
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
        assertThat(testXProt.getXprottype()).isEqualTo(DEFAULT_XPROTTYPE);
        assertThat(testXProt.getXrole()).isEqualTo(DEFAULT_XROLE);
        assertThat(testXProt.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testXProt.getAccessAddress()).isEqualTo(DEFAULT_ACCESS_ADDRESS);
        assertThat(testXProt.getAccessServicePoint()).isEqualTo(DEFAULT_ACCESS_SERVICE_POINT);
        assertThat(testXProt.getCreationdate()).isEqualTo(DEFAULT_CREATIONDATE);
        assertThat(testXProt.getLastupdated()).isEqualTo(DEFAULT_LASTUPDATED);
        assertThat(testXProt.getBuildstate()).isEqualTo(DEFAULT_BUILDSTATE);
        assertThat(testXProt.getBuildcount()).isEqualTo(DEFAULT_BUILDCOUNT);
        assertThat(testXProt.getBuildcomment()).isEqualTo(DEFAULT_BUILDCOMMENT);
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
    void checkXprottypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = xProtRepository.findAll().size();
        // set the field null
        xProt.setXprottype(null);

        // Create the XProt, which fails.

        restXProtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isBadRequest());

        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkXroleIsRequired() throws Exception {
        int databaseSizeBeforeTest = xProtRepository.findAll().size();
        // set the field null
        xProt.setXrole(null);

        // Create the XProt, which fails.

        restXProtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isBadRequest());

        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = xProtRepository.findAll().size();
        // set the field null
        xProt.setCreationdate(null);

        // Create the XProt, which fails.

        restXProtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xProt)))
            .andExpect(status().isBadRequest());

        List<XProt> xProtList = xProtRepository.findAll();
        assertThat(xProtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastupdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = xProtRepository.findAll().size();
        // set the field null
        xProt.setLastupdated(null);

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
            .andExpect(jsonPath("$.[*].xprottype").value(hasItem(DEFAULT_XPROTTYPE.toString())))
            .andExpect(jsonPath("$.[*].xrole").value(hasItem(DEFAULT_XROLE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].accessAddress").value(hasItem(DEFAULT_ACCESS_ADDRESS)))
            .andExpect(jsonPath("$.[*].accessServicePoint").value(hasItem(DEFAULT_ACCESS_SERVICE_POINT)))
            .andExpect(jsonPath("$.[*].creationdate").value(hasItem(DEFAULT_CREATIONDATE.toString())))
            .andExpect(jsonPath("$.[*].lastupdated").value(hasItem(DEFAULT_LASTUPDATED.toString())))
            .andExpect(jsonPath("$.[*].buildstate").value(hasItem(DEFAULT_BUILDSTATE.toString())))
            .andExpect(jsonPath("$.[*].buildcount").value(hasItem(DEFAULT_BUILDCOUNT)))
            .andExpect(jsonPath("$.[*].buildcomment").value(hasItem(DEFAULT_BUILDCOMMENT)));
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
            .andExpect(jsonPath("$.xprottype").value(DEFAULT_XPROTTYPE.toString()))
            .andExpect(jsonPath("$.xrole").value(DEFAULT_XROLE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.accessAddress").value(DEFAULT_ACCESS_ADDRESS))
            .andExpect(jsonPath("$.accessServicePoint").value(DEFAULT_ACCESS_SERVICE_POINT))
            .andExpect(jsonPath("$.creationdate").value(DEFAULT_CREATIONDATE.toString()))
            .andExpect(jsonPath("$.lastupdated").value(DEFAULT_LASTUPDATED.toString()))
            .andExpect(jsonPath("$.buildstate").value(DEFAULT_BUILDSTATE.toString()))
            .andExpect(jsonPath("$.buildcount").value(DEFAULT_BUILDCOUNT))
            .andExpect(jsonPath("$.buildcomment").value(DEFAULT_BUILDCOMMENT));
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
            .xprottype(UPDATED_XPROTTYPE)
            .xrole(UPDATED_XROLE)
            .comment(UPDATED_COMMENT)
            .accessAddress(UPDATED_ACCESS_ADDRESS)
            .accessServicePoint(UPDATED_ACCESS_SERVICE_POINT)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildstate(UPDATED_BUILDSTATE)
            .buildcount(UPDATED_BUILDCOUNT)
            .buildcomment(UPDATED_BUILDCOMMENT);

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
        assertThat(testXProt.getXprottype()).isEqualTo(UPDATED_XPROTTYPE);
        assertThat(testXProt.getXrole()).isEqualTo(UPDATED_XROLE);
        assertThat(testXProt.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testXProt.getAccessAddress()).isEqualTo(UPDATED_ACCESS_ADDRESS);
        assertThat(testXProt.getAccessServicePoint()).isEqualTo(UPDATED_ACCESS_SERVICE_POINT);
        assertThat(testXProt.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testXProt.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testXProt.getBuildstate()).isEqualTo(UPDATED_BUILDSTATE);
        assertThat(testXProt.getBuildcount()).isEqualTo(UPDATED_BUILDCOUNT);
        assertThat(testXProt.getBuildcomment()).isEqualTo(UPDATED_BUILDCOMMENT);
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
            .xprottype(UPDATED_XPROTTYPE)
            .xrole(UPDATED_XROLE)
            .comment(UPDATED_COMMENT)
            .accessAddress(UPDATED_ACCESS_ADDRESS)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildcomment(UPDATED_BUILDCOMMENT);

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
        assertThat(testXProt.getXprottype()).isEqualTo(UPDATED_XPROTTYPE);
        assertThat(testXProt.getXrole()).isEqualTo(UPDATED_XROLE);
        assertThat(testXProt.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testXProt.getAccessAddress()).isEqualTo(UPDATED_ACCESS_ADDRESS);
        assertThat(testXProt.getAccessServicePoint()).isEqualTo(DEFAULT_ACCESS_SERVICE_POINT);
        assertThat(testXProt.getCreationdate()).isEqualTo(DEFAULT_CREATIONDATE);
        assertThat(testXProt.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testXProt.getBuildstate()).isEqualTo(DEFAULT_BUILDSTATE);
        assertThat(testXProt.getBuildcount()).isEqualTo(DEFAULT_BUILDCOUNT);
        assertThat(testXProt.getBuildcomment()).isEqualTo(UPDATED_BUILDCOMMENT);
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
            .xprottype(UPDATED_XPROTTYPE)
            .xrole(UPDATED_XROLE)
            .comment(UPDATED_COMMENT)
            .accessAddress(UPDATED_ACCESS_ADDRESS)
            .accessServicePoint(UPDATED_ACCESS_SERVICE_POINT)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .buildstate(UPDATED_BUILDSTATE)
            .buildcount(UPDATED_BUILDCOUNT)
            .buildcomment(UPDATED_BUILDCOMMENT);

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
        assertThat(testXProt.getXprottype()).isEqualTo(UPDATED_XPROTTYPE);
        assertThat(testXProt.getXrole()).isEqualTo(UPDATED_XROLE);
        assertThat(testXProt.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testXProt.getAccessAddress()).isEqualTo(UPDATED_ACCESS_ADDRESS);
        assertThat(testXProt.getAccessServicePoint()).isEqualTo(UPDATED_ACCESS_SERVICE_POINT);
        assertThat(testXProt.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testXProt.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testXProt.getBuildstate()).isEqualTo(UPDATED_BUILDSTATE);
        assertThat(testXProt.getBuildcount()).isEqualTo(UPDATED_BUILDCOUNT);
        assertThat(testXProt.getBuildcomment()).isEqualTo(UPDATED_BUILDCOMMENT);
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
