package com.project.server;

import com.project.server.config.AppProperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
@Slf4j
public class ServerApplication {




	public static void main(String[] args) {


		SpringApplication.run(ServerApplication.class, args);

	}


//	@Bean
//	public CommandLineRunner initData(UserRepository userRepository, StudyRepository studyRepository) {
//		return args ->
//				IntStream.rangeClosed(1, 154).forEach(i -> {
//					User user =  User.builder()
//							.userName("tester" + i*20)
//							.email("testerer"+i*20+"com")
//							.provider(AuthProvider.local)
//							.role(Role.USER)
//							.build();
//
//					userRepository.save(user);
//
//					Study study = Study.builder()
//							.title("test" + i)
//							.author(user.getUserName())
//							.build();
//
//					studyRepository.save(study);
//				});
//	}
}
