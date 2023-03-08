package com.project.server.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private String bucket = "artproject-s3-bucket";

    private final AmazonS3Client amazonS3Client;

    //단일 파일 업로드 (프로필 사진)
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        //파일 형식 구하기
        String ext = fileName.split("\\.")[1];
        String contentType = "";

        //content type을 지정해서 올려주지 않으면 자동으로 "application/octet-stream"으로 고정이 되서 링크 클릭시 웹에서 열리는게 아니라 자동 다운이 시작됨.
        switch (ext) {
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "csv":
                contentType = "text/csv";
                break;
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return fileName;

    }

    //다중 파일 업로드
    public List<String> uploadFiles(List<MultipartFile> multipartFile) throws IOException {
        List<String> filenameList = new ArrayList<>();
        multipartFile.forEach(files -> {
            String fileName = UUID.randomUUID() + "-" + files.getOriginalFilename();
            //파일 형식 구하기
            String ext = fileName.split("\\.")[1];

            String contentType = "";
            //content type을 지정해서 올려주지 않으면 자동으로 "application/octet-stream"으로 고정이 되서 링크 클릭시 웹에서 열리는게 아니라 자동 다운이 시작됨.
            switch (ext) {
                case "jpeg":
                    contentType = "image/jpeg";
                    break;
                case "png":
                    contentType = "image/png";
                    break;
                case "txt":
                    contentType = "text/plain";
                    break;
                case "csv":
                    contentType = "text/csv";
                    break;
            }

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            try (InputStream inputStream = files.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
            filenameList.add(fileName);
        });
        return filenameList;
    }


    //단일 파일 삭제
    public String deleteFile(String fileName) {
        String result = "success";

        String name = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.length());
        log.info(name);

        try {
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, name);
            System.out.println(isObjectExist);

            if (isObjectExist) {
                amazonS3Client.deleteObject(bucket, name);
                log.info(name);
                log.info("단일 파일 삭제 성공");
            } else {
                result = "file not found";
            }
        } catch (Exception e) {
            log.debug("Delete File failed", e);
        }

        return result;
    }

    //다중 파일 삭제
    public void deleteFiles(List<String> filename) {
       // DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, filename);
        filename.forEach(files -> {
            // String result = "success";
            String keys = files.substring(files.lastIndexOf('/') + 1, files.length());
           // DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket,keys);
            System.out.println("삭제 여부 확인" + keys);
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket,keys);
            System.out.println(isObjectExist);
            if (isObjectExist) {
               amazonS3Client.deleteObject(new DeleteObjectRequest(bucket,keys));
                log.info("삭제 성공");
            } else {
                log.info("file not found");
            }
        });
    }


    public ResponseEntity<byte[]> getObject(String storedFileName) throws IOException {
        S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, storedFileName));
        S3ObjectInputStream objectInputStream = ((S3Object) o).getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);

    }


}