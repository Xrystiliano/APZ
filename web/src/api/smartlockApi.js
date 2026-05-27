import api from './axios';

// ── Auth ─────────────────────────────────────────────────────────────────────
export const login = (email, password) =>
  api.post('/api/auth/login', { email, password });

export const register = (email, password, fullName) =>
  api.post('/api/auth/register', { email, password, fullName });

// ── Locks ─────────────────────────────────────────────────────────────────────
export const getAllLocks = () => api.get('/api/locks');

export const createLock = (name, serialNumber, timezone) =>
  api.post('/api/locks', { name, serialNumber, timezone });

export const deleteLock = (lockId) => api.delete(`/api/locks/${lockId}`);

export const editLock = (lockId, name) =>
  api.put(`/api/locks/${lockId}`, { name });

export const lockLock = (lockId) => api.put(`/api/locks/${lockId}/lock`);

export const unlockLock = (lockId) => api.put(`/api/locks/${lockId}/unlock`);

// ── Lock Roles ────────────────────────────────────────────────────────────────
export const getLockRoles = (lockId) => api.get(`/api/locks/${lockId}/roles`);

export const addUserToLock = (lockId, email, lockrole) =>
  api.post(`/api/locks/${lockId}/roles`, { email, lockrole });

export const editUserLockRole = (lockId, email, lockrole) =>
  api.put(`/api/locks/${lockId}/roles`, { email, lockrole });

export const deleteUserFromLock = (lockId, email) =>
  api.delete(`/api/locks/${lockId}/roles/${encodeURIComponent(email)}`);

// ── Access Keys ───────────────────────────────────────────────────────────────
export const getAllKeysOnLock = (lockId) =>
  api.get(`/api/access-key/lock/${lockId}`);

export const createAccessKey = (lockId, validFrom, validUntil) =>
  api.post(`/api/access-key/lock/${lockId}`, { validFrom, validUntil });

export const deleteAccessKey = (keyId) =>
  api.delete(`/api/access-key/${keyId}`);
