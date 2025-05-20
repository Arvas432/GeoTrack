package com.example.geotrack.ui.authorization

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.geotrack.R
import com.example.geotrack.ui.authorization.state.AuthEffect
import com.example.geotrack.ui.authorization.state.AuthEvent
import com.example.geotrack.ui.robotoFamily
import com.example.geotrack.ui.theme.GrayB4
import com.example.geotrack.ui.common_ui_components.DefaultButton
import com.example.geotrack.ui.common_ui_components.HintedTextField
import com.example.geotrack.ui.common_ui_components.ScreenHeader
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel = koinViewModel(), navController: NavController) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AuthEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }

                is AuthEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is AuthEffect.NavigateToMainScreen -> {
                    navController.navigate("profile_creation_route") {
                        popUpTo("auth_route") { inclusive = true }
                    }
                }
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp, 0.dp)
    ) {
        ScreenHeader(if (!state.isRegister) stringResource(R.string.login) else stringResource(R.string.registration))
        HintedTextField(username,
            stringResource(R.string.example_gmail_com),
            stringResource(R.string.username), { username = it })
        Spacer(modifier = Modifier.height(17.dp))
        HintedTextField(password, stringResource(R.string.enter_password),
            stringResource(R.string.password), { password = it })
        Spacer(modifier = Modifier.height(88.dp))
        DefaultButton(
            text = if (state.isLoading) stringResource(R.string.loading_) else if (state.isRegister) stringResource(
                R.string.register
            ) else stringResource(
                R.string.log_in
            )
        ) {
            if (!state.isLoading) {
                if (state.isRegister) viewModel.onEvent(
                    AuthEvent.Register(
                        username,
                        password
                    )
                ) else viewModel.onEvent(AuthEvent.Login(username, password))
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp)
        ) {
            Text(
                if (!state.isRegister) stringResource(R.string.already_have_an_account) else stringResource(R.string.no_account),
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Normal,
                color = GrayB4,
                fontSize = 12.sp
            )
            Text(
                if (!state.isRegister)  stringResource(R.string.register) else stringResource(R.string.login),
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    viewModel.onEvent(AuthEvent.SwitchRegisterState)
                }
            )

        }
    }
}