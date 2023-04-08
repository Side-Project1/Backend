package com.project.server.repository;

import com.project.server.entity.JobCategory;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    List<JobCategory> findByParentCategory(String parentCategory);

//    @Query(value = "SELECT j FROM JobCategory j WHERE j.parentCategory= :)
//    List<JobCategory> getFindByparentCategory(@Param(""));
}
