package com.project.server.repository;

import com.project.server.entity.Study;
import com.project.server.entity.User;
import com.project.server.repository.Study.StudyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study ,Long>, StudyRepositoryCustom {
//    List<Study> findByCategory(StudyCategory studyCategory);
    List<Study> findByUser(Optional<User> user);

    List<Study> findByOwner(User owner);


}
