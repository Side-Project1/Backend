package com.project.server.repository.comment;

import com.project.server.entity.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StudyCommentRepository extends JpaRepository<StudyComment, Long>, CommentRepositoryCustom {
    @Query(value = "SELECT coalesce(MAX(c.ref), 0) FROM StudyComment c WHERE c.study.id = :studyId")
    Long findNvlRef(@Param("studyId") Long studyId);
    @Query(value = "SELECT MAX(c.refStep) FROM StudyComment c WHERE c.ref = :ref")
    Long findMaxStep(@Param("ref") Long ref);
    //    @Query(value = "SELECT c.childCount FROM Comment c WHERE c.id = :commentId")
//    Long findChildCount(@Param("commentId") Long commentId);
//    @Transactional
//    @Modifying // select 문이 아님을 나타낸다
//    @Query(value = "UPDATE Comment SET refStep = refStep +1l WHERE ref = :ref AND refStep > :refStep")
//    void updateRefStep(@Param("ref") Long ref, @Param("refStep") Long refStep);
    @Transactional
    @Modifying // select 문이 아님을 나타낸다
    @Query(value = "UPDATE StudyComment SET childCount = childCount +1l WHERE id = :id")
    void updateAnswerNum(@Param("id")Long id);
    @Query(value = "SELECT c.users.id FROM StudyComment c WHERE c.id = :commentId")
    Long findByIdValue(@Param("commentId") Long commentId);
}
