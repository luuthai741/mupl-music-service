package com.mupl.music_service;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MusicServiceApplication implements CommandLineRunner {

	@Autowired
	private ConnectionFactory connectionFactory;
	@Value("${spring.r2dbc.url}")
	private String dbUrl;
	public static void main(String[] args) {
		SpringApplication.run(MusicServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Connected to R2dbc server {}", connectionFactory.getMetadata());
		log.info("Connected to R2dbc database {}", dbUrl);
	}
}
