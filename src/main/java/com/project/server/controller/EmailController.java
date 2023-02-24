package com.project.server.controller;

import com.project.server.http.request.EmailRequest;
import com.project.server.http.request.FindPwRequest;
import com.project.server.http.response.ApiResponse;
import com.project.server.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
@RestController
public class EmailController {
    private final EmailService mailService;

    @PostMapping("/send")
    public ResponseEntity sendMail(@RequestBody EmailRequest emailRequest) {
        ApiResponse apiResponse = mailService.send(emailRequest);
        return new ResponseEntity(apiResponse, apiResponse.getStatus());
    }

    @GetMapping("/confirm")
    public ResponseEntity confirm(@RequestParam("token") String token) {
        ApiResponse apiResponse = mailService.confirm(token);
        return new ResponseEntity(apiResponse, apiResponse.getStatus());
    }

    @PostMapping("/findId")
    public void findId(@RequestBody EmailRequest emailRequest){
        mailService.findId(emailRequest);
    }

    @PostMapping("/findPassword")
    public void findById(@RequestBody FindPwRequest findPwRequest){
        mailService.findPassword(findPwRequest);
    }
}
