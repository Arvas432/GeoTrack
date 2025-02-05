package com.example.geotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geotrack.ui.model.Screens
import com.example.geotrack.ui.theme.GeoTrackTheme
import kotlinx.coroutines.launch

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Маршруты",
                icon = Icons.Filled.Home,
                route = Screens.Feed.route
            ),
            BottomNavigationItem(
                label = "Трекинг",
                icon = Icons.Filled.Search,
                route = Screens.Tracking.route
            ),
            BottomNavigationItem(
                label = "Профиль",
                icon = Icons.Filled.AccountCircle,
                route = Screens.Profile.route
            ),
            BottomNavigationItem(
                label = "Настройки",
                icon = Icons.Filled.Settings,
                route = Screens.Settings.route
            ),
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoTrackTheme {
                MyApp()
            }
        }
    }
}
@Preview
@Composable
fun MyApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHostContainer(navController, Modifier.padding(innerPadding))
    }
}

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier) {
    NavHost(navController, startDestination = "auth_route", modifier = modifier) {
        composable("auth_route") { LoginScreen(navController) }
        composable("settings_route") { SettingsScreen() }
        composable("tracking_route") { TrackingScreen() }
        composable("feed_route") { RoutesScreen() }
        composable("profile_route") { ProfileScreen() }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }

    NavigationBar {
        BottomNavigationItem().bottomNavigationItems().forEachIndexed { index, bottomNavigationItem ->
            NavigationBarItem(
                selected = index == navigationSelectedItem,
                label = {
                    Text(bottomNavigationItem.label)
                },
                icon = {
                    Icon(
                        bottomNavigationItem.icon,
                        contentDescription = bottomNavigationItem.label
                    )
                },
                onClick = {
                    navigationSelectedItem = index
                    navController.navigate(bottomNavigationItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    Button(onClick = { navController.navigate("routes") }) {
        Text("Войти")
    }
}

@Composable
fun SettingsScreen() {

}

@Composable
fun TrackingScreen() {
    Text("Экран трекинга")
}

@Composable
fun RoutesScreen() {
    Text("Экран маршрутов")
}

@Composable
fun ProfileScreen() {
    Text("Экран профиля")
}