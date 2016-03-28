package com.gregdm.javart.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.javart.domain.SavedWork;
import com.gregdm.javart.service.SavedWorkService;
import com.gregdm.javart.web.rest.util.HeaderUtil;
import com.gregdm.javart.web.rest.util.PaginationUtil;
import com.gregdm.javart.web.rest.dto.SavedWorkDTO;
import com.gregdm.javart.web.rest.mapper.SavedWorkMapper;
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
 * REST controller for managing SavedWork.
 */
@RestController
@RequestMapping("/api")
public class SavedWorkResource {

    private final Logger log = LoggerFactory.getLogger(SavedWorkResource.class);
        
    @Inject
    private SavedWorkService savedWorkService;
    
    @Inject
    private SavedWorkMapper savedWorkMapper;
    
    /**
     * POST  /saved-works : Create a new savedWork.
     *
     * @param savedWorkDTO the savedWorkDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new savedWorkDTO, or with status 400 (Bad Request) if the savedWork has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/saved-works",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SavedWorkDTO> createSavedWork(@RequestBody SavedWorkDTO savedWorkDTO) throws URISyntaxException {
        log.debug("REST request to save SavedWork : {}", savedWorkDTO);
        if (savedWorkDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("savedWork", "idexists", "A new savedWork cannot already have an ID")).body(null);
        }
        SavedWorkDTO result = savedWorkService.save(savedWorkDTO);
        return ResponseEntity.created(new URI("/api/saved-works/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("savedWork", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /saved-works : Updates an existing savedWork.
     *
     * @param savedWorkDTO the savedWorkDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated savedWorkDTO,
     * or with status 400 (Bad Request) if the savedWorkDTO is not valid,
     * or with status 500 (Internal Server Error) if the savedWorkDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/saved-works",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SavedWorkDTO> updateSavedWork(@RequestBody SavedWorkDTO savedWorkDTO) throws URISyntaxException {
        log.debug("REST request to update SavedWork : {}", savedWorkDTO);
        if (savedWorkDTO.getId() == null) {
            return createSavedWork(savedWorkDTO);
        }
        SavedWorkDTO result = savedWorkService.save(savedWorkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("savedWork", savedWorkDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /saved-works : get all the savedWorks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of savedWorks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/saved-works",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SavedWorkDTO>> getAllSavedWorks(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SavedWorks");
        Page<SavedWork> page = savedWorkService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/saved-works");
        return new ResponseEntity<>(savedWorkMapper.savedWorksToSavedWorkDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /saved-works/:id : get the "id" savedWork.
     *
     * @param id the id of the savedWorkDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the savedWorkDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/saved-works/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SavedWorkDTO> getSavedWork(@PathVariable Long id) {
        log.debug("REST request to get SavedWork : {}", id);
        SavedWorkDTO savedWorkDTO = savedWorkService.findOne(id);
        return Optional.ofNullable(savedWorkDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /saved-works/:id : delete the "id" savedWork.
     *
     * @param id the id of the savedWorkDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/saved-works/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSavedWork(@PathVariable Long id) {
        log.debug("REST request to delete SavedWork : {}", id);
        savedWorkService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("savedWork", id.toString())).build();
    }

}
