package com.radolyn.anime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.radolyn.anime.components.newAnimeView.NewAnimeView
import com.radolyn.anime.components.titleView.TitleView
import com.radolyn.anime.components.updatedAnimeView.UpdatedAnimeView
import com.radolyn.anime.parser.AnimePageParser
import com.radolyn.anime.parser.MainPageParser
import com.radolyn.anime.ui.theme.AnimeTheme
import kotlinx.coroutines.flow.flow
import java.net.URLDecoder

class MainViewModel : ViewModel() {
    val parser = MainPageParser()
    val animeParser = AnimePageParser()

    var animes = flow {
        parser.getMainPageDetails().apply {
            emit(this)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

sealed class Screen(val route: String, val name: String, val icon: ImageVector) {
    object NewAnimeScreen : Screen("newAnime", "New Anime", Icons.Filled.Refresh)
    object UpdatedAnimeScreen : Screen("updatedAnime", "Updated Anime", Icons.Filled.ThumbUp)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.NewAnimeScreen,
        Screen.UpdatedAnimeScreen
    )

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = "") },
                        label = { screen.name },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.NewAnimeScreen.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.NewAnimeScreen.route) { NewAnimeView(navController) }
            composable(Screen.UpdatedAnimeScreen.route) { UpdatedAnimeView(navController) }
            composable(
                "viewAnime/{url}",
                arguments = listOf(navArgument("url") { type = NavType.StringType })
            ) {
                val animeUrl = it.arguments?.getString("url")
                if (animeUrl != null) {
                    TitleView(animeUrl = URLDecoder.decode(animeUrl))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TitlePreview() {
//    TitleView(id = 1, name = "Власть книжного червя 3", desc = "ТВ Сериал / 2022", 10)
}