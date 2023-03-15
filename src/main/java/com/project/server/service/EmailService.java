package com.project.server.service;

import com.project.server.entity.ConfirmMail;
import com.project.server.entity.User;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.request.EmailRequest;
import com.project.server.http.request.FindPwRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.repository.ConfirmMailRepository;
import com.project.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final ConfirmMailRepository confirmMailRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    public ApiRes send(EmailRequest emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            /**
             * 첨부 파일(Multipartfile) 보낼거면 true
             */
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getReceiver());
            mimeMessageHelper.setSubject("이메일 인증");
            /**
             * html 템플릿으로 보낼거면 true
             * plaintext로 보낼거면 false
             */


            ConfirmMail confirmMail = ConfirmMail.builder()
                    .expirationDate(LocalDateTime.now().plusMinutes(3L)) // 3분
                    .expired(false)
                    .email(emailMessage.getReceiver())
                    .build();
            confirmMailRepository.save(confirmMail);
            mimeMessageHelper.setText(confirmMail.getId().toString(), true);

            javaMailSender.send(mimeMessage);
//            log.info("sent email: {}", emailMessage.getMessage());
            return new ApiRes("전송 완료", HttpStatus.OK);
        } catch (MessagingException e) {
            log.error("[EmailService.send()] error {}", e.getMessage());
            return new ApiRes("전송 실패", HttpStatus.BAD_REQUEST);
        }
    }


    public ApiRes confirm(String token) {
        try{
            confirmMailRepository.findByIdAndExpirationDateAfterAndExpired(UUID.fromString(token), LocalDateTime.now(), false).orElseThrow(() -> new Exception());
            return new ApiRes("인증 완료", HttpStatus.OK);
        } catch (Exception e) {
            log.error("error {}", e.getMessage());
            return new ApiRes("인증 실패", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity findId(EmailRequest emailRequest){
        try{
            if(!userRepository.existsByEmail(emailRequest.getReceiver())) {
                return new ResponseEntity(new ApiRes("해당 이메일이 존재하지 않습니다.", HttpStatus.CONFLICT), HttpStatus.CONFLICT);
            }

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailRequest.getReceiver());
            mimeMessageHelper.setSubject("아이디 찾기");
            mimeMessageHelper.setText(userRepository.findByEmail(emailRequest.getReceiver()).get().getUserId(), true);
            javaMailSender.send(mimeMessage);

            log.error("아이디 찾기 메일 전송");
            return new ResponseEntity(new ApiRes("아이디 찾기 메일 전송 성공", HttpStatus.OK), HttpStatus.OK);
        } catch (MessagingException e) {
            log.error("[EmailService.send()] error {}", e.getMessage());
            return new ResponseEntity(new ApiRes("아이디 찾기 메일 전송 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("error {}", e.getMessage());
            return new ResponseEntity(new ApiRes(e.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

//    public ResponseEntity findPassword(EmailRequest emailRequest) {
//        try{
//            if(!userRepository.existsByEmail(emailRequest.getReceiver())) {
//                return new ResponseEntity(new ApiRes("해당 이메일이 존재하지 않습니다.", HttpStatus.CONFLICT), HttpStatus.CONFLICT);
//            }
//
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
//            mimeMessageHelper.setTo(emailRequest.getReceiver());
//            mimeMessageHelper.setSubject("비밀번호 찾기");
//            mimeMessageHelper.setText(userRepository.findByEmail(emailRequest.getReceiver()).get().getUserId(), true);
//            javaMailSender.send(mimeMessage);
//
//            return new ResponseEntity(new ApiRes("아이디 찾기 메일 전송 성공", HttpStatus.OK), HttpStatus.OK);
//        } catch (MessagingException e) {
//            log.error("[EmailService.send()] error {}", e.getMessage());
//            return new ResponseEntity(new ApiRes("아이디 찾기 메일 전송 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            log.error("error {}", e.getMessage());
//            return new ResponseEntity(new ApiRes(e.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
//        }
//    }
}