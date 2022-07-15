package com.radolyn.anime.newAnimeView

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.radolyn.anime.MainViewModel
import com.radolyn.anime.titleView.TitlePreview
import java.net.URLEncoder

@Composable
fun NewAnimeView(navController: NavController, model: MainViewModel = viewModel()){
    val anime by model.animes.collectAsState(initial = Pair(emptyList(), emptyList()))

    LazyColumn() {
        items(anime.second!!) { item ->
            TitlePreview(id = item.hashCode(), name = item.name, desc = item.description, imageUrl = item.icon, Modifier.selectable(true, onClick = { navController.navigate("viewAnime/${URLEncoder.encode(item.url)}") }))
        }
    }
}
