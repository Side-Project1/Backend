package com.project.server.controller;

import com.project.server.entity.Study;
import com.project.server.entity.Users;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.request.PromotionPageRequest;
import com.project.server.http.request.StudyApplyRequest;
import com.project.server.http.request.StudyPageRequest;
import com.project.server.http.request.StudyRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.http.response.StudyResponse;
import com.project.server.repository.Study.StudyRepository;
import com.project.server.service.StudyService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;

@Tag(name="Study", description = "스터디 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/study")
@Slf4j
public class StudyController {
    private final StudyRepository studyRepository;
    private final StudyService studyService;


    @ApiOperation(tags = "Study", value = "스터디 페이지 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("")
    public ResponseEntity getStudyList(@PageableDefault Pageable pageable, @RequestBody(required = false) StudyPageRequest studyPageRequest) {
        return studyService.getStudyList(pageable, studyPageRequest);

    }

    @ApiOperation(tags = "Study", value = "스터디 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{studyId}")
    public ResponseEntity findById(@ApiIgnore @AuthUser Users users, @Parameter(description = "스터디 게시글 일련번호")@PathVariable("studyId") Long studyId) {

        return studyService.findById(users,studyId);

//        return new ResponseEntity(new ApiRes("스터디 상세 보기 성공", HttpStatus.OK,cnt), HttpStatus.OK);
    }


    @ApiOperation(tags = "Study", value = "스터디 마감 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/{studyId}/dead")
    public ResponseEntity dead(@ApiIgnore @AuthUser Users users,@Parameter(description = "스터디 게시글 일련번호")@PathVariable("studyId") Long studyId) {
        return studyService.dead(users,studyId);

    }

    @ApiOperation(tags = "Study", value = "스터디 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity writeStudy(@ApiIgnore @AuthUser Users users,
                                      StudyRequest studyRequest, @RequestParam(name="sub_category") List<Long> sub_category,@RequestBody(required = false) MultipartFile[] files ) throws IOException {

            return studyService.writeStudy(users, studyRequest,sub_category,files);
            //return new ResponseEntity(new ApiRes("스터디 등록 성공", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @Operation(tags = "study", summary = "스터디 게시글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(tags = "study", value = "사용자가 쓴 스터디 글 수정")
    @PutMapping(value = "/{studyId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity updateStudy(@ApiIgnore @AuthUser Users users, @PathVariable Long studyId,
                                      StudyRequest studyRequest, @RequestParam(name="sub_category") List<Long> sub_category,@RequestParam (required = false) MultipartFile[] files) throws IOException {
        return studyService.updateStudy(users, studyId, sub_category,studyRequest,files);
        //return new ResponseEntity(new ApiRes("스터디 수정 성공", HttpStatus.OK), HttpStatus.OK);
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(tags = "study", value = "사용자가 쓴 스터디 글 삭제")
    @DeleteMapping("/{studyId}/delete")
    public ResponseEntity deleteStudyById(@ApiIgnore @AuthUser Users users, @Parameter(description = "스터디 게시글 일련번호") @PathVariable Long studyId) {
        studyService.deleteStudyById(users, studyId);
        return new ResponseEntity(new ApiRes("스터디 삭제 성공", HttpStatus.OK), HttpStatus.OK);

    }

    @ApiOperation(tags = "study", value = "스터디 멤버 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/studies/{studyId}/members")
    public ResponseEntity addStudyMembers(@PathVariable Long studyId, @ApiIgnore @AuthUser Users users, @RequestBody StudyApplyRequest studyApplyRequest) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResourceNotFoundException("Study", "id", studyId));
        String res = studyService.addStudyMembers(studyId, users, studyApplyRequest);
        return new ResponseEntity(new ApiRes("스터디 멤버 등록 성공", HttpStatus.OK, res), HttpStatus.OK);

    }

    @ApiOperation(tags = "study", value = "스터디 멤버 승인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/studies/{studyId}/{approve}/apply")
    public ResponseEntity approveStudyApply(@PathVariable Long studyId,@ApiIgnore @AuthUser Users users, @PathVariable (name="approve") String approve) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResourceNotFoundException("Study", "id", studyId));
       studyService.approveStudyApply(studyId, users,approve);
        return new ResponseEntity(new ApiRes("스터디 멤버 승인", HttpStatus.OK), HttpStatus.OK);
    }
}



