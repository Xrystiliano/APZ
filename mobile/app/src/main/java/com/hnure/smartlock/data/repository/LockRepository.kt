package com.hnure.smartlock.data.repository

import com.hnure.smartlock.data.api.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockRepository @Inject constructor(private val api: SmartLockApi) {

    // ── Locks ─────────────────────────────────────────────────────────────────

    suspend fun getAllLocks(): List<LockDto> {
        val response = api.getAllLocks()
        return if (response.isSuccessful) response.body() ?: emptyList()
        else throw Exception("Помилка завантаження замків (${response.code()})")
    }

    suspend fun createLock(name: String, serialNumber: String, timezone: String): LockDto {
        val response = api.createLock(CreateLockRequest(name, serialNumber, timezone))
        return if (response.isSuccessful) response.body()!!
        else {
            val err = response.errorBody()?.string() ?: "Помилка створення замку"
            throw Exception(err)
        }
    }

    suspend fun deleteLock(lockId: String) {
        val response = api.deleteLock(lockId)
        if (!response.isSuccessful) throw Exception("Помилка видалення замку (${response.code()})")
    }

    suspend fun editLock(lockId: String, name: String): LockDto {
        val response = api.editLock(lockId, EditLockRequest(name))
        return if (response.isSuccessful) response.body()!!
        else throw Exception("Помилка перейменування (${response.code()})")
    }

    suspend fun lockLock(lockId: String): LockDto {
        val response = api.lockLock(lockId)
        return if (response.isSuccessful) response.body()!!
        else throw Exception("Помилка блокування (${response.code()})")
    }

    suspend fun unlockLock(lockId: String): LockDto {
        val response = api.unlockLock(lockId)
        return if (response.isSuccessful) response.body()!!
        else throw Exception("Помилка розблокування (${response.code()})")
    }

    // ── Lock Roles ────────────────────────────────────────────────────────────

    suspend fun getLockRoles(lockId: String): List<LockRoleDto> {
        val response = api.getLockRoles(lockId)
        return if (response.isSuccessful) response.body() ?: emptyList()
        else throw Exception("Помилка завантаження ролей (${response.code()})")
    }

    suspend fun addUserToLock(lockId: String, email: String, role: String): LockRoleDto {
        val response = api.addUserToLock(lockId, EditLockRoleRequest(email, role))
        return if (response.isSuccessful) response.body()!!
        else {
            val err = response.errorBody()?.string() ?: "Помилка додавання користувача"
            throw Exception(err)
        }
    }

    suspend fun editUserLockRole(lockId: String, email: String, role: String): LockRoleDto {
        val response = api.editUserLockRole(lockId, EditLockRoleRequest(email, role))
        return if (response.isSuccessful) response.body()!!
        else throw Exception("Помилка зміни ролі (${response.code()})")
    }

    suspend fun deleteUserFromLock(lockId: String, email: String) {
        val response = api.deleteUserFromLock(lockId, email)
        if (!response.isSuccessful) throw Exception("Помилка видалення користувача (${response.code()})")
    }

    // ── Access Keys ───────────────────────────────────────────────────────────

    suspend fun getAllKeysOnLock(lockId: String): List<AccessKeyDto> {
        val response = api.getAllKeysOnLock(lockId)
        return if (response.isSuccessful) response.body() ?: emptyList()
        else throw Exception("Помилка завантаження ключів (${response.code()})")
    }

    suspend fun createAccessKey(lockId: String, validFrom: String, validUntil: String): AccessKeyDto {
        val response = api.createAccessKey(lockId, CreateKeyRequest(validFrom, validUntil))
        return if (response.isSuccessful) response.body()!!
        else {
            val err = response.errorBody()?.string() ?: "Помилка генерації ключа"
            throw Exception(err)
        }
    }

    suspend fun deleteAccessKey(keyId: String) {
        val response = api.deleteAccessKey(keyId)
        if (!response.isSuccessful) throw Exception("Помилка відкликання ключа (${response.code()})")
    }
}
