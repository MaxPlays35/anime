package com.radolyn.anime.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AnimeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) {
        dynamicDarkColorScheme(LocalContext.current)
    } else {
        dynamicLightColorScheme(LocalContext.current)
    }

    val nunitoSans = FontFamily(
        Font(com.radolyn.anime.R.font.nunitosans_black),
        Font(com.radolyn.anime.R.font.nunitosans_blackitalic),
        Font(com.radolyn.anime.R.font.nunitosans_bold),
        Font(com.radolyn.anime.R.font.nunitosans_bolditalic),
        Font(com.radolyn.anime.R.font.nunitosans_extrabold),
        Font(com.radolyn.anime.R.font.nunitosans_extrabolditalic),
        Font(com.radolyn.anime.R.font.nunitosans_extralight),
        Font(com.radolyn.anime.R.font.nunitosans_extralightitalic),
        Font(com.radolyn.anime.R.font.nunitosans_italic),
        Font(com.radolyn.anime.R.font.nunitosans_light),
        Font(com.radolyn.anime.R.font.nunitosans_lightitalic),
        Font(com.radolyn.anime.R.font.nunitosans_regular),
        Font(com.radolyn.anime.R.font.nunitosans_semibold),
        Font(com.radolyn.anime.R.font.nunitosans_semibolditalic)
    )

    val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = nunitoSans,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = nunitoSans,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp
        ),
        bodySmall = TextStyle(
            fontFamily = nunitoSans,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        ),
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}