package com.hnure.smartlock.data.local

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * Singleton session event bus for broadcasting session expiry (401 responses).
 * The NavGraph observes [sessionExpiredFlow] and navigates to Login.
 */
object SessionManager {
    private val _sessionExpiredFlow = MutableSharedFlow<Unit>(replay = 0)
    val sessionExpiredFlow: SharedFlow<Unit> = _sessionExpiredFlow.asSharedFlow()

    fun triggerSessionExpiry() {
        CoroutineScope(Dispatchers.IO).launch {
            _sessionExpiredFlow.emit(Unit)
        }
    }
}
