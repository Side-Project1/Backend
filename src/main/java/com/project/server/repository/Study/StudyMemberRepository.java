package com.project.server.repository.Study;

import com.project.server.entity.Study;
import com.project.server.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyMemberRepository extends JpaRepository<StudyMember,Long> {
    List<StudyMember> findByStudy(Study study);
}
