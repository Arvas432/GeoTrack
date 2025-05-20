package com.example.geotrack

import android.os.Bundle
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.MaterialTheme
import com.example.geotrack.domain.auth.TokenStorage
import com.example.geotrack.ui.authorization.LoginScreen
import com.example.geotrack.ui.model.Screens
import com.example.geotrack.ui.theme.GeoTrackTheme
import com.example.geotrack.ui.common_ui_components.BottomNavigationBar
import com.example.geotrack.ui.common_ui_components.MapPin
import com.example.geotrack.ui.common_ui_components.Settings
import com.example.geotrack.ui.common_ui_components.SocialIcon
import com.example.geotrack.ui.profile_creation.ProfileCreation
import com.example.geotrack.ui.social.RoutesScreen
import com.example.geotrack.ui.tracking.TrackingScreen
import com.example.geotrack.ui.user_profile.ProfileScreen
import org.koin.android.ext.android.inject
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
    private val tokenStorage: TokenStorage by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Configuration.getInstance()
            .load(applicationContext, getDefaultSharedPreferences(applicationContext));
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID;
        val showAuth = tokenStorage.getToken() == null
        setContent {
            GeoTrackTheme {
                MyApp(showAuth)
            }
        }
    }
}

@Composable
fun MyApp(showAuth: Boolean) {
    val bottomBarHiddenRoutes = listOf(
        "auth_route",
        "profile_creation_route"
    )

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.primary),
        bottomBar = {
            if (currentRoute !in bottomBarHiddenRoutes) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHostContainer(
            navController,
            showAuth,
            Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colors.primary)
        )
    }
}

@Composable
fun NavHostContainer(navController: NavHostController, showAuth: Boolean, modifier: Modifier) {
    NavHost(navController, startDestination = if (showAuth) "auth_route" else "tracking_route", modifier = modifier) {
        composable("auth_route") { LoginScreen(navController = navController) }
        composable("settings_route") { SettingsScreen() }
        composable("tracking_route") { TrackingScreen() }
        composable("feed_route") { RoutesScreen() }
        composable("profile_route") { ProfileScreen() }
        composable("profile_creation_route") { ProfileCreation(navController = navController) }
    }
}


@Composable
fun SettingsScreen() {

}




