package com.gregdm.javart.service;

import com.gregdm.javart.domain.Album;
import com.gregdm.javart.repository.AlbumRepository;
import com.gregdm.javart.web.rest.dto.AlbumDTO;
import com.gregdm.javart.web.rest.mapper.AlbumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Album.
 */
@Service
@Transactional
public class AlbumService {

    private final Logger log = LoggerFactory.getLogger(AlbumService.class);
    
    @Inject
    private AlbumRepository albumRepository;
    
    @Inject
    private AlbumMapper albumMapper;
    
    /**
     * Save a album.
     * 
     * @param albumDTO the entity to save
     * @return the persisted entity
     */
    public AlbumDTO save(AlbumDTO albumDTO) {
        log.debug("Request to save Album : {}", albumDTO);
        Album album = albumMapper.albumDTOToAlbum(albumDTO);
        album = albumRepository.save(album);
        AlbumDTO result = albumMapper.albumToAlbumDTO(album);
        return result;
    }

    /**
     *  Get all the albums.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Album> findAll(Pageable pageable) {
        log.debug("Request to get all Albums");
        Page<Album> result = albumRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one album by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AlbumDTO findOne(Long id) {
        log.debug("Request to get Album : {}", id);
        Album album = albumRepository.findOne(id);
        AlbumDTO albumDTO = albumMapper.albumToAlbumDTO(album);
        return albumDTO;
    }

    /**
     *  Delete the  album by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Album : {}", id);
        albumRepository.delete(id);
    }
}
