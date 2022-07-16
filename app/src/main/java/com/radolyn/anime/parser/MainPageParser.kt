package com.radolyn.anime.parser

import com.radolyn.anime.parser.model.AnimeIcon
import com.radolyn.anime.parser.model.NewAnime
import com.radolyn.anime.parser.model.UpdatedAnime
import org.jsoup.nodes.Document
import parser.baseUrl
import parser.getRequest

class MainPageParser {
    suspend fun getMainPageDetails(): Pair<List<UpdatedAnime>?, List<NewAnime>?> {
        val doc = getRequest(baseUrl)

        val updatedAnimeList = parseUpdatedAnimeList(doc)
        val newAnimeList = parseNewAnimeList(doc)

        return Pair(updatedAnimeList, newAnimeList)
    }

    private fun parseNewAnimeList(doc: Document): List<NewAnime>? {
        val newAnimeNode =
            doc.selectFirst("#wrap > div:nth-child(5) > div > div > div.col-12.col-md-12.col-lg-4.d-none.d-xl-block.content-block > div")

        val res = newAnimeNode?.children()?.filter { element ->
            !element.hasClass("card-header")
        }?.map {
            val icon = it.selectFirst("div > div.media-left.mr-3 > div")!!.attr("data-original")

            val nameNode = it.selectFirst("div > div.media-body > div.h6.font-weight-normal > a")!!
            val name = nameNode.text() ?: ""
            val url = nameNode.attr("href") ?: ""

            val desc = it.selectFirst("div > div.media-body > div:nth-child(2)")!!.text()

            val series = it.selectFirst("div > div.media-body > div:nth-child(3)")!!.text()
            val seriesCount = series.split(" ")[0].toInt()

            NewAnime(AnimeIcon(icon), name, desc, seriesCount, baseUrl + url)
        }

        return res
    }

    private fun parseUpdatedAnimeList(doc: Document): List<UpdatedAnime>? {
        val updatedNode = doc.selectFirst("#slide-toggle-1")

        val res = updatedNode?.children()?.map {
            val icon =
                it.selectFirst("div > div.media-left.last-update-img.mr-2 > div")!!
                    .attr("style")
                    .replace("background-image: url(", "")
                    .replace(");", "")
            val name =
                it.select("div > div.media-body > div > div.d-flex.mr-auto > span > span")
                    .text()
            val series =
                it.select("div > div.media-body > div > div.ml-3.text-right > div.font-weight-600.text-truncate")
                    .text()
            val type =
                it.select("div > div.media-body > div > div.ml-3.text-right > div.text-gray-dark-6")
                    .text()
            val url = it.attr("onclick")
                    .replace("location.href='", "")
                    .replace("'", "")

            UpdatedAnime(AnimeIcon(icon), name, "$series $type", baseUrl + url)
        }

        return res
    }
}
