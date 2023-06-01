
package com.project.server.service;

import com.project.server.entity.*;
import com.project.server.http.request.CommentRequest;
import com.project.server.http.request.CommunityCommentRequest;
import com.project.server.http.request.StudyCommentRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.http.response.CommentResponse;
import com.project.server.http.response.CommunityCommentResponse;
import com.project.server.http.response.StudyCommentResponse;
import com.project.server.repository.Community.CommunityRepository;
import com.project.server.repository.Study.StudyRepository;
import com.project.server.repository.UsersRepository;
import com.project.server.repository.comment.CommentRepository;
import com.project.server.repository.comment.CommentRepositoryCustomImpl;
import com.project.server.repository.comment.CommunityCommentRepository;
import com.project.server.repository.comment.StudyCommentRepository;
import com.project.server.repository.promotion.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final UsersRepository usersRepository;
    private final StudyCommentRepository studyCommentRepository;
    private final PromotionRepository promotionRepository;
    private final CommentRepository commentRepository;
    private final StudyRepository studyRepository;
    private final CommunityRepository communityRepository;
    private final CommentRepositoryCustomImpl commentRepositoryCustom;

    private final CommunityCommentRepository communityCommentRepository;

    public ResponseEntity createComment(Users users, CommentRequest commentRequest){
        try {
            Promotions promotions = promotionRepository.findById(commentRequest.getPromotionId()).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
            // 댓글 그룹번호 NVL 함수  NULL 이면 0, NULL 아니면 최대값 리턴
            Long commentsRef = commentRepository.findNvlRef(commentRequest.getPromotionId());
            if(commentRequest.getCommentId() == 0) { // 0 이면 댓글, 아니면 대댓글 저장
                Comment comment = Comment.builder()
                        .promotions(promotions)
                        .users(users)
                        .comments(commentRequest.getComments())
                        .ref(commentsRef + 1l)
                        .refStep(0l)
                        .parent(null)
                        .childCount(0l)
                        .isDeleted(EnumStatus.Status.N)
                        .isPrivated(commentRequest.getIsPrivated())
                        .build();
                commentRepository.save(comment);
                Users saveUsers = usersRepository.findById(users.getId()).get();
                saveUsers.getCommentList().add(comment);
                return new ResponseEntity(new ApiRes("댓글 작성 완료", HttpStatus.OK,comment), HttpStatus.OK);
            } else {
                Comment parent = commentRepository.findById(commentRequest.getCommentId()).orElseThrow(()->new IllegalStateException("댓글이 존재하지 않습니다"));

                Comment comment = Comment.builder()
                        .promotions(promotions)
                        .users(users)
                        .comments("@"+parent.getUsers().getUserId() + " " + commentRequest.getComments())
                        .ref(parent.getRef())
                        .refStep(commentRepository.findMaxStep(parent.getRef()) + 1l)
                        .parent(parent)
                        .childCount(0l)
                        .isDeleted(EnumStatus.Status.N)
                        .isPrivated(commentRequest.getIsPrivated())
                        .build();
                commentRepository.save(comment);
                commentRepository.updateAnswerNum(parent.getId());
                return new ResponseEntity(new ApiRes("댓글 작성 완료", HttpStatus.OK,comment), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 작성 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity createStudyComment(Users users, StudyCommentRequest studyCommentRequest){
        try {
            Study study = studyRepository.findById(studyCommentRequest.getStudyId()).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
            // 댓글 그룹번호 NVL 함수  NULL 이면 0, NULL 아니면 최대값 리턴
            Long commentsRef = studyCommentRepository.findNvlRef(studyCommentRequest.getStudyId());
            if(studyCommentRequest.getCommentId() == 0) { // 0 이면 댓글, 아니면 대댓글 저장
                StudyComment studyComment = StudyComment.builder()
                        .study(study)
                        .users(users)
                        .comments(studyCommentRequest.getComments())
                        .ref(commentsRef + 1l)
                        .refStep(0l)
                        .parent(null)
                        .childCount(0l)
                        .isDeleted(EnumStatus.Status.N)
                        .isPrivated(studyCommentRequest.getIsPrivated())
                        .build();
                studyCommentRepository.save(studyComment);
                Users saveUsers = usersRepository.findById(users.getId()).get();
                saveUsers.getStudycommentList().add(studyComment);
                return new ResponseEntity(new ApiRes("댓글 작성 완료", HttpStatus.OK,studyComment), HttpStatus.OK);
            } else {
                StudyComment parent = studyCommentRepository.findById(studyCommentRequest.getCommentId()).orElseThrow(()->new IllegalStateException("댓글이 존재하지 않습니다"));

                StudyComment studyComment = StudyComment.builder()
                        .study(study)
                        .users(users)
                        .comments("@"+parent.getUsers().getUserId() + " " + studyCommentRequest.getComments())
                        .ref(parent.getRef())
                        .refStep(studyCommentRepository.findMaxStep(parent.getRef()) + 1l)
                        .parent(parent)
                        .childCount(0l)
                        .isDeleted(EnumStatus.Status.N)
                        .isPrivated(studyCommentRequest.getIsPrivated())
                        .build();
                studyCommentRepository.save(studyComment);
                commentRepository.updateAnswerNum(parent.getId());
                return new ResponseEntity(new ApiRes("댓글 작성 완료", HttpStatus.OK,studyComment), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 작성 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity createCommunityComment(Users users, CommunityCommentRequest communityCommentRequest){
        try {
            Community community = communityRepository.findById(communityCommentRequest.getCommunityId()).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
            // 댓글 그룹번호 NVL 함수  NULL 이면 0, NULL 아니면 최대값 리턴
            Long commentsRef = communityCommentRepository.findNvlRef(communityCommentRequest.getCommunityId());
            if(communityCommentRequest.getCommentId() == 0) { // 0 이면 댓글, 아니면 대댓글 저장
                CommunityComment communityComment = CommunityComment.builder()
                        .community(community)
                        .users(users)
                        .comments(communityCommentRequest.getComments())
                        .ref(commentsRef + 1l)
                        .refStep(0l)
                        .parent(null)
                        .childCount(0l)
                        .isDeleted(EnumStatus.Status.N)
                        .isPrivated(communityCommentRequest.getIsPrivated())
                        .build();
                communityCommentRepository.save(communityComment);
                Users saveUsers = usersRepository.findById(users.getId()).get();
                saveUsers.getCommunityCommentList().add(communityComment);
                return new ResponseEntity(new ApiRes("댓글 작성 완료", HttpStatus.OK,communityComment), HttpStatus.OK);
            } else {
                CommunityComment parent = communityCommentRepository.findById(communityCommentRequest.getCommentId()).orElseThrow(()->new IllegalStateException("댓글이 존재하지 않습니다"));

                CommunityComment communityComment = CommunityComment.builder()
                        .community(community)
                        .users(users)
                        .comments("@"+parent.getUsers().getUserId() + " " + communityCommentRequest.getComments())
                        .ref(parent.getRef())
                        .refStep(communityCommentRepository.findMaxStep(parent.getRef()) + 1l)
                        .parent(parent)
                        .childCount(0l)
                        .isDeleted(EnumStatus.Status.N)
                        .isPrivated(communityCommentRequest.getIsPrivated())
                        .build();
                communityCommentRepository.save(communityComment);
                commentRepository.updateAnswerNum(parent.getId());
                return new ResponseEntity(new ApiRes("댓글 작성 완료", HttpStatus.OK,communityComment), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 작성 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }




    public ResponseEntity getComment(Users users, Pageable pageable, Long promotionId) {
        try {
            List<CommentResponse> commentList = commentRepositoryCustom.findAllByPromotion(pageable, promotionId);

            commentList.forEach(data -> {
                if (data.getIsPrivated().getValue().equals(EnumStatus.Status.Y.getValue()) && !data.getUserId().equals(users.getUserId())) {
                    data.setComments("비밀 댓글입니다");
                }
            });

            return new ResponseEntity(new ApiRes("댓글 조회 완료", HttpStatus.OK, commentList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getStudyComment(Users users, Pageable pageable, Long studyId) {
        try {
            List<StudyCommentResponse> commentList = commentRepositoryCustom.findAllByStudy(pageable, studyId);
            System.out.println("댓글 조회"+ commentList);

            commentList.forEach(data -> {
                if (data.getIsPrivated().getValue().equals(EnumStatus.Status.Y.getValue()) && !data.getUserId().equals(users.getUserId())) {
                    data.setComments("비밀 댓글입니다");
                }
            });

            return new ResponseEntity(new ApiRes("댓글 조회 완료", HttpStatus.OK, commentList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getCommunityComment(Users users, Pageable pageable, Long communityId) {
        try {
            List<CommunityCommentResponse> commentList = commentRepositoryCustom.findAllByCommunity(pageable, communityId);
            System.out.println("댓글 조회"+ commentList);

            commentList.forEach(data -> {
                if (data.getIsPrivated().getValue().equals(EnumStatus.Status.Y.getValue()) && !data.getUserId().equals(users.getUserId())) {
                    data.setComments("비밀 댓글입니다");
                }
            });

            return new ResponseEntity(new ApiRes("댓글 조회 완료", HttpStatus.OK, commentList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @Transactional
    public ResponseEntity deleteComment(Users users, Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).get();

            if(!comment.getUsers().getId().equals(users.getId())) {
                throw new Exception("댓글 삭제 권한이 없습니다.");
            }

            if(comment.getIsDeleted() == EnumStatus.Status.N) {
                comment.setComments("댓글이 삭제되었습니다.");
                comment.setIsDeleted(EnumStatus.Status.Y);
                return new ResponseEntity(new ApiRes("댓글 삭제 완료", HttpStatus.OK), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ApiRes("이미 삭제된 댓글입니다.", HttpStatus.OK), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 삭제 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @Transactional
    public ResponseEntity deleteStudyComment(Users users, Long commentId) {
        try {
            StudyComment studyComment = studyCommentRepository.findById(commentId).get();
            System.out.println(studyComment.getUsers().getId());

            if(!studyComment.getUsers().getId().equals(users.getId())) {
                throw new Exception("댓글 삭제 권한이 없습니다.");
            }

            if(studyComment.getIsDeleted() == EnumStatus.Status.N) {
                studyComment.setComments("댓글이 삭제되었습니다.");
                studyComment.setIsDeleted(EnumStatus.Status.Y);
                return new ResponseEntity(new ApiRes("댓글 삭제 완료", HttpStatus.OK), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ApiRes("이미 삭제된 댓글입니다.", HttpStatus.OK), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 삭제 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity deleteCommunityComment(Users users, Long commentId) {
        try {
            CommunityComment communityComment = communityCommentRepository.findById(commentId).get();
            System.out.println(communityComment.getUsers().getId());

            if(!communityComment.getUsers().getId().equals(users.getId())) {
                throw new Exception("댓글 삭제 권한이 없습니다.");
            }

            if(communityComment.getIsDeleted() == EnumStatus.Status.N) {
                communityComment.setComments("댓글이 삭제되었습니다.");
                communityComment.setIsDeleted(EnumStatus.Status.Y);
                return new ResponseEntity(new ApiRes("댓글 삭제 완료", HttpStatus.OK), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ApiRes("이미 삭제된 댓글입니다.", HttpStatus.OK), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 삭제 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity updateComment(Users users, Long commentId, String updateComment) {
        try {
            Comment comment = commentRepository.findById(commentId).get();
            comment.setComments(updateComment);
            return new ResponseEntity(new ApiRes("댓글 수정 완료", HttpStatus.OK,comment), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity updateStudyComment(Users users, Long commentId, String updateComment) {
        try {
            StudyComment studyComment = studyCommentRepository.findById(commentId).get();
            System.out.println("studyComment"+studyComment);
            studyComment.setComments(updateComment);
            return new ResponseEntity(new ApiRes("댓글 수정 완료", HttpStatus.OK,studyComment), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity updateCommunityComment(Users users, Long commentId, String updateComment) {
        try {
            CommunityComment communityComment = communityCommentRepository.findById(commentId).get();
            communityComment.setComments(updateComment);
            return new ResponseEntity(new ApiRes("댓글 수정 완료", HttpStatus.OK,communityComment), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}