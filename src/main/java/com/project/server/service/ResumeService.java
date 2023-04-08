package com.project.server.service;

import com.project.server.entity.Photo;
import com.project.server.entity.Resume;
import com.project.server.entity.User;
import com.project.server.http.request.ResumeRequest;
import com.project.server.repository.PhotoRepository;
import com.project.server.repository.ResumeRepository;
import com.project.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Slf4j
@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
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
    public Long writeResume(String userId, ResumeRequest resumeRequest ,MultipartFile[] files) throws IOException {
        User user = userRepository.findByUserId(userId) .orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
        Resume resume = Resume.builder()
                .title(resumeRequest.getTitle())
                .certificate(resumeRequest.getCertificate())
                .career(resumeRequest.getCareer())
                .school(resumeRequest.getSchool())
                .job(resumeRequest.getJob())
                .link(resumeRequest.getLink())
                .user(user)
                .build();
        Resume saveResume = resumeRepository.save(resume);
        for (MultipartFile file : files) {
            String fileNames = file.getOriginalFilename();
            String fileUrl = s3Service.uploadFile(file);
            long fileSize = file.getSize();
            Photo photo =Photo.builder()
                    .fileName(fileNames)
                    .resume(resume)
                    .fileUrl(fileUrl)
                    .fileSize(fileSize)
                    .build();
            photoRepository.save(photo);
            saveResume.writePhoto(photo);

        }

        user.writeResume(saveResume);

        return saveResume.getId();
    }

    //수정
    @Transactional
    public void updateResume(String userId, Long resumeId, ResumeRequest resumeRequest , MultipartFile[] files) throws IOException {
        User user = userRepository.findByUserId(userId) .orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(() -> new IllegalArgumentException(String.format("study is not Found!")));
        List<Photo> photo=photoRepository.findByResumeId(resumeId);
        checkResumeLoginUser(user, resume);
        resumeRepository.deleteById(resumeId);
        Resume resumes=Resume.builder()
                .title(resumeRequest.getTitle())
                .certificate(resumeRequest.getCertificate())
                .career(resumeRequest.getCareer())
                .school(resumeRequest.getSchool())
                .job(resumeRequest.getJob())
                .link(resumeRequest.getLink())
                .user(user)
                .build();
        Resume saveResume = resumeRepository.save(resumes);
        photoRepository.deleteAll(photo);
        for (Photo existingFile : photo) {
            s3Service.deleteFile(existingFile.getFileUrl());

        }

// 파일 정보를 파일 테이블에 저장

        for (MultipartFile file : files) {
            String fileNames = file.getOriginalFilename();
            String fileUrl = s3Service.uploadFile(file);
            long fileSize = file.getSize();
            Photo photo1= Photo.builder().fileName(fileNames)
                    .resume(saveResume)
                    .fileUrl(fileUrl)
                    .fileSize(fileSize)
                    .build();
            photoRepository.save(photo1);
            saveResume.writePhoto(photo1);
        }
        resumeRepository.save(saveResume);

    }

    //삭제
    @Transactional
    public void deleteResumeById(String userId, Long resumeId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(() -> new IllegalArgumentException(String.format("resume is not Found!")));
        List<Photo> photo=photoRepository.findByResumeId(resumeId);
        checkResumeLoginUser(user, resume);

        for (Photo existingFile : photo) {
            s3Service.deleteFile(existingFile.getFileUrl());
        }
        photoRepository.deleteAll(photo);
        photoRepository.deleteByResumeId(resumeId);
        resumeRepository.deleteById(resumeId);
    }
    private void checkResumeLoginUser(User user, Resume resume) {
        if (!Objects.equals(resume.getUser().getUserId(), user.getUserId())) {
            throw new IllegalArgumentException("해당 게시물을 수정할 권한이 없습니다.");
        }

    }
}

