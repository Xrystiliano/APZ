package com.hnure.smartlock.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnure.smartlock.data.api.LockDto
import com.hnure.smartlock.data.repository.LockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val locks: List<LockDto> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val searchQuery: String = ""
) {
    val filtered: List<LockDto>
        get() = if (searchQuery.isBlank()) locks
        else locks.filter { it.name.contains(searchQuery, ignoreCase = true) }

    val totalCount: Int get() = locks.size
    val lockedCount: Int get() = locks.count { it.locked }
    val unlockedCount: Int get() = locks.count { !it.locked }
    val onlineCount: Int get() = locks.count { it.status == "ONLINE" }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val lockRepository: LockRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init { fetchLocks() }

    fun fetchLocks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val locks = lockRepository.getAllLocks()
                _state.value = _state.value.copy(locks = locks, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = "Не вдалося завантажити замки")
            }
        }
    }

    fun onSearchChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun toggleLock(lockId: String, isLocked: Boolean) {
        viewModelScope.launch {
            try {
                val updated = if (isLocked) lockRepository.unlockLock(lockId)
                              else lockRepository.lockLock(lockId)
                _state.value = _state.value.copy(
                    locks = _state.value.locks.map { if (it.lockId == lockId) updated else it }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Не вдалося змінити стан замку")
            }
        }
    }

    fun createLock(name: String, serialNumber: String, timezone: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val newLock = lockRepository.createLock(name, serialNumber, timezone)
                _state.value = _state.value.copy(locks = _state.value.locks + newLock)
                onSuccess()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Помилка створення замку")
            }
        }
    }

    fun deleteLock(lockId: String) {
        viewModelScope.launch {
            try {
                lockRepository.deleteLock(lockId)
                _state.value = _state.value.copy(
                    locks = _state.value.locks.filter { it.lockId != lockId }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Не вдалося видалити замок")
            }
        }
    }

    fun clearError() { _state.value = _state.value.copy(error = null) }
}
