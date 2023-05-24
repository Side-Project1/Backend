package com.project.server.service;

import com.project.server.entity.Users;
import com.project.server.http.request.AccountRequest;
import com.project.server.http.request.PasswordRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.http.response.UserResponse;
import com.project.server.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;

    public ResponseEntity getUserInfo(Users users) {
        UserResponse userResponse = new UserResponse(usersRepository.findById(users.getId()).get());
        return new ResponseEntity(new ApiRes("내 정보 조회 성공", HttpStatus.OK, userResponse), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity changeAcount(Users users, AccountRequest accountRequest) {
        try {
            Users user = usersRepository.findById(users.getId()).get();

            user.setUserId(accountRequest.getUserId());
            user.setUserName(accountRequest.getUserName());
            return new ResponseEntity(new ApiRes("계정 수정 성공", HttpStatus.OK, null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("계정 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity changePhone(Users users, String phone) {
        try {
            Users user = usersRepository.findById(users.getId()).get();

            user.setPhone(phone);
            return new ResponseEntity(new ApiRes("계정 수정 성공", HttpStatus.OK, null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("계정 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity changePassword(Users users, PasswordRequest passwordRequest) {
        try {
            Users user = usersRepository.findById(users.getId()).get();

            if (!user.getPassword().equals(passwordRequest.getNowPassword())) {
                throw new Exception("비밀번호 불일치");
            }

            user.setPassword(passwordRequest.getAfterPassword());

            return new ResponseEntity(new ApiRes("계정 수정 성공", HttpStatus.OK, null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("계정 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
