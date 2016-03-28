package com.gregdm.javart.service;

import com.gregdm.javart.domain.Commentary;
import com.gregdm.javart.repository.CommentaryRepository;
import com.gregdm.javart.web.rest.dto.CommentaryDTO;
import com.gregdm.javart.web.rest.mapper.CommentaryMapper;
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
 * Service Implementation for managing Commentary.
 */
@Service
@Transactional
public class CommentaryService {

    private final Logger log = LoggerFactory.getLogger(CommentaryService.class);
    
    @Inject
    private CommentaryRepository commentaryRepository;
    
    @Inject
    private CommentaryMapper commentaryMapper;
    
    /**
     * Save a commentary.
     * 
     * @param commentaryDTO the entity to save
     * @return the persisted entity
     */
    public CommentaryDTO save(CommentaryDTO commentaryDTO) {
        log.debug("Request to save Commentary : {}", commentaryDTO);
        Commentary commentary = commentaryMapper.commentaryDTOToCommentary(commentaryDTO);
        commentary = commentaryRepository.save(commentary);
        CommentaryDTO result = commentaryMapper.commentaryToCommentaryDTO(commentary);
        return result;
    }

    /**
     *  Get all the commentaries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Commentary> findAll(Pageable pageable) {
        log.debug("Request to get all Commentaries");
        Page<Commentary> result = commentaryRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one commentary by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CommentaryDTO findOne(Long id) {
        log.debug("Request to get Commentary : {}", id);
        Commentary commentary = commentaryRepository.findOne(id);
        CommentaryDTO commentaryDTO = commentaryMapper.commentaryToCommentaryDTO(commentary);
        return commentaryDTO;
    }

    /**
     *  Delete the  commentary by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Commentary : {}", id);
        commentaryRepository.delete(id);
    }
}
