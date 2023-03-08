package com.project.server.controller;

import com.project.server.entity.JobOffer;
import com.project.server.entity.User;
import com.project.server.http.request.JobOfferPageRequest;
import com.project.server.http.request.JobOfferRequest;
import com.project.server.service.JobOfferService;
import com.project.server.util.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Tag(name="JobOffer", description = "구인 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/jobOffer")
public class JobOfferController {
    private final JobOfferService jobOfferService;

    @Operation(tags = "JobOffer", summary = "구인글 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("")
    public ResponseEntity createJobOffer(@ApiIgnore @AuthUser User user, @RequestBody JobOfferRequest jobOfferRequest) {
        return jobOfferService.creatJobOffer(user, jobOfferRequest);
    }

    @Operation(tags = "JobOffer", summary = "구인글 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("")
    public ResponseEntity getJobOfferList(@PageableDefault Pageable pageable, @RequestBody JobOfferPageRequest jobOfferPageRequest,
                                          @RequestParam(name = "category", required = false) String category){
        return jobOfferService.getJobOfferList(pageable, jobOfferPageRequest, category);
    }

    @Operation(tags = "JobOffer", summary = "구인글 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("{jobOfferSn}")
    public ResponseEntity getJobOffer(@PathVariable("jobOfferSn") Integer id){
        return jobOfferService.getJobOffer(id);
    }

    @Operation(tags = "JobOffer", summary = "구인글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("{jobOfferSn}")
    public ResponseEntity updateJobOffer(@ApiIgnore @AuthUser User user, @PathVariable("jobOfferSn") Integer id,
                                         @RequestBody JobOfferRequest jobOfferRequest) {
        return jobOfferService.updateJobOffer(user, id, jobOfferRequest);
    }

    @Operation(tags = "JobOffer", summary = "구인글 마감하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/dead/{jobOfferSn}")
    public ResponseEntity deadLineCheck(@ApiIgnore @AuthUser User user, @PathVariable("jobOfferSn") Integer id) {
        return jobOfferService.deadLineCheck(user, id);
    }

    @Operation(tags = "JobOffer", summary = "구인글 삭제하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("{jobOfferSn}")
    public ResponseEntity deleteJobOffer(@ApiIgnore @AuthUser User user, @PathVariable("jobOfferSn") Integer id) {
        return jobOfferService.deleteJobOffer(user, id);
    }
}
