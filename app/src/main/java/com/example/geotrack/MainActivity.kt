package com.example.geotrack

import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import androidx.wear.compose.material.ripple
import com.example.geotrack.ui.PlaceholderTransformation
import com.example.geotrack.ui.model.Screens
import com.example.geotrack.ui.robotoFamily
import com.example.geotrack.ui.theme.Black22
import com.example.geotrack.ui.theme.BlueE7
import com.example.geotrack.ui.theme.GeoTrackTheme
import com.example.geotrack.ui.theme.GrayB4
import com.example.geotrack.ui.theme.GrayEB
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
fun BottomNavigationBar(navController: NavHostController) {
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }

    NavigationBar {
        BottomNavigationItem().bottomNavigationItems()
            .forEachIndexed { index, bottomNavigationItem ->
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
fun LoginScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp, 0.dp)
    ) {
        ScreenHeader("Вход")
        HintedTextField("", "example@gmail.com", "Логин", {})
        Spacer(modifier = Modifier.height(17.dp))
        HintedTextField("", "Введите пароль", "Пароль", {})
        Spacer(modifier = Modifier.height(88.dp))
        DefaultButton(text = "Войти") { }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp)
        ) {
            Text(
                "Нету аккаунта? ",
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Normal,
                color = GrayB4,
                fontSize = 12.sp
            )
            Text(
                "Зарегистрироваться ",
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp,
                modifier = Modifier.clickable {

                }
            )

        }
    }
}

@Composable
fun ScreenHeader(headerText: String) {
    Text(
        text = headerText,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 22.sp,
        modifier = Modifier.padding(0.dp, 19.dp)
    )
}

@Composable
fun HintedTextField(
    text: String,
    hintText: String,
    topHint: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val localTextColor = if (text.isEmpty())
        MaterialTheme.colorScheme.onPrimary.copy(ContentAlpha.medium)
    else
        LocalContentColor.current.copy(LocalContentAlpha.current)

    val textStyle = if (text.isEmpty())
        LocalTextStyle.current.merge(
            color = Black22,
            fontFamily = robotoFamily,
            fontWeight = FontWeight.Normal
        )
    else
        LocalTextStyle.current

    OutlinedTextField(
        value = text,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = BlueE7,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            focusedLabelColor = BlueE7,
            unfocusedLabelColor = GrayB4,
            unfocusedTextColor = localTextColor,
        ),
        textStyle = textStyle,
        visualTransformation = if (text.isEmpty())
            PlaceholderTransformation(hintText)
        else VisualTransformation.None,
        shape = RoundedCornerShape(12.dp),
        label = {
            Text(
                topHint,
                fontFamily = robotoFamily,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .offset(y = (0).dp)
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onValueChange = onTextChanged,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
            .height(65.dp)
    )
}

@Composable
fun DefaultButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = GrayB4
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp), onClick = onClick
    ) {
        Text(
            text,
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = robotoFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
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