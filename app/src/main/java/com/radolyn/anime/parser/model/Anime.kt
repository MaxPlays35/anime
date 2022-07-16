package com.radolyn.anime.parser.model

enum class AnimeType {
    TV,
    FILM,
    OVA,
    ONA,
    SPECIAL,
    ERROR
}

enum class AnimeStatus {
    ONGOING,
    FINISHED,
    ANNOUNCED,
    ERROR
}

enum class AnimeSource {
    ORIGINAL,
    NOVEL,
    LIGHT_NOVEL,
    VISUAL_NOVEL,
    MANGA,
    WEB_MANGA,
    GAME,
    MUSIC,
    OTHER,
    ERROR
}

enum class AnimeMPAA {
    G,
    PG,
    PG_13,
    R,
    NC_17,
    UNRATED,
    ERROR
}

data class Anime(
    val icon: AnimeIcon,
    val name: String,
    val alternativeNames: List<String>,
    val type: AnimeType,
    val publishedSeriesCount: Int,
    val totalSeriesCount: Int?,
    val status: AnimeStatus,
    val genres: List<String>,
    val source: AnimeSource,
    val season: String, // todo: season as class ?
    val publishingFrom: String,
    val studio: String,
    val mpaa: AnimeMPAA,
    val ageRestriction: Int,
    val duration: Int?,
    val voiceovers: List<String>?,
    val producer: String?,
    val originalAuthor: String?,
    val rating: Double?,
    val sourceDetails: String?,
    val mainHeroes: List<String>?,
    val announcements: List<String>,
    val description: String,
)
