package com.bankai.hokage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anime {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "anime_name")
        private String animeName;

        private Float rating;

        private String genre;

        @Column(name = "no_of_ep")
        private Integer noOfEp;

        @Enumerated(EnumType.STRING)
        private AnimeType type;


        @Column(length = 512)
        private String imageUrl;

        @Column(length = 5000) // AniList descriptions can be long
        private String description;


}

