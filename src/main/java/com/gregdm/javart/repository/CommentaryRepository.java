package com.gregdm.javart.repository;

import com.gregdm.javart.domain.Commentary;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Commentary entity.
 */
public interface CommentaryRepository extends JpaRepository<Commentary,Long> {

    @Query("select commentary from Commentary commentary where commentary.user.login = ?#{principal.username}")
    List<Commentary> findByUserIsCurrentUser();

}
