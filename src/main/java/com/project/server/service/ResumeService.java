package com.project.server.service;


import com.project.server.entity.Resume;
import com.project.server.entity.User;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.request.ResumeRequest;
import com.project.server.http.response.ApiResponse;
import com.project.server.repository.ResumeRepository;
import com.project.server.repository.UserRepository;
import com.project.server.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    private Resume resume;
    @Transactional
    public ResponseEntity saveResume(ResumeRequest resumeRequest, Authentication authentication){
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        Resume resume = Resume.builder()
                .major(resumeRequest.getMajor())
                .career(resumeRequest.getCareer())
                .school(resumeRequest.getSchool())
                .award(resumeRequest.getAward())
                .profileImgUrl(resumeRequest.getProfileImgUrl())
                .area(resumeRequest.getArea())
                .contents(resumeRequest.getContents())
                .portfolioUrl(resumeRequest.getPortfolioUrl())
                .build();
        resumeRepository.save(resume);
        user.setResume(resume);
        return new ResponseEntity(new ApiResponse("이력서 작성 성공", HttpStatus.CREATED,resumeRepository.save(resume)), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity getResumeList(){
        List<Resume> all = resumeRepository.findAll();
        List<ResumeRequest> ResumeList=new ArrayList<>();

        for (Resume resume : all){
            ResumeRequest resumeRequest = ResumeRequest.builder()
                    .major(resume.getMajor())
                    .career(resume.getCareer())
                    .school(resume.getSchool())
                    .award(resume.getAward())
                    .profileImgUrl(resume.getProfileImgUrl())
                    .area(resume.getArea())
                    .contents(resume.getContents())
                    .portfolioUrl(resume.getPortfolioUrl())
                    .build();

            ResumeList.add(resumeRequest);
        }
        return new ResponseEntity(new ApiResponse("이력서 읽기 성공", HttpStatus.OK,getResumeList()), HttpStatus.OK);
    }
    // 글 하나 가져오기
    public ResponseEntity findOneResume(Long id) {
        Resume resume = resumeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 없습니다")
        );
        return new ResponseEntity(new ApiResponse("이력서 읽기 성공", HttpStatus.OK,resume), HttpStatus.OK);

    }

    //글 수정하기
    @Transactional
    public ResponseEntity updateBoard(Long id, ResumeRequest request) {
        // 어떤 게시판인지 찾기
        Resume resume = resumeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
            resume.update(request);
        return new ResponseEntity(new ApiResponse("이력서 수정 성공", HttpStatus.OK), HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity deleteResume(Long id) {
        // 어떤 게시판인지 찾기
        Resume resume = resumeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        String url = s3Service.deleteFile(resume.getPortfolioUrl());
        System.out.println(id);
        resumeRepository.deleteById(id);
        return new ResponseEntity(new ApiResponse("이력서 삭제 성공", HttpStatus.OK), HttpStatus.OK);

    }

}
