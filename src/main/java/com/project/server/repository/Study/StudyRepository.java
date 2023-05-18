package com.project.server.repository.Study;


import com.project.server.entity.Scrap;
import com.project.server.entity.Study;

import com.project.server.entity.Users;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
@Primary
public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryCustom {

}
