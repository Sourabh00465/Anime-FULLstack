package com.bankai.hokage.controller;

import com.bankai.hokage.model.Anime;
import com.bankai.hokage.model.AnimeType;
import com.bankai.hokage.service.AniListService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AniListController {

    private final AniListService aniListService;

    public AniListController(AniListService aniListService){
        this.aniListService = aniListService;
    }

    @GetMapping("/anime/anilist/top10")
    public String getTopAnimeFromList(){
        aniListService.fetchAndSaveTopAnime();
        return "Top 10 anime saved to DB!";
    }

    @GetMapping("/anime/anilist/saveCurrentTop10")
    public String saveCurrentSeasonAnimeToDb() {
        aniListService.fetchAndSaveCurrentSeasonAnime();
        return "Current season top 10 anime saved to DB!";
    }

    @GetMapping("/anime")
    public List<Anime> getAllAnime() {
        return aniListService.getAllAnime();
    }

    @GetMapping("/anime/top50")
    public List<Anime> getTop50Anime() {
        return aniListService.getTop50Anime(); // service returns List<Anime>
    }

    @GetMapping("/anime/currenttop10")
    public List<Anime> getCurrentTop10Anime() {
        return aniListService.getCurrentTop10Anime(); // service returns List<Anime>
    }
}

