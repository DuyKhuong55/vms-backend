package com.facenet.cms;

import com.facenet.cms.model.CameraModel;
import com.facenet.cms.model.NVRModel;
import com.facenet.cms.repository.CameraRepository;
import com.facenet.cms.repository.NVRRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@Slf4j
public class CmsApplication implements CommandLineRunner {

	@Autowired
	private NVRRepository nvrRepository;

	@Autowired
	private CameraRepository cameraRepository;

	public static void main(String[] args) {
		SpringApplication.run(CmsApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/*").allowedOrigins("http://localhost:4205");
			}
		};
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
