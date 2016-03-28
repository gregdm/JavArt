package com.gregdm.javart.repository;

import com.gregdm.javart.domain.Album;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Album entity.
 */
public interface AlbumRepository extends JpaRepository<Album,Long> {

    @Query("select album from Album album where album.user.login = ?#{principal.username}")
    List<Album> findByUserIsCurrentUser();

}
