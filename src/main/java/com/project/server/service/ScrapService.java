package com.project.server.service;

import com.project.server.entity.JobCategory;
import com.project.server.entity.Scrap;
import com.project.server.entity.Study;
import com.project.server.entity.Users;
import com.project.server.http.response.ApiRes;
import com.project.server.http.response.StudyResponse;
import com.project.server.repository.ScrapRepository;
import com.project.server.repository.Study.StudyRepository;
import com.project.server.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final UsersRepository usersRepository;
    private final ScrapRepository scrapRepository;
    private final StudyRepository studyRepository;


    public Object getScrapsByUserId(Users users) {
        List<Scrap> scrapList = scrapRepository.findAllByUsers_Id(users.getId());
        List<StudyResponse> studyList = new ArrayList<>();

        for (int i = 0; i < scrapList.size(); i++)
            studyList.add(new StudyResponse(scrapList.get(i).getStudy()));
        System.out.println(studyList);

        return studyList;
    }


    public ResponseEntity addScrapToStudy(Users users, Long studyId) {
        Scrap newScrap = new Scrap();
        Users saveUsers = usersRepository.findById(users.getId()).get();
        System.out.println(saveUsers);

        Study study = studyRepository.findById(studyId).get();
        newScrap.setUsers(saveUsers);
        newScrap.setStudy(study);
        scrapRepository.save(newScrap);


        return new ResponseEntity(new ApiRes("스터디 스크랩 성공", HttpStatus.OK), HttpStatus.OK);

    }

    public ResponseEntity removeScrapFromStudy(Users users, Long studyId) {
        try {
            Optional<Users> userOptional = usersRepository.findById(users.getId());
            Optional<Study> studyOptional = studyRepository.findById(studyId);

            if (userOptional.isPresent() && studyOptional.isPresent()) {
                Users user = userOptional.get();
                Study study = studyOptional.get();

                Scrap deleteScrap = scrapRepository.findByUsersAndStudy(user, study);
                if (deleteScrap != null) {
                    scrapRepository.delete(deleteScrap);
                    return new ResponseEntity(new ApiRes("스크랩 취소 성공", HttpStatus.OK), HttpStatus.OK);
                } else {
                    return new ResponseEntity(new ApiRes("스크랩 존재하지 않습니다.", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity(new ApiRes("스터디 혹은 유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("Failed to remove scrap", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}



