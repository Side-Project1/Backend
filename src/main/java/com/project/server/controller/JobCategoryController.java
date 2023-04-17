package com.project.server.controller;

import com.project.server.service.JobCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "JobCategory", description = "카테고리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/category")
public class JobCategoryController {
    private final JobCategoryService jobCategoryService;

    @Operation(tags = "JobCategory", summary = "카테고리 전체 목록 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("")
    public ResponseEntity getCategoryList() {
        return jobCategoryService.getCategoryList();
    }

    @Operation(tags = "JobCategory", summary = "카테고리 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity getCategory(@Parameter(description = "카테고리 일련번호") @PathVariable(name = "categoryId") Long id) {
        return jobCategoryService.getCategory(id);
    }

    @Operation(tags = "JobCategory", summary = "카테고리 목록 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/sub")
    public ResponseEntity getSubCategory(@Parameter(description = "부모 카테고리 일련번호") @RequestParam(name = "categoryId") String parentCategory) {
        return jobCategoryService.getSubCategory(parentCategory);
    }
}
