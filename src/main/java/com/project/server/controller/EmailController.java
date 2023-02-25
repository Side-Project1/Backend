package com.project.server.controller;

import com.project.server.http.request.EmailRequest;
import com.project.server.http.request.FindPwRequest;
import com.project.server.http.response.ApiRes;
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
        ApiRes apiRes = mailService.send(emailRequest);
        return new ResponseEntity(apiRes, apiRes.getStatus());
    }

    @GetMapping("/confirm")
    public ResponseEntity confirm(@RequestParam("token") String token) {
        ApiRes apiRes = mailService.confirm(token);
        return new ResponseEntity(apiRes, apiRes.getStatus());
    }

    @PostMapping("/findId")
    public ResponseEntity findId(@RequestBody EmailRequest emailRequest){
        return mailService.findId(emailRequest);
    }

//    @PostMapping("/findPassword")
//    public ResponseEntity findById(@RequestBody EmailRequest emailRequest){
//        return mailService.findPassword(findPwRequest);
//    }
}
