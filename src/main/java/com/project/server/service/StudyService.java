package com.project.server.service;

import com.project.server.entity.*;
import com.project.server.http.request.StudyApplyRequest;
import com.project.server.http.request.StudyRequest;
import com.project.server.repository.PhotoRepository;
import com.project.server.repository.Study.StudyApplyRepository;
import com.project.server.repository.Study.StudyMemberRepository;
import com.project.server.repository.StudyRepository;
import com.project.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.project.server.entity.StudyApplyStatus.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyApplyRepository studyApplyRepository;
    private final PhotoRepository photoRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final S3Service s3Service;

    private static final String STUDY_VIEW_COUNT_KEY = "study:viewCount:";
    private static final int EXPIRATION_DAYS = 1;
    private final RedisTemplate redisTemplate;


    public class RedisUtil {
        public static long getUnixTime(LocalDateTime localDateTime) {
            return localDateTime.toEpochSecond(ZoneOffset.UTC);
        }
    }

    public Long findById(Long studyId, String userName) {
        String studyViewCountKey = STUDY_VIEW_COUNT_KEY + studyId;
        HashOperations<String, String, Long> hashOps = redisTemplate.opsForHash();

        // 현재 시간을 UTC 기준으로 계산
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expirationTime = now.plusDays(EXPIRATION_DAYS);

        long unixTime = RedisUtil.getUnixTime(expirationTime);

        // Redis hash에 사용자 이름과 만료 시간을 저장
        boolean isNewUser = hashOps.putIfAbsent(studyViewCountKey, userName, expirationTime.toEpochSecond(ZoneOffset.UTC));
        if (!isNewUser) {
            // 이미 사용자가 저장되어 있으면 만료 시간을 가져옴
            Long expirationTimestamp = hashOps.get(studyViewCountKey, userName);
            if (expirationTimestamp != null && expirationTimestamp < now.toEpochSecond(ZoneOffset.UTC)) {
                // 저장된 만료 시간이 지났으면 사용자 아이디와 만료 시간을 갱신
                System.out.println("시간 만료");
                hashOps.put(studyViewCountKey, userName, expirationTime.toEpochSecond(ZoneOffset.UTC));
            } else {
                // 아직 만료 시간이 지나지 않았으면 조회수를 증가시키지 않음
                Study study = studyRepository.findById(studyId).orElseThrow(() -> new RuntimeException("Study not found"));
                System.out.println(study.getViewCount());
                return study.getViewCount();
            }
        } else {
            // 새로운 사용자라면 만료 시간(하루) 설정
            redisTemplate.expireAt(studyViewCountKey, Instant.ofEpochSecond(unixTime));
        }

        // 조회수를 증가시키고 결과를 반환
        long viewCount = hashOps.increment(studyViewCountKey, "viewCount", 1L);
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new RuntimeException("Study not found"));
        study.setViewCount(viewCount);
        studyRepository.save(study);
        System.out.println(study.getViewCount());

        return study.getViewCount();
    }

        //전체 조회
        @Transactional(readOnly = true)
        public List<Study> findAll() {
            return studyRepository.findAll();

        }

        @Transactional
        public Page<Study> getStudyList(Pageable pageable, String title, String contents) {
            int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
            pageable = PageRequest.of(page, 10);

            return studyRepository.findBySearchOption(pageable, title, contents);

        }

        @Transactional(readOnly = true)
        public List<Study> findByUser(String userId) {
            //수정
            Optional<User> user = userRepository.findByUserId(userId);
            return studyRepository.findByUser(user);
        }

        //글 생성
        @Transactional
        public Long writeStudy(String userId, StudyRequest studyRequest,MultipartFile[] files) throws IOException {
            User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
            Study study = Study.builder()
                    .title(studyRequest.getTitle())
                    .author(user.getUserId())
                    .max(studyRequest.getMax())
                    .region(studyRequest.getRegion())
                    .contents(studyRequest.getContents())
                    .owner(user.getUserId())
                    .build();
            Study savedStudy = studyRepository.save(study);
            user.writeStudy(savedStudy);
            System.out.println(files[0].getOriginalFilename());
            String filex=files[0].getOriginalFilename();

            if(!filex.equals("")){
                for (MultipartFile file : files) {
                    String fileNames = file.getOriginalFilename();
                    String fileUrl = s3Service.uploadFile(file);
                    long fileSize = file.getSize();
                    Photo photo = Photo.builder()
                            .fileName(fileNames)
                            .study(study)
                            .fileUrl(fileUrl)
                            .fileSize(fileSize)
                            .build();
                    photoRepository.save(photo);
                    savedStudy.writePhoto(photo);
                }
            }

            return savedStudy.getId();
        }

        // 스터디 멤버 추가
        //글 생성
        @Transactional
        public String addStudyMembers(Long studyID, String userID, StudyApplyRequest studyApplyRequest) {
            Study study = studyRepository.findById(studyID).orElseThrow(() -> new IllegalArgumentException(String.format(String.format("study is not Found!"))));
            User user = userRepository.findByUserId(userID).orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
            StudyApply studyApply = StudyApply.builder()
                    .study(study)
                    .introduction(studyApplyRequest.getIntroduction())
                    .user(user)
                    .applyStatus(PENDING)
                    .build();

            StudyApply savedStudyApply = studyApplyRepository.save(studyApply);
            return study.getMembers().toString();
        }

        //스터디 승인 (파라미터 값으로 승인, 거절 프론트에서 어떻게 받아올까~)
        public void approveStudyApply(Long studyId, String userId) {
            User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
            StudyApply studyApply = studyApplyRepository.findByStudyId(studyId)
                    .orElseThrow(() -> new IllegalArgumentException("study not found"));

            if (studyApply.getUser().getId().equals(user.getId())) {
                System.out.println(studyApply.getUser());
                studyApply.setApplyStatus(APPROVED);
                studyApplyRepository.save(studyApply);
            }
            //거절했을 경우
//        } else if (studyApply.getUser().getId().equals(user.getId())){
//                System.out.println(studyApply.getUser());
//                studyApply.setApplyStatus(REJECTED);
//                studyApplyRepository.save(studyApply);
//        }
            Study study = studyApply.getStudy();
            User members = studyApply.getUser();

            StudyMember studyMember = StudyMember.builder()
                    .study(study)
                    .user(members)
                    .build();
            studyMemberRepository.save(studyMember);
        }

        //수정
        @Transactional
        public void updateStudy(String userId, Long studyId, StudyRequest studyRequest, MultipartFile[] files) throws IOException {
            User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
            Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException(String.format("study is not Found!")));
            List<Photo> photo = photoRepository.findByStudyId(studyId);
            checkStudyLoginUser(user, study);

            studyRepository.deleteById(studyId);
            Study studies = study.builder()
                    .title(studyRequest.getTitle())
                    .author(user.getUserId())
                    .max(studyRequest.getMax())
                    .region(studyRequest.getRegion())
                    .contents(studyRequest.getContents())
                    .owner(user.getUserId())
                    .build();

            Study saveStudy = studyRepository.save(studies);
            photoRepository.deleteAll(photo);
            for (Photo existingFile : photo) {
                s3Service.deleteFile(existingFile.getFileUrl());
            }
            String filex=files[0].getOriginalFilename();

            if(!filex.equals("")) {
                // 파일 정보를 파일 테이블에 저장
                for (MultipartFile file : files) {
                    String fileNames = file.getOriginalFilename();
                    String fileUrl = s3Service.uploadFile(file);
                    long fileSize = file.getSize();
                    Photo photo1 = Photo.builder().fileName(fileNames)
                            .study(saveStudy)
                            .fileUrl(fileUrl)
                            .fileSize(fileSize)
                            .build();
                    photoRepository.save(photo1);
                    saveStudy.writePhoto(photo1);
                }
            }
            studyRepository.save(saveStudy);
            user.writeStudy(saveStudy);
            userRepository.save(user);

        }

        //삭제
        @Transactional
        public void deleteStudyById(String userId, Long studyId) {
            User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
            Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException(String.format("stydy is not Found!")));
            List<Photo> photo = photoRepository.findByStudyId(studyId);
            checkStudyLoginUser(user, study);

            for (Photo existingFile : photo) {
                s3Service.deleteFile(existingFile.getFileUrl());
            }
            photoRepository.deleteByStudyId(studyId);
            studyRepository.deleteById(studyId);
        }

        //수정 및 삭제 권한 체크
        private void checkStudyLoginUser(User user, Study study) {
            if (!Objects.equals(study.getUser().getUserId(), user.getUserId())) {
                throw new IllegalArgumentException("해당 게시물을 수정할 권한이 없습니다.");
            }

        }
    }
