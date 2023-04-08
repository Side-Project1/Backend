package com.project.server.controller;

import com.project.server.service.JobCategoryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "JobCategory Controller", tags = {"Category"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/category")
public class JobCategoryController {
    private final JobCategoryService jobCategoryService;

    @GetMapping("")
    public ResponseEntity getCategoryList() {
        return jobCategoryService.getCategoryList();
    }
}
