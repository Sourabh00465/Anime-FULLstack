package com.bankai.hokage.dto;

import lombok.Data;
import java.util.List;

@Data
public class Media {
    private Integer id;
    private Title title;
    private Integer averageScore;
    private Integer episodes;
    private List<String> genres;
    private CoverImage coverImage;// nested object
    private String description;

    @Data
    public static class CoverImage {
        private String large;        // AniList gives "large" and "extraLarge"
        private String extraLarge;   // optional, higher resolution
    }
}
