package com.radolyn.anime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.radolyn.anime.ui.theme.AnimeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    TitleView(id = 1, name = "Qq", desc = "It's anime for IT workers", image = "https://avatars.githubusercontent.com/u/59579711?s=200&v=4")
                }
            }
        }
    }
}

@Composable
fun TitleView(id: Int, name: String, desc: String, image: String) {
    Row {
//        AsyncImage(model = image, contentDescription = null)
        SubcomposeAsyncImage(model = image, contentDescription = null, loading = { CircularProgressIndicator() })
//        Text(text = "Image")
        Column(modifier = Modifier.padding(start = 5.dp)) {
            Text(text = name)
            Text(text = desc)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TitlePreview() {
    TitleView(id = 1, name = "Qq", desc = "It's anime for IT workers", image = "https://avatars.githubusercontent.com/u/59579711?s=200&v=4")
}