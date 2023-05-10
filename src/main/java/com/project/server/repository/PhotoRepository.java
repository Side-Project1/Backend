package com.project.server.repository;

import com.project.server.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PhotoRepository extends JpaRepository <Photo,Long> {

    List<Photo> findByStudyId(Long StudyId);
    Optional<Photo> deleteByStudyId(Long StudyId);
}
