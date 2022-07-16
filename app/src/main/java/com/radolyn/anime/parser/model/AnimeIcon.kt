package com.radolyn.anime.parser.model

import java.net.URI
import java.nio.file.Paths

class AnimeIcon(url: String) {
    private val filename = Paths.get(URI(url).path).fileName.toString()

    val resolution120x120 = "https://animego.org/media/cache/thumbs_120x120/upload/anime/images/$filename"
    val resolution150x210 = "https://animego.org/media/cache/thumbs_150x210/upload/anime/images/$filename"
    val resolution300x420 = "https://animego.org/media/cache/thumbs_300x420/upload/anime/images/$filename"
    val resolution250x350 = "https://animego.org/media/cache/thumbs_250x350/upload/anime/images/$filename"
}