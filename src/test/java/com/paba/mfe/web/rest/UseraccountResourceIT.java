package com.paba.mfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paba.mfe.IntegrationTest;
import com.paba.mfe.domain.User;
import com.paba.mfe.domain.Useraccount;
import com.paba.mfe.domain.enumeration.Accounttype;
import com.paba.mfe.repository.UseraccountRepository;
import com.paba.mfe.service.UseraccountService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UseraccountResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UseraccountResourceIT {

    private static final Accounttype DEFAULT_ACCOUNTTYPE = Accounttype.TRF;
    private static final Accounttype UPDATED_ACCOUNTTYPE = Accounttype.CONF;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATIONDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATIONDATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LASTUPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LASTUPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/useraccounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UseraccountRepository useraccountRepository;

    @Mock
    private UseraccountRepository useraccountRepositoryMock;

    @Mock
    private UseraccountService useraccountServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUseraccountMockMvc;

    private Useraccount useraccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Useraccount createEntity(EntityManager em) {
        Useraccount useraccount = new Useraccount()
            .accounttype(DEFAULT_ACCOUNTTYPE)
            .comment(DEFAULT_COMMENT)
            .creationdate(DEFAULT_CREATIONDATE)
            .lastupdated(DEFAULT_LASTUPDATED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        useraccount.setUser(user);
        return useraccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Useraccount createUpdatedEntity(EntityManager em) {
        Useraccount useraccount = new Useraccount()
            .accounttype(UPDATED_ACCOUNTTYPE)
            .comment(UPDATED_COMMENT)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        useraccount.setUser(user);
        return useraccount;
    }

    @BeforeEach
    public void initTest() {
        useraccount = createEntity(em);
    }

    @Test
    @Transactional
    void createUseraccount() throws Exception {
        int databaseSizeBeforeCreate = useraccountRepository.findAll().size();
        // Create the Useraccount
        restUseraccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(useraccount)))
            .andExpect(status().isCreated());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeCreate + 1);
        Useraccount testUseraccount = useraccountList.get(useraccountList.size() - 1);
        assertThat(testUseraccount.getAccounttype()).isEqualTo(DEFAULT_ACCOUNTTYPE);
        assertThat(testUseraccount.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testUseraccount.getCreationdate()).isEqualTo(DEFAULT_CREATIONDATE);
        assertThat(testUseraccount.getLastupdated()).isEqualTo(DEFAULT_LASTUPDATED);
    }

    @Test
    @Transactional
    void createUseraccountWithExistingId() throws Exception {
        // Create the Useraccount with an existing ID
        useraccount.setId(1L);

        int databaseSizeBeforeCreate = useraccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUseraccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(useraccount)))
            .andExpect(status().isBadRequest());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAccounttypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = useraccountRepository.findAll().size();
        // set the field null
        useraccount.setAccounttype(null);

        // Create the Useraccount, which fails.

        restUseraccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(useraccount)))
            .andExpect(status().isBadRequest());

        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = useraccountRepository.findAll().size();
        // set the field null
        useraccount.setCreationdate(null);

        // Create the Useraccount, which fails.

        restUseraccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(useraccount)))
            .andExpect(status().isBadRequest());

        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastupdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = useraccountRepository.findAll().size();
        // set the field null
        useraccount.setLastupdated(null);

        // Create the Useraccount, which fails.

        restUseraccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(useraccount)))
            .andExpect(status().isBadRequest());

        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUseraccounts() throws Exception {
        // Initialize the database
        useraccountRepository.saveAndFlush(useraccount);

        // Get all the useraccountList
        restUseraccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(useraccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accounttype").value(hasItem(DEFAULT_ACCOUNTTYPE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].creationdate").value(hasItem(DEFAULT_CREATIONDATE.toString())))
            .andExpect(jsonPath("$.[*].lastupdated").value(hasItem(DEFAULT_LASTUPDATED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUseraccountsWithEagerRelationshipsIsEnabled() throws Exception {
        when(useraccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUseraccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(useraccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUseraccountsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(useraccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUseraccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(useraccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUseraccount() throws Exception {
        // Initialize the database
        useraccountRepository.saveAndFlush(useraccount);

        // Get the useraccount
        restUseraccountMockMvc
            .perform(get(ENTITY_API_URL_ID, useraccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(useraccount.getId().intValue()))
            .andExpect(jsonPath("$.accounttype").value(DEFAULT_ACCOUNTTYPE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.creationdate").value(DEFAULT_CREATIONDATE.toString()))
            .andExpect(jsonPath("$.lastupdated").value(DEFAULT_LASTUPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUseraccount() throws Exception {
        // Get the useraccount
        restUseraccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUseraccount() throws Exception {
        // Initialize the database
        useraccountRepository.saveAndFlush(useraccount);

        int databaseSizeBeforeUpdate = useraccountRepository.findAll().size();

        // Update the useraccount
        Useraccount updatedUseraccount = useraccountRepository.findById(useraccount.getId()).get();
        // Disconnect from session so that the updates on updatedUseraccount are not directly saved in db
        em.detach(updatedUseraccount);
        updatedUseraccount
            .accounttype(UPDATED_ACCOUNTTYPE)
            .comment(UPDATED_COMMENT)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED);

        restUseraccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUseraccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUseraccount))
            )
            .andExpect(status().isOk());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeUpdate);
        Useraccount testUseraccount = useraccountList.get(useraccountList.size() - 1);
        assertThat(testUseraccount.getAccounttype()).isEqualTo(UPDATED_ACCOUNTTYPE);
        assertThat(testUseraccount.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testUseraccount.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testUseraccount.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
    }

    @Test
    @Transactional
    void putNonExistingUseraccount() throws Exception {
        int databaseSizeBeforeUpdate = useraccountRepository.findAll().size();
        useraccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUseraccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, useraccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(useraccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUseraccount() throws Exception {
        int databaseSizeBeforeUpdate = useraccountRepository.findAll().size();
        useraccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUseraccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(useraccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUseraccount() throws Exception {
        int databaseSizeBeforeUpdate = useraccountRepository.findAll().size();
        useraccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUseraccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(useraccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUseraccountWithPatch() throws Exception {
        // Initialize the database
        useraccountRepository.saveAndFlush(useraccount);

        int databaseSizeBeforeUpdate = useraccountRepository.findAll().size();

        // Update the useraccount using partial update
        Useraccount partialUpdatedUseraccount = new Useraccount();
        partialUpdatedUseraccount.setId(useraccount.getId());

        partialUpdatedUseraccount
            .accounttype(UPDATED_ACCOUNTTYPE)
            .comment(UPDATED_COMMENT)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED);

        restUseraccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUseraccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUseraccount))
            )
            .andExpect(status().isOk());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeUpdate);
        Useraccount testUseraccount = useraccountList.get(useraccountList.size() - 1);
        assertThat(testUseraccount.getAccounttype()).isEqualTo(UPDATED_ACCOUNTTYPE);
        assertThat(testUseraccount.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testUseraccount.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testUseraccount.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
    }

    @Test
    @Transactional
    void fullUpdateUseraccountWithPatch() throws Exception {
        // Initialize the database
        useraccountRepository.saveAndFlush(useraccount);

        int databaseSizeBeforeUpdate = useraccountRepository.findAll().size();

        // Update the useraccount using partial update
        Useraccount partialUpdatedUseraccount = new Useraccount();
        partialUpdatedUseraccount.setId(useraccount.getId());

        partialUpdatedUseraccount
            .accounttype(UPDATED_ACCOUNTTYPE)
            .comment(UPDATED_COMMENT)
            .creationdate(UPDATED_CREATIONDATE)
            .lastupdated(UPDATED_LASTUPDATED);

        restUseraccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUseraccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUseraccount))
            )
            .andExpect(status().isOk());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeUpdate);
        Useraccount testUseraccount = useraccountList.get(useraccountList.size() - 1);
        assertThat(testUseraccount.getAccounttype()).isEqualTo(UPDATED_ACCOUNTTYPE);
        assertThat(testUseraccount.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testUseraccount.getCreationdate()).isEqualTo(UPDATED_CREATIONDATE);
        assertThat(testUseraccount.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
    }

    @Test
    @Transactional
    void patchNonExistingUseraccount() throws Exception {
        int databaseSizeBeforeUpdate = useraccountRepository.findAll().size();
        useraccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUseraccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, useraccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(useraccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUseraccount() throws Exception {
        int databaseSizeBeforeUpdate = useraccountRepository.findAll().size();
        useraccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUseraccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(useraccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUseraccount() throws Exception {
        int databaseSizeBeforeUpdate = useraccountRepository.findAll().size();
        useraccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUseraccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(useraccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Useraccount in the database
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUseraccount() throws Exception {
        // Initialize the database
        useraccountRepository.saveAndFlush(useraccount);

        int databaseSizeBeforeDelete = useraccountRepository.findAll().size();

        // Delete the useraccount
        restUseraccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, useraccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Useraccount> useraccountList = useraccountRepository.findAll();
        assertThat(useraccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
