package com.project.server.repository;

import com.project.server.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository <Resume, Long> {
}
