package com.project.server.controller;

import com.project.server.entity.Scrap;
import com.project.server.entity.Study;
import com.project.server.entity.Users;

import com.project.server.service.ScrapService;
import com.project.server.util.AuthUser;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Scrap", description = "스크랩 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/study/scrap")
public class ScrapController {

    private final ScrapService scrapService;

    @Operation(tags = "Study", summary = "스터디 스크랩 목록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @ApiOperation(value = "스크랩 상제 조회")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("")
    public Object getScrapsByUserId(@ApiIgnore @AuthUser Users users) {
        return scrapService.getScrapsByUserId(users);

    }


    @Operation(tags = "Study", summary = "스터디 스크랩 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/{studyId}")
    public ResponseEntity addScrapToStudy(@ApiIgnore @AuthUser Users users, @PathVariable("studyId") Long studyId) {
        return scrapService.addScrapToStudy(users,studyId);


    }

    @Operation(tags = "Study", summary = "스터디 스크랩 취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/{studyId}")
    public ResponseEntity removeScrapFromStudy(@ApiIgnore @AuthUser Users users,@PathVariable("studyId") Long studyId) {

        return scrapService.removeScrapFromStudy(users, studyId);

    }


}
