package com.gregdm.javart.repository;

import com.gregdm.javart.domain.SavedWork;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SavedWork entity.
 */
public interface SavedWorkRepository extends JpaRepository<SavedWork,Long> {

    @Query("select savedWork from SavedWork savedWork where savedWork.user.login = ?#{principal.username}")
    List<SavedWork> findByUserIsCurrentUser();

}
