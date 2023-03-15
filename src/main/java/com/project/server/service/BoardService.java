//package com.project.server.service;
//
//
//import com.project.server.entity.Resume;
//import com.project.server.entity.StudyCategory;
//import com.project.server.entity.User;
//import com.project.server.exception.ResourceNotFoundException;
//import com.project.server.http.request.ResumeRequest;
//import com.project.server.http.request.StudyRequest;
//import com.project.server.http.response.ApiResponse;
//import com.project.server.repository.ResumeRepository;
//import com.project.server.repository.UserRepository;
//import com.project.server.security.CustomUserPrincipal;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class ResumeService {
//    private final ResumeRepository resumeRepository;
//    private final UserRepository userRepository;
//    private final S3Service s3Service;
//
//    private Resume resume;
//    @Transactional
//    public ResponseEntity saveResume(ResumeRequest resumeRequest, Authentication authentication){
//        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
//        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
//        Resume resume = Resume.builder()
//                .major(resumeRequest.getMajor())
//                .career(resumeRequest.getCareer())
//                .school(resumeRequest.getSchool())
//                .award(resumeRequest.getAward())
//                .profileImgUrl(resumeRequest.getProfileImgUrl())
//                .area(resumeRequest.getArea())
//                .contents(resumeRequest.getContents())
//                .portfolioUrl(resumeRequest.getPortfolioUrl())
//                .build();
//        resumeRepository.save(resume);
//        user.setResume(resume);
//        return new ResponseEntity(new ApiResponse("이력서 작성 성공", HttpStatus.CREATED,resumeRepository.save(resume)), HttpStatus.CREATED);
//    }
//
//    @Transactional
//    public ResponseEntity getResumeList(){
//        List<Resume> all = resumeRepository.findAll();
//        List<ResumeRequest> ResumeList=new ArrayList<>();
//
//        for (Resume resume : all){
//            ResumeRequest resumeRequest = ResumeRequest.builder()
//                    .major(resume.getMajor())
//                    .career(resume.getCareer())
//                    .school(resume.getSchool())
//                    .award(resume.getAward())
//                    .profileImgUrl(resume.getProfileImgUrl())
//                    .area(resume.getArea())
//                    .contents(resume.getContents())
//                    .portfolioUrl(resume.getPortfolioUrl())
//                    .build();
//
//            ResumeList.add(resumeRequest);
//        }
//        return new ResponseEntity(new ApiResponse("이력서 읽기 성공", HttpStatus.OK,getResumeList()), HttpStatus.OK);
//    }
//    // 글 하나 가져오기
//    public ResponseEntity findOneResume(Long id) {
//        Resume resume = resumeRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("게시글이 없습니다")
//        );
//        return new ResponseEntity(new ApiResponse("이력서 읽기 성공", HttpStatus.OK,resume), HttpStatus.OK);
//
//    }
//
//    //글 수정하기
//    @Transactional
//    public ResponseEntity updateBoard(Long id, ResumeRequest request) {
//        // 어떤 게시판인지 찾기
//        Resume resume = resumeRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
//        );
//        resume.update(request);
//        return new ResponseEntity(new ApiResponse("이력서 수정 성공", HttpStatus.OK), HttpStatus.OK);
//
//    }
//
//    @Transactional
//    public ResponseEntity deleteResume(Authentication authentication,Long id) {
//        // 어떤 게시판인지 찾기
//        Resume resume = resumeRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
//        );
//        String url = s3Service.deleteFile(resume.getPortfolioUrl());
//        System.out.println(id);
//        resumeRepository.deleteById(id);
//        return new ResponseEntity(new ApiResponse("이력서 삭제 성공", HttpStatus.OK), HttpStatus.OK);
//
//    }
//
//}
//

 //return amazonS3.getUrl(bucket, key).toString();







//         package com.project.server.service;
//
// import com.amazonaws.AmazonServiceException;
// import com.amazonaws.SdkClientException;
// import com.amazonaws.services.s3.AmazonS3;
// import com.amazonaws.services.s3.model.*;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;
//
// import java.io.IOException;
// import java.util.List;
// import java.util.UUID;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class S3Service {
//    //@Value("${cloud.aws.s3.bucket}")
//    private String bucket ="sideproject-s3-bucket";
//    private final AmazonS3 amazonS3;
//
//    public String uploadFile(MultipartFile multipartFile) throws IOException {
//        String fileName = multipartFile.getOriginalFilename();
//        String key = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
//
//        //파일 형식 구하기
//        String ext = fileName.split("\\.")[1];
//        String contentType = "";
//
//        //content type을 지정해서 올려주지 않으면 자동으로 "application/octet-stream"으로 고정이 되서 링크 클릭시 웹에서 열리는게 아니라 자동 다운이 시작됨.
//        switch (ext) {
//            case "jpeg":
//                contentType = "image/jpeg";
//                break;
//            case "png":
//                contentType = "image/png";
//                break;
//            case "txt":
//                contentType = "text/plain";
//                break;
//            case "csv":
//                contentType = "text/csv";
//                break;
//        }
//
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentType(contentType);
//
//            amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
//                    .withCannedAcl(CannedAccessControlList.PublicRead));
//        } catch (AmazonServiceException e) {
//            e.printStackTrace();
//        } catch (SdkClientException e) {
//            e.printStackTrace();
//        }
//
//        //object 정보 가져오기
//        ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(bucket);
//        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();
//
//        for (S3ObjectSummary object: objectSummaries) {
//            System.out.println("object = " + object.toString());
//        }
//        return amazonS3.getUrl(bucket, key).toString();
//    }
//    //파일 삭제
//    public String deleteFile(String fileName){
//        System.out.println(fileName);
//        DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
//        amazonS3.deleteObject(request);
//        return fileName;
//    }
//}