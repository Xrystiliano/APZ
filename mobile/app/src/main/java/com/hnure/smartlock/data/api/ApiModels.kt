package com.hnure.smartlock.data.api

import com.google.gson.annotations.SerializedName

// ── Auth ──────────────────────────────────────────────────────────────────────

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String
)

data class AuthResponse(
    val jwtToken: String,
    val id: String,
    val email: String,
    val fullName: String
)

// ── Locks ─────────────────────────────────────────────────────────────────────

data class LockDto(
    val lockId: String,
    val name: String,
    val serialNumber: String? = null,
    val timezone: String? = null,
    val locked: Boolean = false,
    val status: String = "OFFLINE",         // "ONLINE" | "OFFLINE"
    val lastHeartBeatAt: String? = null
)

data class CreateLockRequest(
    val name: String,
    val serialNumber: String,
    val timezone: String
)

data class EditLockRequest(
    val name: String
)

// ── Lock Roles ────────────────────────────────────────────────────────────────

data class LockRoleDto(
    val email: String,
    val lockRole: String              // GUEST | MEMBER | ADMIN | OWNER
)

data class EditLockRoleRequest(
    val email: String,
    @SerializedName("lockrole") val lockrole: String
)

// ── Access Keys ───────────────────────────────────────────────────────────────

data class AccessKeyDto(
    val accessKeyId: String,
    val accessToken: String? = null,
    val validFrom: String? = null,
    val validUntil: String? = null,
    val active: Boolean = false
)

data class CreateKeyRequest(
    val validFrom: String,
    val validUntil: String
)
