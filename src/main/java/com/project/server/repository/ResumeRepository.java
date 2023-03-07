package com.project.server.repository;

import com.project.server.entity.Resume;
import com.project.server.entity.Study;
import com.project.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository <Resume, Long> {
    List<Resume> findByUser(User user);
}
