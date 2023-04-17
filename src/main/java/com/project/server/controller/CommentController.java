package com.project.server.controller;

import com.project.server.entity.User;
import com.project.server.http.request.CommentRequest;
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
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;

    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(tags = "Comment", summary = "댓글 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("")
    public ResponseEntity createComment(@ApiIgnore @AuthUser User user, @RequestBody CommentRequest commentRequest) {
        return commentService.createComment(user, commentRequest);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(tags = "Comment", summary = "홍보 게시글 댓글 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })    
    @GetMapping("/{promotionId}")
    public ResponseEntity getComment(@ApiIgnore @AuthUser User user, @ApiIgnore @PageableDefault Pageable pageable, 
                                     @Parameter(description = "홍보 게시글 일련번호") @PathVariable(value = "promotionId") Long promotionId) {
        return commentService.getComment(user, pageable, promotionId);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(tags = "Comment", summary = "댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })    
    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@ApiIgnore @AuthUser User user,
                                        @Parameter(description = "댓글 일련번호") @PathVariable(value = "commentId") Long commentId) {
        return commentService.deleteComment(user, commentId);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(tags = "Comment", summary = "댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })    
    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@ApiIgnore @AuthUser User user,
                                        @Parameter(description = "댓글 일련번호") @PathVariable("commentId") Long commentId,
                                        @Parameter(description = "수정 댓글") @RequestParam("comment") String updateComment) {
        System.out.println(user.getId());
        return commentService.updateComment(user, commentId, updateComment);
    }

}

