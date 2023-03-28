package com.project.server.controller;

import com.project.server.entity.User;
import com.project.server.http.request.CommentRequest;
import com.project.server.CommentService;
import com.project.server.util.AuthUser;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "Comment Controller", tags = {"Comment"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;

    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("")
    public ResponseEntity createComment(@ApiIgnore @AuthUser User user, @RequestBody CommentRequest commentRequest) {
        return commentService.createComment(user, commentRequest);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{promotionId}")
    public ResponseEntity getComment(@ApiIgnore @AuthUser User user, @PageableDefault Pageable pageable, @PathVariable("promotionId") Long promotionId) {
        return commentService.getComment(user, pageable, promotionId);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@ApiIgnore @AuthUser User user, @PathVariable("commentId") Long commentId) {
        return commentService.deleteComment(user, commentId);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@ApiIgnore @AuthUser User user, @PathVariable("commentId") Long commentId,
                                        @RequestParam("comment") String updateComment) {
        System.out.println(user.getId());
        return commentService.updateComment(user, commentId, updateComment);
    }

//    @ApiOperation(value = "스터디 댓글 목록 조회", notes = "해당 스터디 댓글 목록을 조회한다.")
//    @GetMapping(value = "/comments/{studyId}")
//
//    public void findAllCommentsByTicketId(@PathVariable("studyId") Long studyId) {
//        commentService.findCommentsByStudyId(studyId);
//
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "댓글 작성", notes = "댓글을 작성한다.")
//    @PostMapping(value = "/comments")
//    public CommentDto createComment(@RequestBody CommentRequest requestDto) {
//        return commentService.createComment(requestDto);
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제한다.")
//    @DeleteMapping(value = "/comments/{commentId}")
//    public void deleteComment(@PathVariable("commentId") Long commentId) {
//        commentService.deleteComment(commentId);
//
//    }
}

