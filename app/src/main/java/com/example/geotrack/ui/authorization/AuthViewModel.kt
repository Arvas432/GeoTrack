package com.example.geotrack.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geotrack.data.network.dto.UserCredentials
import com.example.geotrack.domain.auth.AuthInteractor
import com.example.geotrack.domain.auth.model.ResultType
import com.example.geotrack.ui.authorization.state.AuthEffect
import com.example.geotrack.ui.authorization.state.AuthEvent
import com.example.geotrack.ui.authorization.state.AuthState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authInteractor: AuthInteractor) :
    ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>()
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()
    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> login(event.username, event.password)
            is AuthEvent.Register -> register(event.username, event.password)
            is AuthEvent.SwitchRegisterState -> _state.value =
                _state.value.copy(isRegister = !_state.value.isRegister)
        }
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val result = authInteractor.login(UserCredentials(username, password))
                if (result.isSuccess) {
                    val resultType = result.getOrNull()
                    when (resultType) {
                        ResultType.SUCCESS -> _effect.emit(AuthEffect.NavigateToMainScreen)
                        ResultType.NO_CONNECTION -> _effect.emit(AuthEffect.ShowError("Нет соединения"))
                        ResultType.SERVER_ERROR -> _effect.emit(AuthEffect.ShowError("Ошибка входа"))
                        ResultType.EMPTY -> _effect.emit(AuthEffect.ShowError("Ошибка входа"))
                        null -> _effect.emit(AuthEffect.ShowError("Ошибка входа"))
                    }

                } else {
                    _effect.emit(AuthEffect.ShowError("Ошибка входа"))
                }

            } catch (e: Exception) {
                _effect.emit(AuthEffect.ShowError("Ошибка входа: ${e.message}"))
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun register(username: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val result = authInteractor.register(UserCredentials(username, password))
                if (result.isSuccess) {
                    val resultType = result.getOrNull()
                    when (resultType) {
                        ResultType.SUCCESS -> {
                            _effect.emit(AuthEffect.ShowMessage("Регистрация успешна"))
                            _state.value = _state.value.copy(isRegister = !_state.value.isRegister)
                        }

                        ResultType.NO_CONNECTION -> _effect.emit(AuthEffect.ShowError("Нет соединения"))
                        ResultType.SERVER_ERROR -> _effect.emit(AuthEffect.ShowError("Ошибка регистрации"))
                        ResultType.EMPTY -> _effect.emit(AuthEffect.ShowError("Ошибка регистрации"))
                        null -> _effect.emit(AuthEffect.ShowError("Ошибка регистрации"))
                    }

                } else {
                    _effect.emit(AuthEffect.ShowError("Ошибка регистрации"))
                }
            } catch (e: Exception) {
                _effect.emit(AuthEffect.ShowError("Ошибка регистрации: ${e.message}"))
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}