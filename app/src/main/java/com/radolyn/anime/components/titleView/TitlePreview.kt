package com.radolyn.anime.components.titleView

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.radolyn.anime.parser.model.AnimeIcon

@Composable
fun TitlePreview(
    id: Int,
    name: String,
    desc: String,
    icon: AnimeIcon,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.height(100.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(icon.resolution120x120)
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(100))
                .aspectRatio(1f),
            loading = {
                CircularProgressIndicator()
            },
        )
        Column(
            modifier = Modifier
                .padding(6.dp),
        ) {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = desc,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .align(Alignment.BottomStart)
                )
            }
        }
    }
}