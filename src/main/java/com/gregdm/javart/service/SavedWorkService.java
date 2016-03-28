package com.gregdm.javart.service;

import com.gregdm.javart.domain.SavedWork;
import com.gregdm.javart.repository.SavedWorkRepository;
import com.gregdm.javart.web.rest.dto.SavedWorkDTO;
import com.gregdm.javart.web.rest.mapper.SavedWorkMapper;
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
 * Service Implementation for managing SavedWork.
 */
@Service
@Transactional
public class SavedWorkService {

    private final Logger log = LoggerFactory.getLogger(SavedWorkService.class);
    
    @Inject
    private SavedWorkRepository savedWorkRepository;
    
    @Inject
    private SavedWorkMapper savedWorkMapper;
    
    /**
     * Save a savedWork.
     * 
     * @param savedWorkDTO the entity to save
     * @return the persisted entity
     */
    public SavedWorkDTO save(SavedWorkDTO savedWorkDTO) {
        log.debug("Request to save SavedWork : {}", savedWorkDTO);
        SavedWork savedWork = savedWorkMapper.savedWorkDTOToSavedWork(savedWorkDTO);
        savedWork = savedWorkRepository.save(savedWork);
        SavedWorkDTO result = savedWorkMapper.savedWorkToSavedWorkDTO(savedWork);
        return result;
    }

    /**
     *  Get all the savedWorks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<SavedWork> findAll(Pageable pageable) {
        log.debug("Request to get all SavedWorks");
        Page<SavedWork> result = savedWorkRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one savedWork by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SavedWorkDTO findOne(Long id) {
        log.debug("Request to get SavedWork : {}", id);
        SavedWork savedWork = savedWorkRepository.findOne(id);
        SavedWorkDTO savedWorkDTO = savedWorkMapper.savedWorkToSavedWorkDTO(savedWork);
        return savedWorkDTO;
    }

    /**
     *  Delete the  savedWork by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SavedWork : {}", id);
        savedWorkRepository.delete(id);
    }
}
