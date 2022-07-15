package com.radolyn.anime.titleView

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage

@Composable
fun TitlePreview(id: Int, name: String, desc: String, imageUrl: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        SubcomposeAsyncImage(model = imageUrl, contentDescription = "", modifier =  Modifier.padding(10.dp).clip(
            RoundedCornerShape(10)
        ), loading = {
            CircularProgressIndicator()
        })
        Column(modifier = Modifier.padding(5.dp)) {
            Text(
                text = name,
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(text = desc, fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
        }
    }
}