package com.radolyn.anime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
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
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

sealed class Screen(val route: String, val name: String, val icon: Int) {
    object NewAnimeScreen : Screen("newAnime", "New Anime", R.drawable.added)
    object UpdatedAnimeScreen : Screen("updatedAnime", "Updated Anime", R.drawable.updated)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.NewAnimeScreen,
        Screen.UpdatedAnimeScreen
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val currentRoute = currentDestination?.route

                items.forEach { screen ->
                    NavigationBarItem (
                        icon = { Icon(ImageVector.vectorResource(id = screen.icon), contentDescription = screen.name, modifier = Modifier.size(32.dp)) },
                        label = { Text(text = screen.name) },
                        selected = currentRoute == screen.route,
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
fun MainTitlePreview() {
//    TitlePreview(id = 1, name = "Власть книжного червя 3", desc = "ТВ Сериал / 2022", "https://i.stack.imgur.com/2fbPs.png")
}