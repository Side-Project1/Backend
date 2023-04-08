package com.project.server.repository.Study;

import com.project.server.entity.StudyApply;
import com.project.server.entity.StudyApplyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyApplyRepository extends JpaRepository<StudyApply,Long> {
        List<StudyApply> findByApplyStatus(StudyApplyStatus applyStatus);
        Optional<StudyApply> findByStudyId(Long studyId);
}
