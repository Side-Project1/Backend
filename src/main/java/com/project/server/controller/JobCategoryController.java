package com.project.server.controller;

import com.project.server.service.JobCategoryService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{categoryId}")
    public ResponseEntity getCategory(@PathVariable(name = "categoryId") Long id) {
        return jobCategoryService.getCategory(id);
    }

    @GetMapping("/sub")
    public ResponseEntity getSubCategory(@RequestParam(name = "category") String parentCategory) {
        return jobCategoryService.getSubCategory(parentCategory);
    }
}
