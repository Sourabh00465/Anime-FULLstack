package com.bankai.hokage.repo;

import com.bankai.hokage.model.Anime;
import com.bankai.hokage.model.AnimeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  AnimeRepo extends JpaRepository<Anime,Long> {
        List<Anime> findByType(AnimeType type);
}
