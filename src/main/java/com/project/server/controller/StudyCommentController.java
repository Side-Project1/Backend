package com.project.server.controller;

import com.project.server.entity.Users;
import com.project.server.http.request.StudyCommentRequest;
import com.project.server.service.CommentService;
import com.project.server.util.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Tag(name = "Comment", description = "댓글 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/study/comment")
public class StudyCommentController {
    private final CommentService commentService;
    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(tags = "Study", summary = "스터디 댓글 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("")
    public ResponseEntity createStudyComment(@ApiIgnore @AuthUser Users users, @RequestBody StudyCommentRequest studyCommentRequest) {
        return commentService.createStudyComment(users, studyCommentRequest);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(tags = "Comment", summary = "스터디 게시글 댓글 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/{studyId}")
    public ResponseEntity getStudyComment(@ApiIgnore @AuthUser Users users, @PageableDefault Pageable pageable, @PathVariable("studyId") Long studyId) {
        return commentService.getStudyComment(users, pageable, studyId);
    }

    @PreAuthorize("hasAnyRole('USER')")

    @Operation(tags = "StudyComment", summary = "스터디 댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteStudyComment(@ApiIgnore @AuthUser Users users,
                                             @Parameter(description = "댓글 일련번호") @PathVariable(value = "commentId") Long commentId) {
        return commentService.deleteStudyComment(users, commentId);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(tags = "StudyComment", summary = "스터디 댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PutMapping("/{commentId}")
    public ResponseEntity updateStudyComment(@ApiIgnore @AuthUser Users users,
                                             @Parameter(description = "댓글 일련번호") @PathVariable("commentId") Long commentId,
                                            @RequestBody String updateComment) {
        System.out.println(users.getId());
        return commentService.updateStudyComment(users, commentId, updateComment);
    }
}
