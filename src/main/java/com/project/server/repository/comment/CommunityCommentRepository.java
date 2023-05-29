package com.project.server.repository.comment;

import com.project.server.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment,Long> ,CommentRepositoryCustom {
    @Query(value = "SELECT coalesce(MAX(c.ref), 0) FROM CommunityComment c WHERE c.community.id = :communityId")
    Long findNvlRef(@Param("communityId") Long communityId);
    @Query(value = "SELECT MAX(c.refStep) FROM CommunityComment c WHERE c.ref = :ref")
    Long findMaxStep(@Param("ref") Long ref);

    @Modifying // select 문이 아님을 나타낸다
    @Query(value = "UPDATE CommunityComment SET childCount = childCount +1l WHERE id = :id")
    void updateAnswerNum(@Param("id")Long id);
    @Query(value = "SELECT c.users.id FROM CommunityComment c WHERE c.id = :commentId")
    Long findByIdValue(@Param("commentId") Long commentId);
}
