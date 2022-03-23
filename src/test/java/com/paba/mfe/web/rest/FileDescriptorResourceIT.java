package com.paba.mfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paba.mfe.IntegrationTest;
import com.paba.mfe.domain.FileDescriptor;
import com.paba.mfe.domain.enumeration.BuildState;
import com.paba.mfe.domain.enumeration.FlowUseCase;
import com.paba.mfe.repository.FileDescriptorRepository;
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
 * Integration tests for the {@link FileDescriptorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileDescriptorResourceIT {

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

    private static final String ENTITY_API_URL = "/api/file-descriptors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileDescriptorRepository fileDescriptorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileDescriptorMockMvc;

    private FileDescriptor fileDescriptor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileDescriptor createEntity(EntityManager em) {
        FileDescriptor fileDescriptor = new FileDescriptor()
            .fileIdent(DEFAULT_FILE_IDENT)
            .flowUseCase(DEFAULT_FLOW_USE_CASE)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .buildState(DEFAULT_BUILD_STATE)
            .buildCount(DEFAULT_BUILD_COUNT)
            .buildComment(DEFAULT_BUILD_COMMENT);
        return fileDescriptor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileDescriptor createUpdatedEntity(EntityManager em) {
        FileDescriptor fileDescriptor = new FileDescriptor()
            .fileIdent(UPDATED_FILE_IDENT)
            .flowUseCase(UPDATED_FLOW_USE_CASE)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);
        return fileDescriptor;
    }

    @BeforeEach
    public void initTest() {
        fileDescriptor = createEntity(em);
    }

    @Test
    @Transactional
    void createFileDescriptor() throws Exception {
        int databaseSizeBeforeCreate = fileDescriptorRepository.findAll().size();
        // Create the FileDescriptor
        restFileDescriptorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isCreated());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeCreate + 1);
        FileDescriptor testFileDescriptor = fileDescriptorList.get(fileDescriptorList.size() - 1);
        assertThat(testFileDescriptor.getFileIdent()).isEqualTo(DEFAULT_FILE_IDENT);
        assertThat(testFileDescriptor.getFlowUseCase()).isEqualTo(DEFAULT_FLOW_USE_CASE);
        assertThat(testFileDescriptor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFileDescriptor.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFileDescriptor.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testFileDescriptor.getBuildState()).isEqualTo(DEFAULT_BUILD_STATE);
        assertThat(testFileDescriptor.getBuildCount()).isEqualTo(DEFAULT_BUILD_COUNT);
        assertThat(testFileDescriptor.getBuildComment()).isEqualTo(DEFAULT_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void createFileDescriptorWithExistingId() throws Exception {
        // Create the FileDescriptor with an existing ID
        fileDescriptor.setId(1L);

        int databaseSizeBeforeCreate = fileDescriptorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileDescriptorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFileIdentIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileDescriptorRepository.findAll().size();
        // set the field null
        fileDescriptor.setFileIdent(null);

        // Create the FileDescriptor, which fails.

        restFileDescriptorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isBadRequest());

        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFlowUseCaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileDescriptorRepository.findAll().size();
        // set the field null
        fileDescriptor.setFlowUseCase(null);

        // Create the FileDescriptor, which fails.

        restFileDescriptorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isBadRequest());

        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileDescriptorRepository.findAll().size();
        // set the field null
        fileDescriptor.setCreationDate(null);

        // Create the FileDescriptor, which fails.

        restFileDescriptorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isBadRequest());

        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileDescriptorRepository.findAll().size();
        // set the field null
        fileDescriptor.setLastUpdated(null);

        // Create the FileDescriptor, which fails.

        restFileDescriptorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isBadRequest());

        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFileDescriptors() throws Exception {
        // Initialize the database
        fileDescriptorRepository.saveAndFlush(fileDescriptor);

        // Get all the fileDescriptorList
        restFileDescriptorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileDescriptor.getId().intValue())))
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
    void getFileDescriptor() throws Exception {
        // Initialize the database
        fileDescriptorRepository.saveAndFlush(fileDescriptor);

        // Get the fileDescriptor
        restFileDescriptorMockMvc
            .perform(get(ENTITY_API_URL_ID, fileDescriptor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileDescriptor.getId().intValue()))
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
    void getNonExistingFileDescriptor() throws Exception {
        // Get the fileDescriptor
        restFileDescriptorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFileDescriptor() throws Exception {
        // Initialize the database
        fileDescriptorRepository.saveAndFlush(fileDescriptor);

        int databaseSizeBeforeUpdate = fileDescriptorRepository.findAll().size();

        // Update the fileDescriptor
        FileDescriptor updatedFileDescriptor = fileDescriptorRepository.findById(fileDescriptor.getId()).get();
        // Disconnect from session so that the updates on updatedFileDescriptor are not directly saved in db
        em.detach(updatedFileDescriptor);
        updatedFileDescriptor
            .fileIdent(UPDATED_FILE_IDENT)
            .flowUseCase(UPDATED_FLOW_USE_CASE)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);

        restFileDescriptorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFileDescriptor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFileDescriptor))
            )
            .andExpect(status().isOk());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeUpdate);
        FileDescriptor testFileDescriptor = fileDescriptorList.get(fileDescriptorList.size() - 1);
        assertThat(testFileDescriptor.getFileIdent()).isEqualTo(UPDATED_FILE_IDENT);
        assertThat(testFileDescriptor.getFlowUseCase()).isEqualTo(UPDATED_FLOW_USE_CASE);
        assertThat(testFileDescriptor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFileDescriptor.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFileDescriptor.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFileDescriptor.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testFileDescriptor.getBuildCount()).isEqualTo(UPDATED_BUILD_COUNT);
        assertThat(testFileDescriptor.getBuildComment()).isEqualTo(UPDATED_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingFileDescriptor() throws Exception {
        int databaseSizeBeforeUpdate = fileDescriptorRepository.findAll().size();
        fileDescriptor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileDescriptorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileDescriptor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileDescriptor() throws Exception {
        int databaseSizeBeforeUpdate = fileDescriptorRepository.findAll().size();
        fileDescriptor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDescriptorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileDescriptor() throws Exception {
        int databaseSizeBeforeUpdate = fileDescriptorRepository.findAll().size();
        fileDescriptor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDescriptorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDescriptor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileDescriptorWithPatch() throws Exception {
        // Initialize the database
        fileDescriptorRepository.saveAndFlush(fileDescriptor);

        int databaseSizeBeforeUpdate = fileDescriptorRepository.findAll().size();

        // Update the fileDescriptor using partial update
        FileDescriptor partialUpdatedFileDescriptor = new FileDescriptor();
        partialUpdatedFileDescriptor.setId(fileDescriptor.getId());

        partialUpdatedFileDescriptor.fileIdent(UPDATED_FILE_IDENT).description(UPDATED_DESCRIPTION).buildState(UPDATED_BUILD_STATE);

        restFileDescriptorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileDescriptor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileDescriptor))
            )
            .andExpect(status().isOk());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeUpdate);
        FileDescriptor testFileDescriptor = fileDescriptorList.get(fileDescriptorList.size() - 1);
        assertThat(testFileDescriptor.getFileIdent()).isEqualTo(UPDATED_FILE_IDENT);
        assertThat(testFileDescriptor.getFlowUseCase()).isEqualTo(DEFAULT_FLOW_USE_CASE);
        assertThat(testFileDescriptor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFileDescriptor.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFileDescriptor.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testFileDescriptor.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testFileDescriptor.getBuildCount()).isEqualTo(DEFAULT_BUILD_COUNT);
        assertThat(testFileDescriptor.getBuildComment()).isEqualTo(DEFAULT_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateFileDescriptorWithPatch() throws Exception {
        // Initialize the database
        fileDescriptorRepository.saveAndFlush(fileDescriptor);

        int databaseSizeBeforeUpdate = fileDescriptorRepository.findAll().size();

        // Update the fileDescriptor using partial update
        FileDescriptor partialUpdatedFileDescriptor = new FileDescriptor();
        partialUpdatedFileDescriptor.setId(fileDescriptor.getId());

        partialUpdatedFileDescriptor
            .fileIdent(UPDATED_FILE_IDENT)
            .flowUseCase(UPDATED_FLOW_USE_CASE)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .buildState(UPDATED_BUILD_STATE)
            .buildCount(UPDATED_BUILD_COUNT)
            .buildComment(UPDATED_BUILD_COMMENT);

        restFileDescriptorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileDescriptor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileDescriptor))
            )
            .andExpect(status().isOk());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeUpdate);
        FileDescriptor testFileDescriptor = fileDescriptorList.get(fileDescriptorList.size() - 1);
        assertThat(testFileDescriptor.getFileIdent()).isEqualTo(UPDATED_FILE_IDENT);
        assertThat(testFileDescriptor.getFlowUseCase()).isEqualTo(UPDATED_FLOW_USE_CASE);
        assertThat(testFileDescriptor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFileDescriptor.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFileDescriptor.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFileDescriptor.getBuildState()).isEqualTo(UPDATED_BUILD_STATE);
        assertThat(testFileDescriptor.getBuildCount()).isEqualTo(UPDATED_BUILD_COUNT);
        assertThat(testFileDescriptor.getBuildComment()).isEqualTo(UPDATED_BUILD_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingFileDescriptor() throws Exception {
        int databaseSizeBeforeUpdate = fileDescriptorRepository.findAll().size();
        fileDescriptor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileDescriptorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileDescriptor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileDescriptor() throws Exception {
        int databaseSizeBeforeUpdate = fileDescriptorRepository.findAll().size();
        fileDescriptor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDescriptorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileDescriptor() throws Exception {
        int databaseSizeBeforeUpdate = fileDescriptorRepository.findAll().size();
        fileDescriptor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDescriptorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fileDescriptor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileDescriptor in the database
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFileDescriptor() throws Exception {
        // Initialize the database
        fileDescriptorRepository.saveAndFlush(fileDescriptor);

        int databaseSizeBeforeDelete = fileDescriptorRepository.findAll().size();

        // Delete the fileDescriptor
        restFileDescriptorMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileDescriptor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileDescriptor> fileDescriptorList = fileDescriptorRepository.findAll();
        assertThat(fileDescriptorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
