package com.project.server.service;

import com.project.server.entity.ConfirmMail;
import com.project.server.exception.BadRequestException;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.request.EmailRequest;
import com.project.server.http.response.ApiResponse;
import com.project.server.repository.ConfirmMailRepository;
import com.project.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
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

    public ApiResponse send(EmailRequest emailMessage) {
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
            return new ApiResponse("전송 완료", HttpStatus.OK);
        } catch (MessagingException e) {
            log.error("[EmailService.send()] error {}", e.getMessage());
            return new ApiResponse("전송 실패", HttpStatus.BAD_REQUEST);
        }
    }


    public ApiResponse confirm(String token) {
        try{
            confirmMailRepository.findByIdAndExpirationDateAfterAndExpired(UUID.fromString(token), LocalDateTime.now(), false).orElseThrow(() -> new Exception());
            return new ApiResponse("인증 완료", HttpStatus.OK);
        } catch (Exception e) {
            log.error("[EmailService.send()] error {}", e.getMessage());
            return new ApiResponse("인증 실패", HttpStatus.BAD_REQUEST);
        }
    }
}