package com.gregdm.javart.web.rest;

import com.gregdm.javart.JavartApp;
import com.gregdm.javart.domain.Commentary;
import com.gregdm.javart.repository.CommentaryRepository;
import com.gregdm.javart.service.CommentaryService;
import com.gregdm.javart.web.rest.dto.CommentaryDTO;
import com.gregdm.javart.web.rest.mapper.CommentaryMapper;

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
 * Test class for the CommentaryResource REST controller.
 *
 * @see CommentaryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JavartApp.class)
@WebAppConfiguration
@IntegrationTest
public class CommentaryResourceIntTest {

    private static final String DEFAULT_WORK_ID = "AAAAA";
    private static final String UPDATED_WORK_ID = "BBBBB";

    private static final Boolean DEFAULT_VALIDATED = false;
    private static final Boolean UPDATED_VALIDATED = true;
    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private CommentaryRepository commentaryRepository;

    @Inject
    private CommentaryMapper commentaryMapper;

    @Inject
    private CommentaryService commentaryService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommentaryMockMvc;

    private Commentary commentary;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommentaryResource commentaryResource = new CommentaryResource();
        ReflectionTestUtils.setField(commentaryResource, "commentaryService", commentaryService);
        ReflectionTestUtils.setField(commentaryResource, "commentaryMapper", commentaryMapper);
        this.restCommentaryMockMvc = MockMvcBuilders.standaloneSetup(commentaryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        commentary = new Commentary();
        commentary.setWorkId(DEFAULT_WORK_ID);
        commentary.setValidated(DEFAULT_VALIDATED);
        commentary.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createCommentary() throws Exception {
        int databaseSizeBeforeCreate = commentaryRepository.findAll().size();

        // Create the Commentary
        CommentaryDTO commentaryDTO = commentaryMapper.commentaryToCommentaryDTO(commentary);

        restCommentaryMockMvc.perform(post("/api/commentaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentaryDTO)))
                .andExpect(status().isCreated());

        // Validate the Commentary in the database
        List<Commentary> commentaries = commentaryRepository.findAll();
        assertThat(commentaries).hasSize(databaseSizeBeforeCreate + 1);
        Commentary testCommentary = commentaries.get(commentaries.size() - 1);
        assertThat(testCommentary.getWorkId()).isEqualTo(DEFAULT_WORK_ID);
        assertThat(testCommentary.isValidated()).isEqualTo(DEFAULT_VALIDATED);
        assertThat(testCommentary.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllCommentaries() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get all the commentaries
        restCommentaryMockMvc.perform(get("/api/commentaries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(commentary.getId().intValue())))
                .andExpect(jsonPath("$.[*].workId").value(hasItem(DEFAULT_WORK_ID.toString())))
                .andExpect(jsonPath("$.[*].validated").value(hasItem(DEFAULT_VALIDATED.booleanValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getCommentary() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get the commentary
        restCommentaryMockMvc.perform(get("/api/commentaries/{id}", commentary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(commentary.getId().intValue()))
            .andExpect(jsonPath("$.workId").value(DEFAULT_WORK_ID.toString()))
            .andExpect(jsonPath("$.validated").value(DEFAULT_VALIDATED.booleanValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommentary() throws Exception {
        // Get the commentary
        restCommentaryMockMvc.perform(get("/api/commentaries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommentary() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);
        int databaseSizeBeforeUpdate = commentaryRepository.findAll().size();

        // Update the commentary
        Commentary updatedCommentary = new Commentary();
        updatedCommentary.setId(commentary.getId());
        updatedCommentary.setWorkId(UPDATED_WORK_ID);
        updatedCommentary.setValidated(UPDATED_VALIDATED);
        updatedCommentary.setValue(UPDATED_VALUE);
        CommentaryDTO commentaryDTO = commentaryMapper.commentaryToCommentaryDTO(updatedCommentary);

        restCommentaryMockMvc.perform(put("/api/commentaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentaryDTO)))
                .andExpect(status().isOk());

        // Validate the Commentary in the database
        List<Commentary> commentaries = commentaryRepository.findAll();
        assertThat(commentaries).hasSize(databaseSizeBeforeUpdate);
        Commentary testCommentary = commentaries.get(commentaries.size() - 1);
        assertThat(testCommentary.getWorkId()).isEqualTo(UPDATED_WORK_ID);
        assertThat(testCommentary.isValidated()).isEqualTo(UPDATED_VALIDATED);
        assertThat(testCommentary.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteCommentary() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);
        int databaseSizeBeforeDelete = commentaryRepository.findAll().size();

        // Get the commentary
        restCommentaryMockMvc.perform(delete("/api/commentaries/{id}", commentary.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Commentary> commentaries = commentaryRepository.findAll();
        assertThat(commentaries).hasSize(databaseSizeBeforeDelete - 1);
    }
}
