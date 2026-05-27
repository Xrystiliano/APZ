package com.hnure.smartlock.data.api

import retrofit2.Response
import retrofit2.http.*

interface SmartLockApi {

    // ── Auth ─────────────────────────────────────────────────────────────────

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    // ── Locks ─────────────────────────────────────────────────────────────────

    @GET("api/locks")
    suspend fun getAllLocks(): Response<List<LockDto>>

    @POST("api/locks")
    suspend fun createLock(@Body request: CreateLockRequest): Response<LockDto>

    @DELETE("api/locks/{lockId}")
    suspend fun deleteLock(@Path("lockId") lockId: String): Response<Unit>

    @PUT("api/locks/{lockId}")
    suspend fun editLock(
        @Path("lockId") lockId: String,
        @Body request: EditLockRequest
    ): Response<LockDto>

    @PUT("api/locks/{lockId}/lock")
    suspend fun lockLock(@Path("lockId") lockId: String): Response<LockDto>

    @PUT("api/locks/{lockId}/unlock")
    suspend fun unlockLock(@Path("lockId") lockId: String): Response<LockDto>

    // ── Lock Roles ────────────────────────────────────────────────────────────

    @GET("api/locks/{lockId}/roles")
    suspend fun getLockRoles(@Path("lockId") lockId: String): Response<List<LockRoleDto>>

    @POST("api/locks/{lockId}/roles")
    suspend fun addUserToLock(
        @Path("lockId") lockId: String,
        @Body request: EditLockRoleRequest
    ): Response<LockRoleDto>

    @PUT("api/locks/{lockId}/roles")
    suspend fun editUserLockRole(
        @Path("lockId") lockId: String,
        @Body request: EditLockRoleRequest
    ): Response<LockRoleDto>

    @DELETE("api/locks/{lockId}/roles/{email}")
    suspend fun deleteUserFromLock(
        @Path("lockId") lockId: String,
        @Path("email", encoded = false) email: String
    ): Response<Unit>

    // ── Access Keys ───────────────────────────────────────────────────────────

    @GET("api/access-key/lock/{lockId}")
    suspend fun getAllKeysOnLock(@Path("lockId") lockId: String): Response<List<AccessKeyDto>>

    @POST("api/access-key/lock/{lockId}")
    suspend fun createAccessKey(
        @Path("lockId") lockId: String,
        @Body request: CreateKeyRequest
    ): Response<AccessKeyDto>

    @DELETE("api/access-key/{keyId}")
    suspend fun deleteAccessKey(@Path("keyId") keyId: String): Response<Unit>
}
