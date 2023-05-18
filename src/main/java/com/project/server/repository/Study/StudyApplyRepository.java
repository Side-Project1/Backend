package com.project.server.repository.Study;

import com.project.server.entity.EnumStatus;
import com.project.server.entity.Study;
import com.project.server.entity.StudyApply;

import com.project.server.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyApplyRepository extends JpaRepository<StudyApply,Long> {
        List<StudyApply> findByApplyStatus(EnumStatus.StudyApplyStatus applyStatus);

        StudyApply findByUsersAndStudy(Users users, Study study);
        Optional<StudyApply> findByStudyId(Long studyId);
}
