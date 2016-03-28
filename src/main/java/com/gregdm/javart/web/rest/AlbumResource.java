package com.gregdm.javart.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.javart.domain.Album;
import com.gregdm.javart.service.AlbumService;
import com.gregdm.javart.web.rest.util.HeaderUtil;
import com.gregdm.javart.web.rest.util.PaginationUtil;
import com.gregdm.javart.web.rest.dto.AlbumDTO;
import com.gregdm.javart.web.rest.mapper.AlbumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Album.
 */
@RestController
@RequestMapping("/api")
public class AlbumResource {

    private final Logger log = LoggerFactory.getLogger(AlbumResource.class);
        
    @Inject
    private AlbumService albumService;
    
    @Inject
    private AlbumMapper albumMapper;
    
    /**
     * POST  /albums : Create a new album.
     *
     * @param albumDTO the albumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new albumDTO, or with status 400 (Bad Request) if the album has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/albums",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AlbumDTO> createAlbum(@RequestBody AlbumDTO albumDTO) throws URISyntaxException {
        log.debug("REST request to save Album : {}", albumDTO);
        if (albumDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("album", "idexists", "A new album cannot already have an ID")).body(null);
        }
        AlbumDTO result = albumService.save(albumDTO);
        return ResponseEntity.created(new URI("/api/albums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("album", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /albums : Updates an existing album.
     *
     * @param albumDTO the albumDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated albumDTO,
     * or with status 400 (Bad Request) if the albumDTO is not valid,
     * or with status 500 (Internal Server Error) if the albumDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/albums",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AlbumDTO> updateAlbum(@RequestBody AlbumDTO albumDTO) throws URISyntaxException {
        log.debug("REST request to update Album : {}", albumDTO);
        if (albumDTO.getId() == null) {
            return createAlbum(albumDTO);
        }
        AlbumDTO result = albumService.save(albumDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("album", albumDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /albums : get all the albums.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of albums in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/albums",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AlbumDTO>> getAllAlbums(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Albums");
        Page<Album> page = albumService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/albums");
        return new ResponseEntity<>(albumMapper.albumsToAlbumDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /albums/:id : get the "id" album.
     *
     * @param id the id of the albumDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the albumDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/albums/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AlbumDTO> getAlbum(@PathVariable Long id) {
        log.debug("REST request to get Album : {}", id);
        AlbumDTO albumDTO = albumService.findOne(id);
        return Optional.ofNullable(albumDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /albums/:id : delete the "id" album.
     *
     * @param id the id of the albumDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/albums/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        log.debug("REST request to delete Album : {}", id);
        albumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("album", id.toString())).build();
    }

}
