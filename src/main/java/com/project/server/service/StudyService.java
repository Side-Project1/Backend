package com.project.server.service;

import com.project.server.entity.Study;
import com.project.server.entity.StudyCategory;
import com.project.server.entity.User;
import com.project.server.http.request.StudyRequest;
import com.project.server.repository.StudyRepository;
import com.project.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    private static final String STUDY_VIEW_COUNT_KEY = "study:viewCount:";
    private static final int EXPIRATION_DAYS = 1;
    private final RedisTemplate redisTemplate;

    public class RedisUtil {
        public static long getUnixTime(LocalDateTime localDateTime) {
            return localDateTime.toEpochSecond(ZoneOffset.UTC);
        }
    }



    @Transactional(readOnly = true)
    public Long findById(Long studyId, String username) {
            String studyViewCountKey = STUDY_VIEW_COUNT_KEY + studyId;
            HashOperations<String, String, Long> hashOps = redisTemplate.opsForHash();

            // 현재 시간을 UTC 기준으로 계산
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            LocalDateTime expirationTime = now.plusDays(EXPIRATION_DAYS);

            long unixTime = RedisUtil.getUnixTime(expirationTime);

            // Redis hash에 사용자 이름과 만료 시간을 저장
            boolean isNewUser = hashOps.putIfAbsent(studyViewCountKey,username, expirationTime.toEpochSecond(ZoneOffset.UTC));
            if (!isNewUser) {
                // 이미 사용자가 저장되어 있으면 만료 시간을 가져옴
                Long expirationTimestamp = hashOps.get(studyViewCountKey, username);
                if (expirationTimestamp != null && expirationTimestamp < now.toEpochSecond(ZoneOffset.UTC)) {
                    // 저장된 만료 시간이 지났으면 사용자 아이디와 만료 시간을 갱신
                    System.out.println("시간 만료");
                    hashOps.put(studyViewCountKey, username, expirationTime.toEpochSecond(ZoneOffset.UTC));
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
            System.out.println(study.getViewCount());

            return study.getViewCount();
    }

    //전체 조회
    @Transactional(readOnly = true)
    public List<Study> findAll() {
        return studyRepository.findAll();

    }

    @Transactional
    public Page<Study> getStudyList(Pageable pageable ,String title,String contents){
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10);

        return studyRepository.findBySearchOption(pageable,title,contents);

    }

    @Transactional(readOnly = true)
    public List<Study> findByCategory(StudyCategory category) {
        return studyRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Study> findByUser(String userId) {
        //수정
        Optional<User> user = userRepository.findByUserId(userId);
        return studyRepository.findByUser(user);
    }

    //글 생성
    @Transactional
    public Long writeStudy(String userId, StudyRequest studyRequest) {
        User user = userRepository.findByUserId(userId) .orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
        Study study = Study.builder()
                .title(studyRequest.getTitle())
                .author(user.getClass().getName())
                .max(studyRequest.getMax())
                .region(studyRequest.getRegion())
                .contents(studyRequest.getContents())
                .category(StudyCategory.art)
                .build();
        Study savedStudy = studyRepository.save(study);
        user.writeStudy(savedStudy);
        return savedStudy.getId();
    }

    //수정
    @Transactional
    public void updateStudy(String userId, Long studyId, StudyRequest studyRequest) {
        User user = userRepository.findByUserId(userId) .orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException(String.format("stydy is not Found!")));
        checkStudyLoginUser(user, study);
        study.update(
                studyRequest.getTitle(),
                studyRequest.getMax(),
                studyRequest.getRegion(),
                studyRequest.getContents(),
                studyRequest.getCategory());
    }

    //삭제
    @Transactional
    public void deleteStudyById(String userId, Long studyId) {
        User user = userRepository.findByUserId(userId) .orElseThrow(() -> new IllegalArgumentException(String.format("user is not Found!")));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException(String.format("stydy is not Found!")));
        checkStudyLoginUser(user, study);
        studyRepository.deleteById(studyId);
    }

    //수정 및 삭제 권한 체크
    private void checkStudyLoginUser(User user, Study study) {
        if (!Objects.equals(study.getUser().getUserId(), user.getUserId())) {
            throw new IllegalArgumentException("해당 게시물을 수정할 권한이 없습니다.");
        }

    }



}
