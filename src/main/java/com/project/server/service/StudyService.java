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



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Study findById(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Board is not Found!")));
        return study;
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
