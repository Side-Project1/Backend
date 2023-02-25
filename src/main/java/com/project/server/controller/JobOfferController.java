package com.project.server.controller;

import com.project.server.entity.JobOffer;
import com.project.server.http.request.JobOfferRequest;
import com.project.server.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/jobOffer")
public class JobOfferController {
    private final JobOfferService jobOfferService;

    @PostMapping("")
    public ResponseEntity createJobOffer(@RequestBody JobOfferRequest jobOfferRequest) {
        return jobOfferService.creatJobOffer(jobOfferRequest);
    }

    @GetMapping("{jobOfferSn}")
    public ResponseEntity getJobOffer(@PathVariable("jobOfferSn") Integer id){
        return jobOfferService.getJobOffer(id);
    }

    @PutMapping("{jobOfferSn")
    public ResponseEntity updateJobOffer(@PathVariable("jobOfferSn") Integer id, @RequestBody JobOfferRequest jobOfferRequest) {
        return jobOfferService.updateJobOffer(id, jobOfferRequest);
    }

    @PutMapping("{jobOfferSn}/deadcheck")
    public ResponseEntity deadLineCheck(@PathVariable("jobOfferSn") Integer id) {
        return jobOfferService.deadLineCheck(id);
    }

    @DeleteMapping("{jobOfferSn")
    public ResponseEntity deleteJobOffer(@PathVariable("jobOfferSn") Integer id) {
        return jobOfferService.deleteJobOffer(id);
    }
}
