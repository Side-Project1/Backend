package com.project.server.controller;

import com.project.server.entity.Study;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.request.StudyApplyRequest;
import com.project.server.http.request.StudyRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.repository.StudyRepository;
import com.project.server.repository.UserRepository;
import com.project.server.service.StudyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name="Study", description = "스터디 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/study")
@Slf4j
public class StudyController {
    private final StudyRepository studyRepository;
    private final StudyService studyService;

    private final UserRepository userRepository;


    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })


    @ApiOperation(value = "전체 스터디 조회")
    @GetMapping("/all")
    public ResponseEntity findAll(@PageableDefault Pageable pageable,
                                  @RequestParam(required = false) String title,
                                  @RequestParam(required = false) String contents) {
        Page<Study> studyList = studyService.getStudyList(pageable, title, contents);


        log.debug("총 element 수 : {}, 전체 page 수 : {}, 페이지에 표시할 element 수 : {}, 현재 페이지 index : {}, 현재 페이지의 element 수 : {}",
                studyList.getTotalElements(), studyList.getTotalPages(), studyList.getSize(),
                studyList.getNumber(), studyList.getNumberOfElements());

        return new ResponseEntity(new ApiRes("스터디 조회 성공", HttpStatus.OK), HttpStatus.OK);
    }

    @ApiOperation(value = "스터디 상제 조회")
    @GetMapping("/{studyId}")
    public ResponseEntity findById(@PathVariable("studyId") Long studyId) {

        studyService.findById(studyId);
        return new ResponseEntity(new ApiRes("스터디 상세 보기 성공", HttpStatus.OK), HttpStatus.OK);
    }


//    @ApiOperation(value = "스터디 카테고리 별 조회")
//    @GetMapping("/category/{category}")
//    public ResponseEntity findByCategory(@PathVariable StudyCategory category) {
//        studyService.findByCategory(category);
//        return new ResponseEntity(new ApiRes("스터디 카테고리 보기 성공", HttpStatus.OK), HttpStatus.OK);
//
//    }

    @ApiOperation(value = "사용자가 쓴 스터디 글 조회")
    @GetMapping("/user/{userId}")
    public ResponseEntity findByUserId(@PathVariable String userId) {
        studyService.findByUser(userId);
        return new ResponseEntity(new ApiRes("사용자가 작성한 스터디 보기 성공", HttpStatus.OK), HttpStatus.OK);

    }

    @ApiOperation(value = "스터디 글 등록")
    @PostMapping(value = "/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity writeStudy(@PathVariable("userId") String userId,
                                     StudyRequest studyRequest, MultipartFile[] files) throws IOException {
        studyService.writeStudy(userId, studyRequest,files);
        return new ResponseEntity(new ApiRes("스터디 등록 성공", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @ApiOperation(value = "사용자가 쓴 스터디 글 수정")
    @PutMapping(value = "/{userId}/{studyId}/write",consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity updateStudy(@PathVariable String userId, @PathVariable Long studyId,
                                     StudyRequest studyRequest, MultipartFile[] files) throws IOException {
        studyService.updateStudy(userId, studyId, studyRequest,files);
        return new ResponseEntity(new ApiRes("스터디 수정 성공", HttpStatus.OK), HttpStatus.OK);

    }

    @ApiOperation(value = "사용자가 쓴 스터디 글 삭제")
    @DeleteMapping("/{userId}/{studyId}/delete")
    public ResponseEntity deleteStudyById(@PathVariable String userId, @PathVariable Long studyId) {
        studyService.deleteStudyById(userId, studyId);
        return new ResponseEntity(new ApiRes("스터디 삭제 성공", HttpStatus.OK), HttpStatus.OK);

    }


    @PostMapping("/studies/{studyId}/{userId}/members")
    public ResponseEntity addStudyMembers(@PathVariable Long studyId, @PathVariable String userId, @RequestBody StudyApplyRequest studyApplyRequest) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResourceNotFoundException("Study", "id", studyId));
        String res = studyService.addStudyMembers(studyId, userId, studyApplyRequest);
        return new ResponseEntity(new ApiRes("스터디 멤버 등록 성공", HttpStatus.OK, res), HttpStatus.OK);

    }

    @PostMapping("/studies/{studyId}/{userId}/apply")
    public ResponseEntity approveStudyApply(@PathVariable Long studyId, @PathVariable String userId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResourceNotFoundException("Study", "id", studyId));
       studyService.approveStudyApply(studyId, userId);
        return new ResponseEntity(new ApiRes("스터디 멤버 승인", HttpStatus.OK), HttpStatus.OK);

    }

}



