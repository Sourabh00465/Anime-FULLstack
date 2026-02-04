package com.bankai.hokage.controller;

import com.bankai.hokage.model.Anime;
import com.bankai.hokage.model.AnimeType;
import com.bankai.hokage.repo.AnimeRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anime")
@CrossOrigin(origins = "http://localhost:3000")
public class AnimeController {

    private final AnimeRepo animeRepo;

    public AnimeController(AnimeRepo animeRepo) {
        this.animeRepo = animeRepo;
    }

    @GetMapping("/top50/db")
    public List<Anime> getTop50AnimefromDb(){
        return animeRepo.findByType(AnimeType.TOP50);
    }

    @GetMapping("/currenttop10/db")
    public List<Anime> getCurrentAnimefromDb(){
        return animeRepo.findByType(AnimeType.CURRENT);
    }

    @PostMapping
    public Anime addAnime(@RequestBody Anime anime){
        return animeRepo.save(anime);
    }
}
