package com.hnure.smartlock.ui.screens.lockdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnure.smartlock.data.api.AccessKeyDto
import com.hnure.smartlock.data.api.LockDto
import com.hnure.smartlock.data.api.LockRoleDto
import com.hnure.smartlock.data.local.TokenDataStore
import com.hnure.smartlock.data.repository.LockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LockDetailUiState(
    val lock: LockDto? = null,
    val roles: List<LockRoleDto> = emptyList(),
    val keys: List<AccessKeyDto> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val myEmail: String = "",
    val isDeleted: Boolean = false
) {
    val myRole: String
        get() = roles.find { it.email.equals(myEmail, ignoreCase = true) }?.lockRole ?: "GUEST"
    val canManage: Boolean
        get() = myRole == "OWNER" || myRole == "ADMIN"
}

@HiltViewModel
class LockDetailViewModel @Inject constructor(
    private val lockRepository: LockRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(LockDetailUiState())
    val state = _state.asStateFlow()

    fun load(lockId: String) {
        viewModelScope.launch {
            val email = tokenDataStore.userEmail.first() ?: ""
            _state.value = _state.value.copy(isLoading = true, myEmail = email, error = null)
            try {
                val locks = lockRepository.getAllLocks()
                val lock = locks.find { it.lockId == lockId }
                val roles = lockRepository.getLockRoles(lockId)
                val myRole = roles.find { it.email.equals(email, ignoreCase = true) }?.lockRole ?: "GUEST"
                val keys = if (myRole == "ADMIN" || myRole == "OWNER") lockRepository.getAllKeysOnLock(lockId) else emptyList()
                _state.value = _state.value.copy(
                    lock = lock, roles = roles, keys = keys, isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = "Не вдалося завантажити дані")
            }
        }
    }

    fun toggleLock(lockId: String, isLocked: Boolean) {
        viewModelScope.launch {
            try {
                val updated = if (isLocked) lockRepository.unlockLock(lockId) else lockRepository.lockLock(lockId)
                _state.value = _state.value.copy(lock = updated)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Не вдалося змінити стан замку")
            }
        }
    }

    fun renameLock(lockId: String, name: String) {
        viewModelScope.launch {
            try {
                val updated = lockRepository.editLock(lockId, name)
                _state.value = _state.value.copy(lock = updated)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Не вдалося перейменувати замок")
            }
        }
    }

    fun deleteLock(lockId: String) {
        viewModelScope.launch {
            try {
                lockRepository.deleteLock(lockId)
                _state.value = _state.value.copy(isDeleted = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Не вдалося видалити замок")
            }
        }
    }

    fun addUser(lockId: String, email: String, role: String) {
        viewModelScope.launch {
            try {
                lockRepository.addUserToLock(lockId, email, role)
                val roles = lockRepository.getLockRoles(lockId)
                _state.value = _state.value.copy(roles = roles)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Не вдалося додати користувача")
            }
        }
    }

    fun removeUser(lockId: String, email: String) {
        viewModelScope.launch {
            try {
                lockRepository.deleteUserFromLock(lockId, email)
                _state.value = _state.value.copy(
                    roles = _state.value.roles.filter { it.email != email }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Не вдалося видалити користувача")
            }
        }
    }

    fun changeRole(lockId: String, email: String, role: String) {
        viewModelScope.launch {
            try {
                lockRepository.editUserLockRole(lockId, email, role)
                val roles = lockRepository.getLockRoles(lockId)
                _state.value = _state.value.copy(roles = roles)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Не вдалося змінити роль")
            }
        }
    }

    fun generateKey(lockId: String, validFrom: String, validUntil: String, onToken: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val key = lockRepository.createAccessKey(lockId, validFrom, validUntil)
                val keys = lockRepository.getAllKeysOnLock(lockId)
                _state.value = _state.value.copy(keys = keys)
                onToken(key.accessToken)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Не вдалося згенерувати ключ")
                onToken(null)
            }
        }
    }

    fun revokeKey(keyId: String) {
        viewModelScope.launch {
            try {
                lockRepository.deleteAccessKey(keyId)
                _state.value = _state.value.copy(
                    keys = _state.value.keys.filter { it.accessKeyId != keyId }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Не вдалося відкликати ключ")
            }
        }
    }

    fun clearError() { _state.value = _state.value.copy(error = null) }
}
