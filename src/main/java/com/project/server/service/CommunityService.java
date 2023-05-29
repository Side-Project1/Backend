package com.project.server.service;


import com.project.server.entity.*;
import com.project.server.http.request.CommunityRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.http.response.CommunityPageResponse;
import com.project.server.http.response.CommunityResponse;
import com.project.server.repository.Community.CommunityRepository;
import com.project.server.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;



@RequiredArgsConstructor
@Slf4j
@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UsersRepository usersRepository;
    private static final String COMMUNITY_VIEW_COUNT_KEY = "community:viewCount:";
    private static final int EXPIRATION_DAYS = 1;
    private final RedisTemplate redisTemplate;



    public class RedisUtil {
        public static long getUnixTime(LocalDateTime localDateTime) {
            return localDateTime.toEpochSecond(ZoneOffset.UTC);
        }
    }

    public ResponseEntity findById(Users users, Long communityId) {
        try {
            String communityViewCountKey = COMMUNITY_VIEW_COUNT_KEY + communityId;

            HashOperations<String, String, Long> hashOps = redisTemplate.opsForHash();

            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            LocalDateTime expirationTime = now.plusDays(EXPIRATION_DAYS);

            long unixTime = RedisUtil.getUnixTime(expirationTime);
            long viewCount = 0;

            // Redis hash에 사용자 이름과 만료 시간을 저장
            boolean isNewUser = hashOps.putIfAbsent(communityViewCountKey, users.getUserId(), expirationTime.toEpochSecond(ZoneOffset.UTC));
            if (!isNewUser) {
                // 이미 사용자가 저장되어 있으면 만료 시간을 가져옴
                Long expirationTimestamp = hashOps.get(communityViewCountKey, users.getUserId());
                if (expirationTimestamp != null && expirationTimestamp < now.toEpochSecond(ZoneOffset.UTC)) {
                    // 저장된 만료 시간이 지났으면 사용자 아이디와 만료 시간을 갱신
                    System.out.println("시간 만료");
                    hashOps.put(communityViewCountKey, users.getUserId(), expirationTime.toEpochSecond(ZoneOffset.UTC));
                } else {
                    // 아직 만료 시간이 지나지 않았으면 조회수를 증가시키지 않음
                    Community community = communityRepository.findById(communityId).orElseThrow(() -> new RuntimeException("Community not found"));
                    System.out.println(community.getViewCount());

                }
            } else {
                // 새로운 사용자라면 만료 시간(하루) 설정
                redisTemplate.expireAt(communityViewCountKey, Instant.ofEpochSecond(unixTime));
                viewCount = hashOps.increment(communityViewCountKey, "viewCount", 1L);

            }
            Community community = communityRepository.findById(communityId).orElseThrow(() -> new RuntimeException("Community not found"));
            community.setViewCount(viewCount);
            communityRepository.save(community);
            return new ResponseEntity(new ApiRes("커뮤니티 글 조회 성공", HttpStatus.OK, new CommunityResponse(community)), HttpStatus.OK);
        }  catch (Exception e) {
            return new ResponseEntity(new ApiRes("커뮤니티 글 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }
    public ResponseEntity getCommunityList(Pageable pageable, String title, String contents) {
        try {
            List<CommunityPageResponse> communityPageResponseList= communityRepository.findPageCommunity(pageable, title, contents);

            return new ResponseEntity(new ApiRes("커뮤니티 페이지 조회 성공", HttpStatus.OK, communityPageResponseList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("커뮤니티 페이지 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    //글 생성
    @Transactional
    public ResponseEntity writeCommunity(Users users, CommunityRequest communityRequest) throws IOException {
        try {
            Community community = Community.builder()
                    .title(communityRequest.getTitle())
                    .author(users.getUserId())
                    .contents(communityRequest.getContents())
                    .users(users)
                    .build();

             communityRepository.save(community);

            Users saveUsers = usersRepository.findById(users.getId()).get();
            saveUsers.getCommunityList().add(community);


            return new ResponseEntity(new ApiRes("커뮤니티 글 등록 성공", HttpStatus.OK, new CommunityResponse(community)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("커뮤니티 글 등록 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



    //수정
    @Transactional
    public ResponseEntity updateCommunity(Users users,Long communityId ,CommunityRequest communityRequest) throws IOException {

        try {
            Community community = communityRepository.findById(communityId).orElseThrow(() -> new IllegalArgumentException(String.format("community is not Found!")));

            if (checkCommunityLoginUser(users, community)) {
                System.out.println(communityRequest.getTitle());
                community.setTitle(communityRequest.getTitle());
                community.setContents(communityRequest.getContents());
                communityRepository.save(community);
            }

            usersRepository.save(users);
            return new ResponseEntity(new ApiRes("커뮤니티 글 수정 성공", HttpStatus.OK, new CommunityResponse(community)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("커뮤니티 글 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    //삭제
    @Transactional
    public ResponseEntity deleteCommunityById(Users users, Long communityId) {
        try {
            Community community = communityRepository.findById(communityId).orElseThrow(() -> new IllegalArgumentException(String.format("community is not Found!")));

            if (checkCommunityLoginUser(users, community)) {

                communityRepository.deleteById(communityId);
            }
            return new ResponseEntity(new ApiRes("커뮤니티 삭제 성공", HttpStatus.OK, new CommunityResponse(community)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("커뮤니티 삭제 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    //수정 및 삭제 권한 체크
    private boolean checkCommunityLoginUser(Users users, Community community) {
        if (!Objects.equals(community.getUsers().getUserId(), users.getUserId())) {
            return false;
        }
        return true;

    }
}
