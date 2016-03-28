package com.gregdm.javart.web.rest;

import com.gregdm.javart.JavartApp;
import com.gregdm.javart.domain.SavedWork;
import com.gregdm.javart.repository.SavedWorkRepository;
import com.gregdm.javart.service.SavedWorkService;
import com.gregdm.javart.web.rest.dto.SavedWorkDTO;
import com.gregdm.javart.web.rest.mapper.SavedWorkMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SavedWorkResource REST controller.
 *
 * @see SavedWorkResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JavartApp.class)
@WebAppConfiguration
@IntegrationTest
public class SavedWorkResourceIntTest {

    private static final String DEFAULT_WORK_ID = "AAAAA";
    private static final String UPDATED_WORK_ID = "BBBBB";

    @Inject
    private SavedWorkRepository savedWorkRepository;

    @Inject
    private SavedWorkMapper savedWorkMapper;

    @Inject
    private SavedWorkService savedWorkService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSavedWorkMockMvc;

    private SavedWork savedWork;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SavedWorkResource savedWorkResource = new SavedWorkResource();
        ReflectionTestUtils.setField(savedWorkResource, "savedWorkService", savedWorkService);
        ReflectionTestUtils.setField(savedWorkResource, "savedWorkMapper", savedWorkMapper);
        this.restSavedWorkMockMvc = MockMvcBuilders.standaloneSetup(savedWorkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        savedWork = new SavedWork();
        savedWork.setWorkId(DEFAULT_WORK_ID);
    }

    @Test
    @Transactional
    public void createSavedWork() throws Exception {
        int databaseSizeBeforeCreate = savedWorkRepository.findAll().size();

        // Create the SavedWork
        SavedWorkDTO savedWorkDTO = savedWorkMapper.savedWorkToSavedWorkDTO(savedWork);

        restSavedWorkMockMvc.perform(post("/api/saved-works")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(savedWorkDTO)))
                .andExpect(status().isCreated());

        // Validate the SavedWork in the database
        List<SavedWork> savedWorks = savedWorkRepository.findAll();
        assertThat(savedWorks).hasSize(databaseSizeBeforeCreate + 1);
        SavedWork testSavedWork = savedWorks.get(savedWorks.size() - 1);
        assertThat(testSavedWork.getWorkId()).isEqualTo(DEFAULT_WORK_ID);
    }

    @Test
    @Transactional
    public void getAllSavedWorks() throws Exception {
        // Initialize the database
        savedWorkRepository.saveAndFlush(savedWork);

        // Get all the savedWorks
        restSavedWorkMockMvc.perform(get("/api/saved-works?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(savedWork.getId().intValue())))
                .andExpect(jsonPath("$.[*].workId").value(hasItem(DEFAULT_WORK_ID.toString())));
    }

    @Test
    @Transactional
    public void getSavedWork() throws Exception {
        // Initialize the database
        savedWorkRepository.saveAndFlush(savedWork);

        // Get the savedWork
        restSavedWorkMockMvc.perform(get("/api/saved-works/{id}", savedWork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(savedWork.getId().intValue()))
            .andExpect(jsonPath("$.workId").value(DEFAULT_WORK_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSavedWork() throws Exception {
        // Get the savedWork
        restSavedWorkMockMvc.perform(get("/api/saved-works/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSavedWork() throws Exception {
        // Initialize the database
        savedWorkRepository.saveAndFlush(savedWork);
        int databaseSizeBeforeUpdate = savedWorkRepository.findAll().size();

        // Update the savedWork
        SavedWork updatedSavedWork = new SavedWork();
        updatedSavedWork.setId(savedWork.getId());
        updatedSavedWork.setWorkId(UPDATED_WORK_ID);
        SavedWorkDTO savedWorkDTO = savedWorkMapper.savedWorkToSavedWorkDTO(updatedSavedWork);

        restSavedWorkMockMvc.perform(put("/api/saved-works")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(savedWorkDTO)))
                .andExpect(status().isOk());

        // Validate the SavedWork in the database
        List<SavedWork> savedWorks = savedWorkRepository.findAll();
        assertThat(savedWorks).hasSize(databaseSizeBeforeUpdate);
        SavedWork testSavedWork = savedWorks.get(savedWorks.size() - 1);
        assertThat(testSavedWork.getWorkId()).isEqualTo(UPDATED_WORK_ID);
    }

    @Test
    @Transactional
    public void deleteSavedWork() throws Exception {
        // Initialize the database
        savedWorkRepository.saveAndFlush(savedWork);
        int databaseSizeBeforeDelete = savedWorkRepository.findAll().size();

        // Get the savedWork
        restSavedWorkMockMvc.perform(delete("/api/saved-works/{id}", savedWork.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SavedWork> savedWorks = savedWorkRepository.findAll();
        assertThat(savedWorks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
