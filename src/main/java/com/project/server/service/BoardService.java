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
//        return new ResponseEntity(new ApiResponse("????????? ?????? ??????", HttpStatus.CREATED,resumeRepository.save(resume)), HttpStatus.CREATED);
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
//        return new ResponseEntity(new ApiResponse("????????? ?????? ??????", HttpStatus.OK,getResumeList()), HttpStatus.OK);
//    }
//    // ??? ?????? ????????????
//    public ResponseEntity findOneResume(Long id) {
//        Resume resume = resumeRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("???????????? ????????????")
//        );
//        return new ResponseEntity(new ApiResponse("????????? ?????? ??????", HttpStatus.OK,resume), HttpStatus.OK);
//
//    }
//
//    //??? ????????????
//    @Transactional
//    public ResponseEntity updateBoard(Long id, ResumeRequest request) {
//        // ?????? ??????????????? ??????
//        Resume resume = resumeRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("?????? ???????????? ???????????? ????????????.")
//        );
//        resume.update(request);
//        return new ResponseEntity(new ApiResponse("????????? ?????? ??????", HttpStatus.OK), HttpStatus.OK);
//
//    }
//
//    @Transactional
//    public ResponseEntity deleteResume(Authentication authentication,Long id) {
//        // ?????? ??????????????? ??????
//        Resume resume = resumeRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("?????? ???????????? ???????????? ????????????.")
//        );
//        String url = s3Service.deleteFile(resume.getPortfolioUrl());
//        System.out.println(id);
//        resumeRepository.deleteById(id);
//        return new ResponseEntity(new ApiResponse("????????? ?????? ??????", HttpStatus.OK), HttpStatus.OK);
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
//        //?????? ?????? ?????????
//        String ext = fileName.split("\\.")[1];
//        String contentType = "";
//
//        //content type??? ???????????? ???????????? ????????? ???????????? "application/octet-stream"?????? ????????? ?????? ?????? ????????? ????????? ???????????? ????????? ?????? ????????? ?????????.
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
//        //object ?????? ????????????
//        ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(bucket);
//        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();
//
//        for (S3ObjectSummary object: objectSummaries) {
//            System.out.println("object = " + object.toString());
//        }
//        return amazonS3.getUrl(bucket, key).toString();
//    }
//    //?????? ??????
//    public String deleteFile(String fileName){
//        System.out.println(fileName);
//        DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
//        amazonS3.deleteObject(request);
//        return fileName;
//    }
//}