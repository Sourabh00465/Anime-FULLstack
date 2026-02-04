package com.bankai.hokage;

import com.bankai.hokage.repo.AnimeRepo;
import com.bankai.hokage.service.AniListService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class HokageApplication {

	public static void main(String[] args) {
		SpringApplication.run(HokageApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AniListService aniListService, AnimeRepo animeRepo) {
		return args -> {
			animeRepo.deleteAll();
			// fetch fresh data
			aniListService.fetchAndSaveTopAnime();
			aniListService.fetchAndSaveCurrentSeasonAnime();
		};
	}
}
