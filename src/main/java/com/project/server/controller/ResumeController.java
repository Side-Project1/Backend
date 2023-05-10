//package com.project.server.controller;
//
//import com.project.server.entity.Link;
//import com.project.server.entity.Resume;
//import com.project.server.http.request.ResumeRequest;
//import com.project.server.http.response.ApiRes;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import com.project.server.repository.ResumeRepository;
//import com.project.server.service.ResumeService;
//import com.project.server.service.S3Service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Tag(name="Resume", description = "이력서 API")
//@RestController
//@RequiredArgsConstructor //final 선
//@Slf4j
//@RequestMapping("/api/v1/resume")
//public class ResumeController {
//
//    private final ResumeService resumeService;
//    private final  ResumeRepository resumeRepository;
//    private final S3Service s3Service;
//
//
//    @ApiOperation(value = "전체 이력서 조회")
//    @GetMapping("/all")
//    public List<ResumeRequest> findAll(){
//        List<Resume> all = resumeRepository.findAll();
//        List<ResumeRequest> allPost = new ArrayList<>();
//
//        for(Resume resume : all){
//            ResumeRequest build = ResumeRequest.builder()
//                    .title(resume.getTitle())
//                    .profileUrl(resume.getProfileUrl())
//                    .certificate(resume.getCertificate())
//                    .career(resume.getCareer())
//                    .school(resume.getSchool())
//                    .job(resume.getJob())
//                    .build();
//            allPost.add(build);
//        }
//
//        return allPost;
//    }
//
//    @ApiOperation(value = "이력서 상세보기")
//    @GetMapping("/{resumeId}")
//    public ResponseEntity findById(@PathVariable Long resumeId) {
//
//        resumeService.findById(resumeId);
//        return new ResponseEntity(new ApiRes("이력서 상세 보기 성공", HttpStatus.OK), HttpStatus.OK);
//
//    }
//
//    @ApiOperation(value = "사용자가 작성한 이력서 모아보기")
//    @GetMapping("/user/{userId}")
//    public ResponseEntity findByUserId(@PathVariable String userId) {
//         resumeService.findByUser(userId);
//        return new ResponseEntity(new ApiRes("이력서 조회 성공", HttpStatus.OK), HttpStatus.OK);
//
//    }
//
//    @ApiOperation(value = "AWS S3 이미지 업로드 및 이력서 등록")
//    @PostMapping(value="/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity writeResume(@PathVariable String userId,  ResumeRequest resumeRequest, MultipartFile profile, MultipartFile[] files)  throws IOException {
//        System.out.println("file");
//        resumeService.writeResume(userId,resumeRequest, profile,files);
//        return new ResponseEntity(new ApiRes("이력서 등록 성공", HttpStatus.CREATED), HttpStatus.CREATED);
//    }
//
//    @ApiOperation(value = "이력서 수정")
//    @PutMapping(value = "/{userId}/{resumeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity updateResume(@PathVariable String userId,@PathVariable Long resumeId,MultipartFile[] files,
//                              ResumeRequest resumeRequest) throws IOException {
//
//        resumeService.updateResume(userId, resumeId, resumeRequest,files);
//        return new ResponseEntity(new ApiRes("이력서 수정 성공", HttpStatus.OK), HttpStatus.OK);
//
//    }
//
//    @ApiOperation(value = "이력서 삭제")
//    @DeleteMapping("/{userId}/{resumeId}/delete")
//    public ResponseEntity deleteStudyById(@PathVariable String userId,@PathVariable Long resumeId) {
//        resumeService.deleteResumeById(userId, resumeId);
//        return new ResponseEntity(new ApiRes("이력서 삭제 성공", HttpStatus.ACCEPTED), HttpStatus.ACCEPTED);
//
//    }
//
//    @ApiOperation(value = "이력서 다운로드")
//    @GetMapping("/download/{fileName}")
//    public ResponseEntity<byte[]> download(@PathVariable String fileName) throws IOException {
//        return s3Service.getObject(fileName);
//    }
//
//}
