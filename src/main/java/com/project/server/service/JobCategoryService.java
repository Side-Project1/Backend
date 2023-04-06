package com.project.server.service;

import com.project.server.http.response.ApiRes;
import com.project.server.repository.JobCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JobCategoryService {
    private final JobCategoryRepository jobCategoryRepository;

    public ResponseEntity getCategoryList() {
        try {
            return new ResponseEntity(new ApiRes("카테고리 조회 성공", HttpStatus.OK, jobCategoryRepository.findAll()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("카테고리 조회 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
