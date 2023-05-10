package com.project.server.service;

import com.project.server.entity.JobCategory;
import com.project.server.http.response.ApiRes;
import com.project.server.repository.JobCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class JobCategoryService {
    private final JobCategoryRepository jobCategoryRepository;

    public ResponseEntity getCategoryList() {
        try {
            return new ResponseEntity(new ApiRes("카테고리 목록 조회 성공", HttpStatus.OK, jobCategoryRepository.findAll()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("카테고리 목록 조회 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getCategory(Long id) {
        try {
            JobCategory jobCategory = jobCategoryRepository.findById(id).orElseThrow(()->new IllegalStateException("카테고리가 존재하지 않습니다"));
            return new ResponseEntity(new ApiRes("카테고리 조회 성공", HttpStatus.OK, jobCategory), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("카테고리 조회 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getSubCategory(String parentCategory) {
        try {
            List<JobCategory> jobCategory = jobCategoryRepository.findByParentCategory(parentCategory);
            return new ResponseEntity(new ApiRes("카테고리 조회 성공", HttpStatus.OK, jobCategory), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("카테고리 조회 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
