//package com.project.server.service;
//
//import com.project.server.Cache.CacheKey;
//import com.project.server.entity.Comment;
//import com.project.server.entity.DeleteStatus;
//import com.project.server.http.request.CommentDto;
//import com.project.server.http.request.CommentRequest;
//import com.project.server.repository.CommentRepository;
//import com.project.server.repository.StudyRepository;
//import com.project.server.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.Cacheable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.project.server.Cache.CacheKey.*;
//
//@RequiredArgsConstructor
//@Slf4j
//@Service
//public class CommentService {
//    private final StudyRepository studyRepository;
//    private final UserRepository userRepository;
//    private final CommentRepository commentRepository;
//    private final CacheService cacheService;
//
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
//}