package com.example.geotrack

import android.os.Bundle
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geotrack.ui.authorization.LoginScreen
import com.example.geotrack.ui.model.Screens
import com.example.geotrack.ui.theme.GeoTrackTheme
import com.example.geotrack.ui.common_ui_components.BottomNavigationBar
import com.example.geotrack.ui.common_ui_components.MapPin
import com.example.geotrack.ui.common_ui_components.Settings
import com.example.geotrack.ui.common_ui_components.SocialIcon
import com.example.geotrack.ui.social.RoutesScreen
import com.example.geotrack.ui.tracking.TrackingScreen
import com.example.geotrack.ui.user_profile.ProfileScreen
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Маршруты",
                icon = SocialIcon,
                route = Screens.Feed.route
            ),
            BottomNavigationItem(
                label = "Трекинг",
                icon = MapPin,
                route = Screens.Tracking.route
            ),
            BottomNavigationItem(
                label = "Профиль",
                icon = Icons.Filled.AccountCircle,
                route = Screens.Profile.route
            ),
            BottomNavigationItem(
                label = "Настройки",
                icon = Settings,
                route = Screens.Settings.route
            ),
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Configuration.getInstance().load(applicationContext, getDefaultSharedPreferences(applicationContext));
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID;
        setContent {
            GeoTrackTheme {
                MyApp()
            }
        }
    }
}

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
        composable("auth_route") { LoginScreen() }
        composable("settings_route") { SettingsScreen() }
        composable("tracking_route") { TrackingScreen() }
        composable("feed_route") { RoutesScreen() }
        composable("profile_route") { ProfileScreen() }
    }
}



@Composable
fun SettingsScreen() {

}




