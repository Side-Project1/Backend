package com.project.server.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    private String accessKey = "AKIA3BOR2OS45WZUWMXW";

    private String secretKey = "AKIA3BOR2OS45WZUWMXW";


    private String region = "ap-northeast-2";

    @Bean
        public AmazonS3 amazonS3Client() {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

            return AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(region)
                    .build();

    }

}