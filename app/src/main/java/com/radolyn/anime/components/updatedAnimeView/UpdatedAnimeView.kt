package com.radolyn.anime.components.updatedAnimeView

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.radolyn.anime.MainViewModel
import com.radolyn.anime.components.titleView.TitlePreview

@Composable
fun UpdatedAnimeView(navController: NavController, model: MainViewModel = viewModel()) {
    val anime by model.animes.collectAsState(initial = Pair(emptyList(), emptyList()))

    LazyColumn {
        items(anime.first!!) { item ->
            TitlePreview(
                id = item.hashCode(),
                name = item.name,
                desc = item.description,
                imageUrl = item.icon
            )
        }
    }
}