package com.project.server.service;

import com.project.server.entity.*;
import com.project.server.http.request.StudyApplyRequest;
import com.project.server.http.request.StudyPageRequest;
import com.project.server.http.request.StudyRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.http.response.StudyPageResponse;
import com.project.server.http.response.StudyResponse;
import com.project.server.repository.PhotoRepository;
import com.project.server.repository.Study.StudyApplyRepository;
import com.project.server.repository.Study.StudyMemberRepository;
import com.project.server.repository.Study.StudyRepositoryCustom;
import com.project.server.repository.Study.StudyRepository;
import com.project.server.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.project.server.entity.StudyApplyStatus.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;



@RequiredArgsConstructor
@Slf4j
@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final UsersRepository usersRepository;
    private final StudyApplyRepository studyApplyRepository;
    private final PhotoRepository photoRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final S3Service s3Service;
    private final StudyRepositoryCustom studyRepositoryCustom;

    private static final String STUDY_VIEW_COUNT_KEY = "study:viewCount:";
    private static final int EXPIRATION_DAYS = 1;
    private final RedisTemplate redisTemplate;



    public class RedisUtil {
        public static long getUnixTime(LocalDateTime localDateTime) {
            return localDateTime.toEpochSecond(ZoneOffset.UTC);
        }
    }

    public ResponseEntity dead(Users users, Long studyId) {
        try {
            Study study = studyRepository.findById(studyId).orElseThrow(() -> new RuntimeException("Study not found"));
            study.setRecruitment(EnumStatus.Status.N);
            studyRepository.save(study);
            return new ResponseEntity(new ApiRes("스터디 마감 성공", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("스터디 마감  실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }


    }

        public ResponseEntity findById(Users users, Long studyId) {
        try {
            String studyViewCountKey = STUDY_VIEW_COUNT_KEY + studyId;

            HashOperations<String, String, Long> hashOps = redisTemplate.opsForHash();

            // 현재 시간을 UTC 기준으로 계산
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            LocalDateTime expirationTime = now.plusDays(EXPIRATION_DAYS);

            long unixTime = RedisUtil.getUnixTime(expirationTime);
            long viewCount = 0;

            // Redis hash에 사용자 이름과 만료 시간을 저장
            boolean isNewUser = hashOps.putIfAbsent(studyViewCountKey, users.getUserId(), expirationTime.toEpochSecond(ZoneOffset.UTC));
            if (!isNewUser) {
                // 이미 사용자가 저장되어 있으면 만료 시간을 가져옴
                Long expirationTimestamp = hashOps.get(studyViewCountKey, users.getUserId());
                if (expirationTimestamp != null && expirationTimestamp < now.toEpochSecond(ZoneOffset.UTC)) {
                    // 저장된 만료 시간이 지났으면 사용자 아이디와 만료 시간을 갱신
                    System.out.println("시간 만료");
                    hashOps.put(studyViewCountKey, users.getUserId(), expirationTime.toEpochSecond(ZoneOffset.UTC));
                } else {
                    // 아직 만료 시간이 지나지 않았으면 조회수를 증가시키지 않음
                    Study study = studyRepository.findById(studyId).orElseThrow(() -> new RuntimeException("Study not found"));
                    System.out.println(study.getViewCount());

                }
            } else {
                // 새로운 사용자라면 만료 시간(하루) 설정
                redisTemplate.expireAt(studyViewCountKey, Instant.ofEpochSecond(unixTime));
                viewCount = hashOps.increment(studyViewCountKey, "viewCount", 1L);

            }
            Study study = studyRepository.findById(studyId).orElseThrow(() -> new RuntimeException("Study not found"));
            study.setViewCount(viewCount);
            studyRepository.save(study);
            System.out.println(study.getViewCount());
            return new ResponseEntity(new ApiRes("스터디 페이지 조회 성공", HttpStatus.OK, new StudyResponse(study)), HttpStatus.OK);
        }  catch (Exception e) {
        return new ResponseEntity(new ApiRes("스터디 페이지 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

        // 조회수를 증가시키고 결과를 반환



    }
    public ResponseEntity getStudyList(Pageable pageable, StudyPageRequest studyPageRequest) {
        try {
            List<StudyPageResponse> studyPageResponseList = studyRepository.findPageStudy(pageable, studyPageRequest);
            studyPageResponseList.forEach(data -> data.setJobCategoryList(studyRepository.findById(data.getId()).get().getJobCategoryList()));
            return new ResponseEntity(new ApiRes("스터디 페이지 조회 성공", HttpStatus.OK, studyPageResponseList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("스터디 페이지 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    //글 생성
        @Transactional
        public ResponseEntity writeStudy(Users users, StudyRequest studyRequest, List<Long> sub_category, MultipartFile[] files) throws IOException {
            try {
                Study study = Study.builder()
                        .title(studyRequest.getTitle())
                        .author(users.getUserId())
                        .max(studyRequest.getMax())
                        .region(studyRequest.getRegion())
                        .contents(studyRequest.getContents())
                        .recruitment(EnumStatus.Status.Y)
                        .users(users)
                        .build();
                sub_category.forEach(id -> study.getJobCategoryList().add(new JobCategory(id)));
                Study savedstudy=studyRepository.save(study);
                System.out.println(studyRepository.save(study));
                Users saveUsers = usersRepository.findById(users.getId()).get();
                saveUsers.getStudies().add(study);
                String filex = files[0].getOriginalFilename();

                if (!filex.equals("")) {
                    for (MultipartFile file : files) {
                        Photo photo = Photo.builder()
                                .fileName(file.getOriginalFilename())
                                .study(study)
                                .fileUrl(s3Service.uploadFile(file))
                                .fileSize(file.getSize())
                                .build();
                        photoRepository.save(photo);
                        savedstudy.writePhoto(photo);

                    }

                }
                return new ResponseEntity(new ApiRes("스터디글 등록 성공", HttpStatus.OK, new StudyResponse(savedstudy)), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(new ApiRes("스터디글 등록 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }

        // 스터디 멤버 추가
        //글 생성

        public String addStudyMembers(Long studyID,Users users, StudyApplyRequest studyApplyRequest) {
            Study study = studyRepository.findById(studyID).orElseThrow(() -> new IllegalArgumentException(String.format(String.format("study is not Found!"))));
            Users user = usersRepository.findByUserId(users.getUserId()).orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
            StudyApply studyApply = StudyApply.builder()
                    .study(study)
                    .introduction(studyApplyRequest.getIntroduction())
                    .users(user)
                    .applyStatus(PENDING)

                    .build();

            studyApplyRepository.save(studyApply);
            return study.getMembers().toString();
        }


        public void approveStudyApply(Long studyId, Users users , String approve) {
            Users user = usersRepository.findByUserId(users.getUserId()).orElseThrow(() -> new IllegalArgumentException("user not found"));
            StudyApply studyApply = studyApplyRepository.findByStudyId(studyId)
                    .orElseThrow(() -> new IllegalArgumentException("study not found"));

            if (studyApply.getUsers().getId().equals(users.getId()) && approve.equals("승인")) {
                System.out.println(studyApply.getUsers());
                studyApply.setApplyStatus(APPROVED);
                studyApplyRepository.save(studyApply);
            //거절했을 경우
            } else{
                    studyApply.setApplyStatus(REJECTED);
                    studyApplyRepository.save(studyApply);
            }
            Study study = studyApply.getStudy();
            Users members = studyApply.getUsers();

            StudyMember studyMember = StudyMember.builder()
                    .study(study)
                    .users(members)
                    .build();
            studyMemberRepository.save(studyMember);
        }

        //수정
        @Transactional
        public ResponseEntity updateStudy(Users users, Long studyId,List<Long> sub_category, StudyRequest studyRequest, MultipartFile[] files) throws IOException {

            try {
                Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException(String.format("study is not Found!")));
                List<Photo> photo = photoRepository.findByStudyId(studyId);
                if (checkStudyLoginUser(users, study)) {
                    study.setTitle(studyRequest.getTitle());
                    study.setAuthor(users.getUserId());
                    study.setMax(studyRequest.getMax());
                    study.setRegion(studyRequest.getRegion());
                    study.setContents(studyRequest.getContents());
                    List<JobCategory> jobCategoryList = new ArrayList<>();
                    sub_category.forEach(id -> study.setJobCategoryList(Collections.singletonList(new JobCategory(id))));
                    study.setJobCategoryList(jobCategoryList);
                }

//            return new ResponseEntity(new ApiRes("스터디 업데이트 완료", HttpStatus.OK, study), HttpStatus.OK);
                photoRepository.deleteAll(photo);
                for (Photo existingFile : photo) {
                    s3Service.deleteFile(existingFile.getFileUrl());
                }
                String filex = files[0].getOriginalFilename();

                if (!filex.equals("")) {
                    // 파일 정보를 파일 테이블에 저장
                    for (MultipartFile file : files) {
                        String fileNames = file.getOriginalFilename();
                        String fileUrl = s3Service.uploadFile(file);
                        long fileSize = file.getSize();
                        Photo photo1 = Photo.builder().fileName(fileNames)
                                .study(study)
                                .fileUrl(fileUrl)
                                .fileSize(fileSize)
                                .build();
                        photoRepository.save(photo1);
                        study.writePhoto(photo1);
                    }
                }

                usersRepository.save(users);
                return new ResponseEntity(new ApiRes("스터디글 수정 성공", HttpStatus.OK, new StudyResponse(study)), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(new ApiRes("스터디글 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }

        //삭제
        @Transactional
        public ResponseEntity deleteStudyById(Users users, Long studyId) {
            try {
                Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException(String.format("stydy is not Found!")));
                List<Photo> photo = photoRepository.findByStudyId(studyId);
                if (checkStudyLoginUser(users, study)) {

                    for (Photo existingFile : photo) {
                        s3Service.deleteFile(existingFile.getFileUrl());
                    }

                    photoRepository.deleteByStudyId(studyId);
                    studyRepository.deleteById(studyId);
                }
                return new ResponseEntity(new ApiRes("스터디글 삭제 성공", HttpStatus.OK, new StudyResponse(study)), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(new ApiRes("스터디글 삭제 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }


        //수정 및 삭제 권한 체크
        private boolean checkStudyLoginUser(Users users, Study study) {
            if (!Objects.equals(study.getUsers().getUserId(), users.getUserId())) {
                return false;
            }
            return true;

        }
    }
