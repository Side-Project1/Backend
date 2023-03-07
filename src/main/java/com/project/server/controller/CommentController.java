package com.project.server.controller;

import com.project.server.http.request.CommentDto;
import com.project.server.http.request.CommentRequest;
import com.project.server.service.StudyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//@Api(value = "Comment Controller", tags = {"Comment"})
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/v1/comment")
//public class CommentController {
//    private final StudyRepository studyRepository;
//    private final StudyService studyService;
//    private final CommentService commentService;
//
//        @ApiOperation(value = "스터디 댓글 목록 조회", notes = "해당 스터디 댓글 목록을 조회한다.")
//        @GetMapping(value = "/comments/{studyId}")
//
//        public void findAllCommentsByTicketId(@PathVariable("studyId") Long studyId) {
//            commentService.findCommentsByStudyId(studyId);
//
//        }
//
//        @ApiImplicitParams({
//                @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
//        })
//        @ApiOperation(value = "댓글 작성", notes = "댓글을 작성한다.")
//        @PostMapping(value = "/comments")
//        public CommentDto createComment(@RequestBody CommentRequest requestDto) {
//            return commentService.createComment(requestDto);
//        }
//
//        @ApiImplicitParams({
//                @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
//        })
//        @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제한다.")
//        @DeleteMapping(value = "/comments/{commentId}")
//        public void deleteComment(@PathVariable("commentId") Long commentId) {
//            commentService.deleteComment(commentId);
//
//        }
//    }
//
