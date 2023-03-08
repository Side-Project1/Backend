package com.project.server.controller;

import com.project.server.http.request.EmailRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Email", description = "Email API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
@RestController
public class EmailController {
    private final EmailService mailService;

    @Operation(tags = "Email", summary = "인증 이메일 보내기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("/send")
    public ResponseEntity sendMail(@RequestBody EmailRequest emailRequest) {
        ApiRes apiRes = mailService.send(emailRequest);
        return new ResponseEntity(apiRes, apiRes.getStatus());
    }

    @Operation(tags = "Email", summary = "인증 이메일 번호 확인하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/confirm")
    public ResponseEntity confirm(@RequestParam("token") String token) {
        ApiRes apiRes = mailService.confirm(token);
        return new ResponseEntity(apiRes, apiRes.getStatus());
    }

    @Operation(tags = "Email", summary = "이메일로 아이디 찾기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("/findId")
    public ResponseEntity findId(@RequestBody EmailRequest emailRequest){
        return mailService.findId(emailRequest);
    }

//    @PostMapping("/findPassword")
//    public ResponseEntity findById(@RequestBody EmailRequest emailRequest){
//        return mailService.findPassword(findPwRequest);
//    }
}
