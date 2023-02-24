package com.project.server.controller;

import com.project.server.entity.Board;
import com.project.server.entity.Resume;
import com.project.server.entity.User;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.request.ResumeRequest;
import com.project.server.http.response.ApiResponse;
import com.project.server.repository.UserRepository;
import com.project.server.security.CustomUserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.project.server.http.response.ResumeResponse;
import com.project.server.repository.ResumeRepository;
import com.project.server.service.ResumeService;
import com.project.server.service.S3Service;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;
    private final UserRepository userRepository;
    private final  ResumeRepository resumeRepository;
    private Resume resume;

    private final S3Service s3Service;

    //글 등록
    @PostMapping(value="/resume/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void savePost(ResumeRequest request,Authentication authentication) throws IOException {
        String url = s3Service.uploadFile(request.getFile());
        request.setProfileImgUrl(url);
        resumeService.saveResume(request,authentication);
    }

    //전체 이력서 조회
    @GetMapping("/api/resume/posts")
    public List<ResumeRequest> findPosts(){
        List<Resume> all = resumeRepository.findAll();
        List<ResumeRequest> allPost = new ArrayList<>();

        for(Resume resume : all){
            ResumeRequest build = ResumeRequest.builder()
                    .major(resume.getMajor())
                    .career(resume.getCareer())
                    .school(resume.getSchool())
                    .award(resume.getAward())
                    .profileImgUrl(resume.getProfileImgUrl())
                    .area(resume.getArea())
                    .contents(resume.getContents())
                    .portfolioUrl(resume.getPortfolioUrl())
                    .build();

            allPost.add(build);
        }

        return allPost;
    }

    // 내 이력서 조회
    @GetMapping("/api/resume")
    public ResponseEntity getOneResume(Authentication authentication) {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        //System.out.println(user.getResume().getId());
        return resumeService.findOneResume(user.getResume().getId());
    }

    // 글 수정
    @PutMapping("/api/resume")
    public ResponseEntity updateBoard(Authentication authentication, ResumeRequest request) {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        return resumeService.updateBoard(user.getResume().getId(), request);
    }

    // 글 삭제
    @DeleteMapping("/api/resume")
    public ResponseEntity deleteResume(Authentication authentication) {

        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        return resumeService.deleteResume(user.getResume().getId());
    }

}
