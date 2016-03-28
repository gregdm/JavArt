package com.gregdm.javart.web.rest;

import com.gregdm.javart.JavartApp;
import com.gregdm.javart.domain.Album;
import com.gregdm.javart.repository.AlbumRepository;
import com.gregdm.javart.service.AlbumService;
import com.gregdm.javart.web.rest.dto.AlbumDTO;
import com.gregdm.javart.web.rest.mapper.AlbumMapper;

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
 * Test class for the AlbumResource REST controller.
 *
 * @see AlbumResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JavartApp.class)
@WebAppConfiguration
@IntegrationTest
public class AlbumResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private AlbumRepository albumRepository;

    @Inject
    private AlbumMapper albumMapper;

    @Inject
    private AlbumService albumService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAlbumMockMvc;

    private Album album;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AlbumResource albumResource = new AlbumResource();
        ReflectionTestUtils.setField(albumResource, "albumService", albumService);
        ReflectionTestUtils.setField(albumResource, "albumMapper", albumMapper);
        this.restAlbumMockMvc = MockMvcBuilders.standaloneSetup(albumResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        album = new Album();
        album.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAlbum() throws Exception {
        int databaseSizeBeforeCreate = albumRepository.findAll().size();

        // Create the Album
        AlbumDTO albumDTO = albumMapper.albumToAlbumDTO(album);

        restAlbumMockMvc.perform(post("/api/albums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
                .andExpect(status().isCreated());

        // Validate the Album in the database
        List<Album> albums = albumRepository.findAll();
        assertThat(albums).hasSize(databaseSizeBeforeCreate + 1);
        Album testAlbum = albums.get(albums.size() - 1);
        assertThat(testAlbum.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllAlbums() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albums
        restAlbumMockMvc.perform(get("/api/albums?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get the album
        restAlbumMockMvc.perform(get("/api/albums/{id}", album.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(album.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAlbum() throws Exception {
        // Get the album
        restAlbumMockMvc.perform(get("/api/albums/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Update the album
        Album updatedAlbum = new Album();
        updatedAlbum.setId(album.getId());
        updatedAlbum.setName(UPDATED_NAME);
        AlbumDTO albumDTO = albumMapper.albumToAlbumDTO(updatedAlbum);

        restAlbumMockMvc.perform(put("/api/albums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
                .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> albums = albumRepository.findAll();
        assertThat(albums).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = albums.get(albums.size() - 1);
        assertThat(testAlbum.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);
        int databaseSizeBeforeDelete = albumRepository.findAll().size();

        // Get the album
        restAlbumMockMvc.perform(delete("/api/albums/{id}", album.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Album> albums = albumRepository.findAll();
        assertThat(albums).hasSize(databaseSizeBeforeDelete - 1);
    }
}
