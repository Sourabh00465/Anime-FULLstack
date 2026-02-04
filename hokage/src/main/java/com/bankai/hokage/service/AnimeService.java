package com.bankai.hokage.service;

import com.bankai.hokage.model.Anime;
import com.bankai.hokage.model.AnimeType;
import com.bankai.hokage.repo.AnimeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService {

    private final AnimeRepo animeRepo;

    public AnimeService(AnimeRepo animeRepo) {
        this.animeRepo = animeRepo;
    }

    public List<Anime> getTop50AnimeDb(){
        return animeRepo.findByType(AnimeType.TOP50);
    }
    public List<Anime> getCurrentAnime() {
        return animeRepo.findByType(AnimeType.CURRENT);
    }

    public Anime saveAnime(Anime anime){
         return animeRepo.save(anime);
    }
}
