package com.example.geotrack.ui.authorization.state

sealed class AuthEvent {
    data class Login(val username: String, val password: String) : AuthEvent()
    data class Register(val username: String, val password: String) : AuthEvent()
    object SwitchRegisterState: AuthEvent()
}
