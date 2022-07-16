package com.radolyn.anime.parser.model

data class NewAnime(
    val icon: AnimeIcon,
    val name: String,
    val description: String,
    val seriesCount: Int,
    val url: String
)
