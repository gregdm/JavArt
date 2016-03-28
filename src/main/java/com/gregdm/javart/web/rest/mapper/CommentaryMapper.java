package com.gregdm.javart.web.rest.mapper;

import com.gregdm.javart.domain.*;
import com.gregdm.javart.web.rest.dto.CommentaryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Commentary and its DTO CommentaryDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface CommentaryMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    CommentaryDTO commentaryToCommentaryDTO(Commentary commentary);

    List<CommentaryDTO> commentariesToCommentaryDTOs(List<Commentary> commentaries);

    @Mapping(source = "userId", target = "user")
    Commentary commentaryDTOToCommentary(CommentaryDTO commentaryDTO);

    List<Commentary> commentaryDTOsToCommentaries(List<CommentaryDTO> commentaryDTOs);
}
