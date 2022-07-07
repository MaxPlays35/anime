package com.radolyn.anime.parser

import okhttp3.OkHttpClient
import okhttp3.Request

abstract class AnimeGoParser {
    protected abstract val httpClient: OkHttpClient
    protected val url: String = "https://animego.org/"

    suspend fun getRecentlyReleasedList() {
        val request = Request.Builder()
            .get()
            .url(url)

        return httpClient.newCall(request.build()).aw
    }
}