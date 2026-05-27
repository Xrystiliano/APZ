package com.hnure.smartlock.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnure.smartlock.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val email: String = "",
    val fullName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    fun onEmailChange(v: String) { _state.value = _state.value.copy(email = v, error = null) }
    fun onFullNameChange(v: String) { _state.value = _state.value.copy(fullName = v, error = null) }
    fun onPasswordChange(v: String) { _state.value = _state.value.copy(password = v, error = null) }
    fun onConfirmPasswordChange(v: String) { _state.value = _state.value.copy(confirmPassword = v, error = null) }

    fun register() {
        val s = _state.value
        when {
            s.email.isBlank() || s.fullName.isBlank() || s.password.isBlank() ->
                _state.value = s.copy(error = "Заповніть усі поля")
            s.password != s.confirmPassword ->
                _state.value = s.copy(error = "Паролі не збігаються")
            s.password.length < 6 ->
                _state.value = s.copy(error = "Пароль має містити мінімум 6 символів")
            else -> viewModelScope.launch {
                _state.value = s.copy(isLoading = true, error = null)
                try {
                    authRepository.register(s.email.trim(), s.password, s.fullName.trim())
                    _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                } catch (e: Exception) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Помилка реєстрації"
                    )
                }
            }
        }
    }
}
