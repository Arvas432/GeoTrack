package com.example.geotrack.ui.authorization.state

sealed class AuthEffect {
    data class ShowError(val message: String) : AuthEffect()
    data class ShowMessage(val message: String) : AuthEffect()
    object NavigateToMainScreen : AuthEffect()
}
