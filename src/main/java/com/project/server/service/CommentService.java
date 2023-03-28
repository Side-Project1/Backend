package com.project.server.service;

import com.project.server.entity.*;
import com.project.server.http.request.CommentRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.http.response.CommentResponse;
import com.project.server.repository.comment.CommentRepository;
import com.project.server.repository.comment.CommentRepositoryCustomImpl;
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
//    private final UserRepository userRepository;
    private final PromotionRepository promotionRepository;
    private final CommentRepository commentRepository;
    private final CommentRepositoryCustomImpl commentRepositoryCustom;

    public ResponseEntity createComment(User user, CommentRequest commentRequest){
        try {
            Promotion promotion = promotionRepository.findById(commentRequest.getPromotionId()).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
            // 댓글 그룹번호 NVL 함수  NULL 이면 0, NULL 아니면 최대값 리턴
            Long commentsRef = commentRepository.findNvlRef(commentRequest.getPromotionId());
            if(commentRequest.getCommentId() == 0) { // 0 이면 댓글, 아니면 대댓글 저장
                Comment comment = Comment.builder()
                        .promotion(promotion)
                        .user(user)
                        .comments(commentRequest.getComments())
                        .ref(commentsRef + 1l)
                        .refStep(0l)
                        .parent(null)
                        .childCount(0l)
                        .isDeleted(Status.N)
                        .isPrivated(commentRequest.getIsPrivated())
                        .build();
//                User commentUser = userRepository.findById(user.getId()).orElseThrow();
//                commentUser.addComment(comment);
                commentRepository.save(comment);
                return new ResponseEntity(new ApiRes("댓글 작성 완료", HttpStatus.OK), HttpStatus.OK);
            } else {
                Comment parent = commentRepository.findById(commentRequest.getCommentId()).orElseThrow(()->new IllegalStateException("댓글이 존재하지 않습니다"));

                Comment comment = Comment.builder()
                        .promotion(promotion)
                        .user(user)
                        .comments("@"+parent.getUser().getUserId() + " " + commentRequest.getComments())
                        .ref(parent.getRef())
                        .refStep(commentRepository.findMaxStep(parent.getRef()) + 1l)
                        .parent(parent)
                        .childCount(0l)
                        .isDeleted(Status.N)
                        .isPrivated(commentRequest.getIsPrivated())
                        .build();
                commentRepository.save(comment);
                commentRepository.updateAnswerNum(parent.getId());
                return new ResponseEntity(new ApiRes("댓글 작성 완료", HttpStatus.OK), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 작성 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity getComment(User user, Pageable pageable, Long promotionId) {
        try {
            List<CommentResponse> commentList = commentRepositoryCustom.findAllByPromotion(pageable, promotionId);

            commentList.forEach(data -> {
                if (data.getIsPrivated().getValue().equals(Status.Y.getValue()) && !data.getUserId().equals(user.getUserId())) {
                    data.setComments("비밀 댓글입니다");
                }
            });

            return new ResponseEntity(new ApiRes("댓글 조회 완료", HttpStatus.OK, commentList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity deleteComment(User user, Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).get();

            if(!comment.getUser().getId().equals(user.getId())) {
                throw new Exception("댓글 삭제 권한이 없습니다.");
            }

            if(comment.getIsDeleted() == Status.N) {
                comment.setComments("댓글이 삭제되었습니다.");
                comment.setIsDeleted(Status.Y);
                return new ResponseEntity(new ApiRes("댓글 삭제 완료", HttpStatus.OK), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ApiRes("이미 삭제된 댓글입니다.", HttpStatus.OK), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 삭제 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity updateComment(User user, Long commentId, String updateComment) {
        try {
            Comment comment = commentRepository.findById(commentId).get();
            comment.setComments(updateComment);
            return new ResponseEntity(new ApiRes("댓글 수정 완료", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("댓글 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


//    //댓글 등록
//    @CacheEvict(value = COMMENTS, key = "#studyId")
//    public List<CommentDto> findCommentsByStudyId(Long studyId) {
//        studyRepository.findById(studyId).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
//        return convertNestedStructure(commentRepository.findCommentByStudyId(studyId));
//    }
//
//    @Transactional
//    @CacheEvict(value = COMMENTS, key = "#requestDto.studyId")
//    public CommentDto createComment(CommentRequest requestDto) {
//        Comment comment = commentRepository.save(
//                Comment.createComment(requestDto.getContent(),
//                        studyRepository.findById(requestDto.getStudyId()).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다")),
//                        userRepository.findByUserName(requestDto.getUserName()).orElseThrow(()->new IllegalStateException("사용자가 존재하지 않습니다")),
//
//                        commentRepository.findById(requestDto.getParentId()).orElseThrow(()->new IllegalStateException("존재하지 않습니다.")))
//                );
//
//        return CommentDto.convertCommentToDto(comment);
//    }
//
//    @Transactional
//    public void deleteComment(Long commentId) {
//        Comment comment = commentRepository.findCommentByIdWithParent(commentId).orElseThrow(() -> new IllegalStateException("존재하지 않음"));
//        cacheService.deleteCommentsCache(comment.getStudy().getId());
//        if (comment.getChildren().size() != 0) {
//            comment.changeDeletedStatus(DeleteStatus.Y);
//        } else {
//            commentRepository.delete(getDeletableAncestorComment(comment));
//        }
//    }
//
//    private Comment getDeletableAncestorComment(Comment comment) {
//        Comment parent = comment.getParent();
//        if (parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == DeleteStatus.Y)
//            return getDeletableAncestorComment(parent);
//        return comment;
//    }
//
//
//    private List<CommentDto> convertNestedStructure(List<Comment> comments) {
//        List<CommentDto> result = new ArrayList<>();
//        Map<Long, CommentDto> map = new HashMap<>();
//        comments.stream().forEach(c -> {
//            CommentDto dto = CommentDto.convertCommentToDto(c);
//            map.put(dto.getId(), dto);
//            if (c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);
//            else result.add(dto);
//        });
//        return result;
//    }
}