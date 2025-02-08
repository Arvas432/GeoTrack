package com.example.geotrack.ui.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geotrack.ui.robotoFamily
import com.example.geotrack.ui.theme.GrayB4
import com.example.geotrack.ui.common_ui_components.DefaultButton
import com.example.geotrack.ui.common_ui_components.HintedTextField
import com.example.geotrack.ui.common_ui_components.ScreenHeader

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