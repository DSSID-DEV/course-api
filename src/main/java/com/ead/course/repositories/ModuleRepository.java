package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.queries.ModuleQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

    @Query(value = ModuleQueries.WHERE_COURSE_ID, nativeQuery = true)
    List<ModuleModel> findAllModuleIntoCourse(@Param("courseId") UUID courseId);

    @Query(value = ModuleQueries.WHERE_COURSE_ID_AND_MODULE_ID, nativeQuery = true)
    Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId") UUID moduleId);
}
