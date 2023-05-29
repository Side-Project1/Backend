package com.project.server.controller;

import com.project.server.entity.Users;
import com.project.server.http.request.CommunityRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.repository.Community.CommunityRepository;
import com.project.server.service.CommunityService;
import com.project.server.util.AuthUser;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;


@Tag(name="Community", description = "Community API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/community")
@Slf4j
public class CommunityController {
    private final CommunityRepository communityRepository;
    private final CommunityService communityService;


    @ApiOperation(tags = "Community", value = "커뮤니티 페이지 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("")
    public ResponseEntity getStudyList(@PageableDefault Pageable pageable, @RequestParam(required = false) String title, @RequestParam(required = false) String contents) {
        return communityService.getCommunityList(pageable, title,contents);

    }

    @ApiOperation(tags = "Community", value = "커뮤니티 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{communityId}")
    public ResponseEntity findById(@ApiIgnore @AuthUser Users users, @Parameter(description = "커뮤니 게시글 일련번호")@PathVariable("communityId") Long communityId) {

        return communityService.findById(users,communityId);

//        return new ResponseEntity(new ApiRes("스터디 상세 보기 성공", HttpStatus.OK,cnt), HttpStatus.OK);
    }



    @ApiOperation(tags = "Community", value = "커뮤니티 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("")
    public ResponseEntity writeStudy(@ApiIgnore @AuthUser Users users,
                                     @RequestBody  CommunityRequest communityRequest ) throws IOException {

        return communityService.writeCommunity(users, communityRequest);
    }

    @Operation(tags = "Community", summary = "커뮤니티 게시글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(tags = "community", value = "사용자가 쓴 커뮤니티 글 수정")
    @PutMapping("/{communityId}" )
    public ResponseEntity updateCommunity(@ApiIgnore @AuthUser Users users, @PathVariable Long communityId,
                                     @RequestBody CommunityRequest communityRequest) throws IOException {
        return communityService.updateCommunity(users, communityId,communityRequest);
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(tags = "study", value = "사용자가 쓴 커뮤니티 글 삭제")
    @DeleteMapping("/{communityId}/delete")
    public ResponseEntity deleteStudyById(@ApiIgnore @AuthUser Users users, @Parameter(description = "커뮤니티 게시글 일련번호") @PathVariable Long communityId) {
        communityService.deleteCommunityById(users, communityId);
        return new ResponseEntity(new ApiRes("커뮤니티 삭제 성공", HttpStatus.OK), HttpStatus.OK);

    }

}



