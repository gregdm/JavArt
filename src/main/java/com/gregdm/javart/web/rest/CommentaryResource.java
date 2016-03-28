package com.gregdm.javart.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.javart.domain.Commentary;
import com.gregdm.javart.service.CommentaryService;
import com.gregdm.javart.web.rest.util.HeaderUtil;
import com.gregdm.javart.web.rest.util.PaginationUtil;
import com.gregdm.javart.web.rest.dto.CommentaryDTO;
import com.gregdm.javart.web.rest.mapper.CommentaryMapper;
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
 * REST controller for managing Commentary.
 */
@RestController
@RequestMapping("/api")
public class CommentaryResource {

    private final Logger log = LoggerFactory.getLogger(CommentaryResource.class);
        
    @Inject
    private CommentaryService commentaryService;
    
    @Inject
    private CommentaryMapper commentaryMapper;
    
    /**
     * POST  /commentaries : Create a new commentary.
     *
     * @param commentaryDTO the commentaryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentaryDTO, or with status 400 (Bad Request) if the commentary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commentaries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommentaryDTO> createCommentary(@RequestBody CommentaryDTO commentaryDTO) throws URISyntaxException {
        log.debug("REST request to save Commentary : {}", commentaryDTO);
        if (commentaryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("commentary", "idexists", "A new commentary cannot already have an ID")).body(null);
        }
        CommentaryDTO result = commentaryService.save(commentaryDTO);
        return ResponseEntity.created(new URI("/api/commentaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("commentary", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commentaries : Updates an existing commentary.
     *
     * @param commentaryDTO the commentaryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentaryDTO,
     * or with status 400 (Bad Request) if the commentaryDTO is not valid,
     * or with status 500 (Internal Server Error) if the commentaryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commentaries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommentaryDTO> updateCommentary(@RequestBody CommentaryDTO commentaryDTO) throws URISyntaxException {
        log.debug("REST request to update Commentary : {}", commentaryDTO);
        if (commentaryDTO.getId() == null) {
            return createCommentary(commentaryDTO);
        }
        CommentaryDTO result = commentaryService.save(commentaryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("commentary", commentaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commentaries : get all the commentaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commentaries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/commentaries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CommentaryDTO>> getAllCommentaries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Commentaries");
        Page<Commentary> page = commentaryService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commentaries");
        return new ResponseEntity<>(commentaryMapper.commentariesToCommentaryDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /commentaries/:id : get the "id" commentary.
     *
     * @param id the id of the commentaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentaryDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/commentaries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommentaryDTO> getCommentary(@PathVariable Long id) {
        log.debug("REST request to get Commentary : {}", id);
        CommentaryDTO commentaryDTO = commentaryService.findOne(id);
        return Optional.ofNullable(commentaryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /commentaries/:id : delete the "id" commentary.
     *
     * @param id the id of the commentaryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/commentaries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommentary(@PathVariable Long id) {
        log.debug("REST request to delete Commentary : {}", id);
        commentaryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("commentary", id.toString())).build();
    }

}
