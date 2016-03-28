package com.gregdm.javart.web.rest.mapper;

import com.gregdm.javart.domain.*;
import com.gregdm.javart.web.rest.dto.AlbumDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Album and its DTO AlbumDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface AlbumMapper {

    @Mapping(source = "user.id", target = "userId")
    AlbumDTO albumToAlbumDTO(Album album);

    List<AlbumDTO> albumsToAlbumDTOs(List<Album> albums);

    @Mapping(source = "userId", target = "user")
    Album albumDTOToAlbum(AlbumDTO albumDTO);

    List<Album> albumDTOsToAlbums(List<AlbumDTO> albumDTOs);
}
