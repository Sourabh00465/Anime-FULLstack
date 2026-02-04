package com.bankai.hokage.service;

import com.bankai.hokage.dto.AniListResponse;
import com.bankai.hokage.dto.Media;
import com.bankai.hokage.model.Anime;
import com.bankai.hokage.model.AnimeType;
import com.bankai.hokage.repo.AnimeRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AniListService {

    private final WebClient webClient;
    private final AnimeRepo animeRepo;


    public AniListService(WebClient.Builder builder, AnimeRepo animeRepo){
        this.webClient = builder.baseUrl("https://graphql.anilist.co").build();
        this.animeRepo = animeRepo;
    }


    // Fetch AniList Top 10 Anime
    public List<Media> fetchTopAnimeAsDTO() {
        String query = """
            query {
              Page(page: 1, perPage: 10) {
                media(type: ANIME, sort: SCORE_DESC) {
                  id
                  title { romaji english }
                  averageScore
                  episodes
                  genres
                  description(asHtml: false)  \s
                  coverImage {
                    large
                  }
                }
              }
            }
           \s""";

        Map<String,Object> requestBody = Map.of("query", query);

        AniListResponse response = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(AniListResponse.class)
                .block();

        return response.getData().getPage().getMedia();
    }

    // Map DTO → Entity
    private Anime mapToAnimeEntity(Media mediaDto, AnimeType type) {
        return Anime.builder()
                .animeName(mediaDto.getTitle().getRomaji())
                .rating(mediaDto.getAverageScore() != null ? mediaDto.getAverageScore().floatValue() : null)
                .genre(mediaDto.getGenres() != null ? String.join(",", mediaDto.getGenres()) : null)
                .noOfEp(mediaDto.getEpisodes())
                .type(type)
                .imageUrl(mediaDto.getCoverImage().getLarge()) // ✅ poster image
                .description(mediaDto.getDescription())        // ✅ description mapped
                .build();
    }

    // Save Top 10 Anime to DB
    @Cacheable("topAnime")
    public void fetchAndSaveTopAnime() {
        List<Media> mediaList = fetchTopAnimeAsDTO();

        List<Anime> animeEntities = mediaList.stream()
                .map(media -> mapToAnimeEntity(media, AnimeType.TOP50))
                .collect(Collectors.toList());

        animeRepo.saveAll(animeEntities);
    }

    // Fetch Current Season Anime
    @Cacheable("currentSeasonAnime")
    public List<Media> fetchCurrentSeasonAnimeAsDTO() {
        String query = """
        query {
          Page(page: 1, perPage: 10) {
            media(type: ANIME, season: WINTER, seasonYear: 2026, sort: POPULARITY_DESC) {
              id
              title { romaji english }
              averageScore
              episodes
              genres
              description(asHtml: false)  \s
              coverImage {
                large
              }
            }
          }
        }
       \s""";

        Map<String,Object> requestBody = Map.of("query", query);

        AniListResponse response = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(AniListResponse.class)
                .block();

        return response.getData().getPage().getMedia();
    }

    // Save Current Season Anime to DB
    public void fetchAndSaveCurrentSeasonAnime() {
        List<Media> mediaList = fetchCurrentSeasonAnimeAsDTO();

        List<Anime> animeEntities = mediaList.stream()
                .map(media -> mapToAnimeEntity(media, AnimeType.CURRENT))
                .collect(Collectors.toList());

        animeRepo.saveAll(animeEntities);
    }
    @Scheduled(cron ="0 0 * * * Sun")
    @CacheEvict(value = "currentSeasonAnime", allEntries = true)
    public void refreshCurrentAnimeSeason(){
        fetchAndSaveCurrentSeasonAnime();
        System.out.println("Current season updated");
    }

    public List<Anime> getAllAnime() {
        return animeRepo.findAll();
    }
    public List<Anime> getTop50Anime() {
        return fetchTopAnimeAsDTO()
                .stream()
                .map(media -> mapToAnimeEntity(media, AnimeType.TOP50))
                .collect(Collectors.toList());
    }

    public List<Anime> getCurrentTop10Anime() {
        return fetchCurrentSeasonAnimeAsDTO()
                .stream()
                .map(media -> mapToAnimeEntity(media, AnimeType.CURRENT))
                .collect(Collectors.toList());
    }


}
