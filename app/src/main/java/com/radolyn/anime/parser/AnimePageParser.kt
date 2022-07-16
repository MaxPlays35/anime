package com.radolyn.anime.parser

import com.radolyn.anime.parser.model.*
import org.jsoup.nodes.Document
import parser.getRequest

class AnimePageParser {
    suspend fun getAnimeDetails(url: String): Anime {
        val doc = getRequest(url)

        return parseAnimePage(doc)
    }

    private fun parseAnimePage(doc: Document): Anime {
        val name = doc.select("h1").text()
        val alternativeNames =
            doc.selectFirst("#content > div > div.media.mb-3.d-none.d-block.d-md-flex > div.media-body > div.anime-title > div > div > div.anime-synonyms > ul")!!
                .children()
                .map {
                    it.text()
                }

        val announcements: MutableList<String> = mutableListOf()
        var type: AnimeType = AnimeType.ERROR
        var publishedSeriesCount: Int = -1
        var totalSeriesCount: Int = -1
        var status: AnimeStatus = AnimeStatus.ERROR
        var genres: List<String> = listOf()
        var source: AnimeSource = AnimeSource.ERROR
        var season: String = ""
        var publishingFrom: String = ""
        var studio: String = ""
        var mpaa: AnimeMPAA = AnimeMPAA.ERROR
        var ageRestriction: Int = -1
        var duration: Int? = null
        var voiceovers: List<String>? = null
        var producer: String? = null
        var originalAuthor: String? = null
        var sourceDetails: String? = null
        var mainHeroes: List<String>? = null

        val props =
            doc.selectFirst("#content > div > div.media.mb-3.d-none.d-block.d-md-flex > div.media-body > div.anime-info > dl")!!
        props.forEach {
            if (it.tag().name != "dt") {
                return@forEach
            }

            val key = it.text()
            val value = it.nextElementSibling()!!.text()
            when (key) {
                "Следующий эпизод" -> announcements.add(value)
                "Тип" -> type = parseType(value)
                "Эпизоды" -> {
                    val (published, total) = parseSeries(value)
                    publishedSeriesCount = published
                    totalSeriesCount = total
                }
                "Статус" -> status = parseStatus(value)
                "Жанр" -> genres = value.split(", ")
                "Первоисточник" -> source = parseSource(value)
                "Сезон" -> season = value
                "Выпуск" -> publishingFrom = value
                "Студия" -> studio = value
                "Рейтинг MPAA" -> mpaa = parseMPAA(value)
                "Возрастные ограничения" -> ageRestriction = parseAgeRestriction(value)
                "Длительность" -> duration = parseDuration(value)
                "Озвучка" -> voiceovers = value.split(", ")
                "Режиссёр" -> producer = value
                "Автор оригинала" -> originalAuthor = value
                "Главные герои" -> mainHeroes = value.split(", ") // todo: fix
                else -> {
                    if (key.contains("Снят")) {
                        sourceDetails = value
                    } else {
                        throw IllegalArgumentException("Unknown key: $key")
                    }
                }
            }
        }

        val rating = doc
            .selectFirst("#itemRatingBlock > div.itemRatingBlock.d-inline-flex.align-items-center.mb-1.position-relative > div.pr-2 > div:nth-child(1) > span.rating-value")
            ?.text()
            ?.replace(",", ".")
            ?.toDoubleOrNull()
        val description = doc
            .selectFirst("#content > div > div.description")
            ?.text() ?: ""
        val icon = doc
            .selectXpath("//*[@id=\"content\"]/div/div[1]/div[1]/div[1]/div[2]/img")[0]
            ?.attr("src") ?: ""

        return Anime(
            icon = AnimeIcon(icon),
            name = name,
            alternativeNames = alternativeNames,
            type = type,
            publishedSeriesCount = publishedSeriesCount,
            totalSeriesCount = totalSeriesCount,
            status = status,
            genres = genres,
            source = source,
            season = season,
            publishingFrom = publishingFrom,
            studio = studio,
            mpaa = mpaa,
            ageRestriction = ageRestriction,
            duration = duration,
            voiceovers = voiceovers,
            producer = producer,
            originalAuthor = originalAuthor,
            rating = rating,
            sourceDetails = sourceDetails,
            mainHeroes = mainHeroes,
            announcements = announcements,
            description = description
        )
    }

    private fun parseType(type: String): AnimeType {
        return when (type) {
            "ТВ Сериал" -> AnimeType.TV
            "Фильм" -> AnimeType.FILM
            "Спешл" -> AnimeType.SPECIAL
            "OVA" -> AnimeType.OVA
            "ONA" -> AnimeType.ONA
            else -> {
                throw IllegalArgumentException("Unknown type: $type")
            }
        }
    }

    private fun parseSeries(series: String): Pair<Int, Int> {
        var publishedSeriesCount = 0
        var totalSeriesCount = 0

        val parts = series.split(" ").map {
            if (it != "?" && it != "/") {
                it.toInt()
            } else {
                -1
            }
        }
        publishedSeriesCount = parts[0]
        if (parts.count() == 2) {
            totalSeriesCount = parts[1]
        } else {
            totalSeriesCount = -1
        }

        return Pair(publishedSeriesCount, totalSeriesCount)
    }

    private fun parseStatus(status: String): AnimeStatus {
        return when (status) {
            "Онгоинг" -> AnimeStatus.ONGOING
            "Вышел" -> AnimeStatus.FINISHED
            "Анонс" -> AnimeStatus.ANNOUNCED
            else -> {
                throw IllegalArgumentException("Unknown status: $status")
            }
        }
    }

    private fun parseSource(source: String): AnimeSource {
        return when (source) {
            "Оригинал" -> AnimeSource.ORIGINAL
            "Новелла" -> AnimeSource.NOVEL
            "Новвела" -> AnimeSource.NOVEL
            "Легкая новвела" -> AnimeSource.LIGHT_NOVEL
            "Легкая новелла" -> AnimeSource.LIGHT_NOVEL
            "Визуальная новелла" -> AnimeSource.VISUAL_NOVEL
            "Визуальная новвела" -> AnimeSource.VISUAL_NOVEL
            "Манга" -> AnimeSource.MANGA
            "4-koma Manga" -> AnimeSource.MANGA
            "Веб-манга" -> AnimeSource.WEB_MANGA
            "Игра" -> AnimeSource.GAME
            "Музыка" -> AnimeSource.MUSIC
            "Другие" -> AnimeSource.OTHER
            else -> {
                throw IllegalArgumentException("Unknown source: $source")
            }
        }
    }

    private fun parseMPAA(mpaa: String): AnimeMPAA {
        val normalized = mpaa.uppercase().replace("-", "_")
        return AnimeMPAA.valueOf(normalized)
    }

    private fun parseDuration(duration: String): Int {
        return duration.filter { it.isDigit() }.toInt()
    }

    private fun parseAgeRestriction(ageRestriction: String): Int {
        return ageRestriction.filter { it.isDigit() }.toInt()
    }
}
