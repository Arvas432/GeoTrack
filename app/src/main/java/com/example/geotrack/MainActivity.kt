package com.example.geotrack

import android.os.Bundle
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.MaterialTheme
import com.example.geotrack.domain.auth.AuthInteractor
import com.example.geotrack.domain.auth.AuthRepository
import com.example.geotrack.domain.auth.TokenStorage
import com.example.geotrack.domain.profile.UserProfileInteractor
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val authInteractor: AuthInteractor by inject()
    private val userProfileInteractor: UserProfileInteractor by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Configuration.getInstance()
            .load(applicationContext, getDefaultSharedPreferences(applicationContext));
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID;
        val showAuth = tokenStorage.getToken() == null
        setContent {
            GeoTrackTheme {
                AppRoot(tokenStorage, authInteractor, userProfileInteractor)
            }
        }
    }
}

@Composable
fun AppRoot(
    tokenStorage: TokenStorage,
    authInteractor: AuthInteractor,
    userProfileInteractor: UserProfileInteractor
) {
    val navController = rememberNavController()
    val startDestination = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val token = tokenStorage.getToken()
        val isTokenValid = token?.let {
            runCatching { authInteractor.checkToken() }.getOrDefault(false)
        } ?: false

        if (!isTokenValid) {
            startDestination.value = "auth_route"
        } else {
            val user = userProfileInteractor.getProfile()
            startDestination.value =
                if (user == null) "profile_creation_route" else "tracking_route"
        }
    }

    if (startDestination.value == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        MyApp(startDestination.value!!, navController)
    }
}

@Composable
fun MyApp(startDestination: String, navController: NavHostController) {
    val bottomBarHiddenRoutes = listOf(
        "auth_route",
        "profile_creation_route"
    )
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
            navController, startDestination, Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colors.primary)
        )
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier
) {
    NavHost(navController, startDestination = startDestination, modifier = modifier) {
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




