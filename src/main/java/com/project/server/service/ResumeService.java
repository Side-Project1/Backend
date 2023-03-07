package com.project.server.service;

import com.project.server.entity.Resume;
import com.project.server.entity.User;
import com.project.server.http.request.ResumeRequest;
import com.project.server.repository.ResumeRepository;
import com.project.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Slf4j
@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;




    //선택한 이력서 조회
    public Resume findById(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Resume is not Found!")));

        return resume;
    }

    //사용자가 작성한 이력서 모아보기
    @Transactional
    //UUID 변경
    public List<Resume> findByUser(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(()->new NullPointerException("user가 존재하지 않습니다."));
        return resumeRepository.findByUser(user);
    }

    //글 생성
    @Transactional
    public Long writeResume(String userId, ResumeRequest resumeRequest) {
        User user = userRepository.findByUserId(userId) .orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
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
        Resume saveResume = resumeRepository.save(resume);
        user.writeResume(saveResume);
        return saveResume.getId();
    }

    //수정
    @Transactional
    public void updateResume(String userId, Long resumeId, ResumeRequest resumeRequest , MultipartFile profile) {
        User user = userRepository.findByUserId(userId) .orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(() -> new IllegalArgumentException(String.format("study is not Found!")));
        checkResumeLoginUser(user, resume);
        resume.update(resumeRequest);

    }

    //삭제
    @Transactional
    public void deleteResumeById(String userId, Long studyId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
        Resume resume = resumeRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException(String.format("study is not Found!")));
        checkResumeLoginUser(user, resume);

        //저장할 때 애초에 스트링으로 바꾸기
        String s = resume.getPortfolioUrl().replace("[", "");
        String ss = s.replace("]", "");
        List<String> lists = Stream.of(ss.split(", ")).toList();


        if (lists.size() == 1) {
            s3Service.deleteFile(resume.getPortfolioUrl());

        } else {
             s3Service.deleteFiles(lists);
        }
        s3Service.deleteFile(resume.getProfileImgUrl());
        resumeRepository.deleteById(studyId);
    }
    private void checkResumeLoginUser(User user, Resume resume) {
        if (!Objects.equals(resume.getUser().getUserId(), user.getUserId())) {
            throw new IllegalArgumentException("해당 게시물을 수정할 권한이 없습니다.");
        }

    }
}

