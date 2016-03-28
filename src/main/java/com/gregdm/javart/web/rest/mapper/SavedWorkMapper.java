package com.gregdm.javart.web.rest.mapper;

import com.gregdm.javart.domain.*;
import com.gregdm.javart.web.rest.dto.SavedWorkDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SavedWork and its DTO SavedWorkDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface SavedWorkMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "album.id", target = "albumId")
    @Mapping(source = "album.name", target = "albumName")
    SavedWorkDTO savedWorkToSavedWorkDTO(SavedWork savedWork);

    List<SavedWorkDTO> savedWorksToSavedWorkDTOs(List<SavedWork> savedWorks);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "albumId", target = "album")
    SavedWork savedWorkDTOToSavedWork(SavedWorkDTO savedWorkDTO);

    List<SavedWork> savedWorkDTOsToSavedWorks(List<SavedWorkDTO> savedWorkDTOs);

    default Album albumFromId(Long id) {
        if (id == null) {
            return null;
        }
        Album album = new Album();
        album.setId(id);
        return album;
    }
}
