package com.radolyn.anime.components.titleView

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radolyn.anime.MainViewModel
import com.radolyn.anime.parser.model.Anime
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TitleView(animeUrl: String, model: MainViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    var title by remember { mutableStateOf<Anime?>(null) }
    coroutineScope.launch {
        title = model.animeParser.getAnimeDetails(animeUrl)
    }

    if (title != null) {
        Column {
//            SubcomposeAsyncImage(model = title., contentDescription = )
            Text(text = title!!.name)
            Text(text = title!!.description)
            Text(text = title!!.season)
            Text(text = title!!.studio)
            Text(text = title!!.publishingFrom)
        }
    } else {
        CircularProgressIndicator()
    }

}