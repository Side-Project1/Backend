package com.project.server.service;

import com.project.server.entity.Users;
import com.project.server.http.response.ApiRes;
import com.project.server.http.response.UserResponse;
import com.project.server.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;

    public ResponseEntity getUserInfo(Users users) {
        UserResponse userResponse = new UserResponse(usersRepository.findById(users.getId()).get());
        return new ResponseEntity(new ApiRes("내 정보 조회 성공", HttpStatus.OK, userResponse), HttpStatus.OK);
    }
}
