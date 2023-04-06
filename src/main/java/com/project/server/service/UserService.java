package com.project.server.service;

import com.project.server.entity.User;
import com.project.server.http.response.UserResponse;
import com.project.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity getUserInfo(User user) {
        UserResponse userResponse = new UserResponse(userRepository.findById(user.getId()).get());
        return new ResponseEntity(userResponse, HttpStatus.OK);
    }
}
