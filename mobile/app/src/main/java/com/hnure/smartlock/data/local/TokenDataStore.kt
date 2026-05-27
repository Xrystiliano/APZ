package com.hnure.smartlock.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "smartlock_prefs")

@Singleton
class TokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val JWT_TOKEN = stringPreferencesKey("jwt_token")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_FULL_NAME = stringPreferencesKey("user_full_name")
    }

    val jwtToken: Flow<String?> = context.dataStore.data.map { it[JWT_TOKEN] }
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[USER_EMAIL] }
    val userFullName: Flow<String?> = context.dataStore.data.map { it[USER_FULL_NAME] }

    suspend fun saveSession(token: String, id: String, email: String, fullName: String) {
        context.dataStore.edit { prefs ->
            prefs[JWT_TOKEN] = token
            prefs[USER_ID] = id
            prefs[USER_EMAIL] = email
            prefs[USER_FULL_NAME] = fullName
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
