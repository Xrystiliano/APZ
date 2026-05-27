import { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box, Typography, Tabs, Tab, Button, TextField, IconButton,
  Tooltip, Breadcrumbs, Link, Skeleton, Divider,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';
import PeopleAltIcon from '@mui/icons-material/PeopleAlt';
import VpnKeyIcon from '@mui/icons-material/VpnKey';

import * as api from '../api/smartlockApi';
import { useAuth } from '../context/AuthContext';
import { useLang } from '../context/LanguageContext';
import LockControlPanel from '../components/lockdetail/LockControlPanel';
import LockRolesTab from '../components/lockdetail/LockRolesTab';
import AccessKeysTab from '../components/lockdetail/AccessKeysTab';
import ConfirmDialog from '../components/common/ConfirmDialog';
import ErrorSnackbar from '../components/common/ErrorSnackbar';

function TabPanel({ value, index, children }) {
  return value === index ? <Box sx={{ pt: 3 }}>{children}</Box> : null;
}

function InfoRow({ label, value }) {
  return (
    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', py: 1.5 }}>
      <Typography variant="body2" color="text.secondary" sx={{ minWidth: 160 }}>{label}</Typography>
      <Typography variant="body2" fontWeight={500} sx={{ textAlign: 'right', wordBreak: 'break-all' }}>{value || '—'}</Typography>
    </Box>
  );
}

export default function LockDetailPage() {
  const { lockId } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const { t } = useLang();

  const [lock, setLock] = useState(null);
  const [roles, setRoles] = useState([]);
  const [keys, setKeys] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState(0);
  const [error, setError] = useState('');
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [editing, setEditing] = useState(false);
  const [newName, setNewName] = useState('');

  const myRole = roles.find(r => r.email?.toLowerCase() === user?.email?.toLowerCase())?.lockRole || 'GUEST';
  const canManage = myRole === 'OWNER' || myRole === 'ADMIN';

  const fetchAll = useCallback(async () => {
    setLoading(true);
    try {
      const [locksRes, rolesRes] = await Promise.all([api.getAllLocks(), api.getLockRoles(lockId)]);
      setLock(locksRes.data?.find(l => l.lockId === lockId) || null);
      setRoles(rolesRes.data || []);
      const role = rolesRes.data?.find(r => r.email?.toLowerCase() === user?.email?.toLowerCase())?.lockRole || 'GUEST';
      if (role === 'ADMIN' || role === 'OWNER') {
        const keysRes = await api.getAllKeysOnLock(lockId);
        setKeys(keysRes.data || []);
      }
    } catch { setError(t('detail_failed_load')); }
    finally { setLoading(false); }
  }, [lockId, user?.email, t]);

  useEffect(() => { fetchAll(); }, [fetchAll]);

  const handleToggle = async (lid, isLocked) => {
    try {
      const res = isLocked ? await api.unlockLock(lid) : await api.lockLock(lid);
      setLock(res.data);
    } catch { setError(t('detail_failed_toggle')); }
  };

  const handleRename = async () => {
    if (!newName.trim()) return;
    try {
      const res = await api.editLock(lockId, newName.trim());
      setLock(res.data); setEditing(false);
    } catch { setError(t('detail_failed_rename')); }
  };

  const handleAddUser = async (email, role) => {
    try {
      await api.addUserToLock(lockId, email, role);
      const res = await api.getLockRoles(lockId);
      setRoles(res.data || []);
    } catch (e) { setError(e?.response?.data?.message || t('roles_failed_add')); }
  };

  const handleRemoveUser = async (email) => {
    try {
      await api.deleteUserFromLock(lockId, email);
      setRoles(prev => prev.filter(r => r.email !== email));
    } catch { setError(t('roles_failed_remove')); }
  };

  const handleChangeRole = async (email, newRole) => {
    try {
      await api.editUserLockRole(lockId, email, newRole);
      const res = await api.getLockRoles(lockId);
      setRoles(res.data || []);
    } catch { setError(t('roles_failed_role')); }
  };

  const handleGenerateKey = async (validFrom, validUntil) => {
    const res = await api.createAccessKey(lockId, new Date(validFrom).toISOString(), new Date(validUntil).toISOString());
    const keysRes = await api.getAllKeysOnLock(lockId);
    setKeys(keysRes.data || []);
    return res.data?.accessToken;
  };

  const handleRevokeKey = async (keyId) => {
    try {
      await api.deleteAccessKey(keyId);
      setKeys(prev => prev.filter(k => k.accessKeyId !== keyId));
    } catch { setError(t('keys_failed_revoke')); }
  };

  const handleDelete = async () => {
    try { await api.deleteLock(lockId); navigate('/'); }
    catch { setError(t('detail_failed_delete')); setDeleteOpen(false); }
  };

  if (loading) return (
    <Box>
      <Skeleton variant="text" width={200} height={32} sx={{ mb: 1 }} />
      <Skeleton variant="rounded" height={120} sx={{ mb: 3 }} />
      <Skeleton variant="rounded" height={300} />
    </Box>
  );

  if (!lock) return (
    <Box sx={{ textAlign: 'center', py: 10 }}>
      <Typography color="text.secondary">{t('detail_not_found')}</Typography>
      <Button sx={{ mt: 2 }} onClick={() => navigate('/')}>{t('detail_go_back')}</Button>
    </Box>
  );

  return (
    <Box>
      {/* Breadcrumb */}
      <Breadcrumbs sx={{ mb: 2 }}>
        <Link onClick={() => navigate('/')} sx={{ cursor: 'pointer', color: 'text.secondary', textDecoration: 'none', fontSize: '0.875rem', '&:hover': { color: 'primary.main' } }}>
          {t('detail_breadcrumb')}
        </Link>
        <Typography variant="body2" fontWeight={600} color="text.primary">{lock.name}</Typography>
      </Breadcrumbs>

      {/* Header */}
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5, mb: 3, flexWrap: 'wrap' }}>
        <IconButton size="small" onClick={() => navigate('/')} sx={{ color: 'text.secondary', border: '1px solid #E0E0E0' }}>
          <ArrowBackIcon fontSize="small" />
        </IconButton>

        {editing ? (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, flex: 1 }}>
            <TextField value={newName} onChange={(e) => setNewName(e.target.value)} size="small" autoFocus sx={{ maxWidth: 260 }}
              onKeyDown={(e) => { if (e.key === 'Enter') handleRename(); if (e.key === 'Escape') setEditing(false); }} />
            <IconButton size="small" onClick={handleRename} sx={{ color: 'success.main' }}><CheckIcon fontSize="small" /></IconButton>
            <IconButton size="small" onClick={() => setEditing(false)}><CloseIcon fontSize="small" /></IconButton>
          </Box>
        ) : (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, flex: 1 }}>
            <Typography variant="h5" fontWeight={700}>{lock.name}</Typography>
            {canManage && (
              <Tooltip title={t('detail_rename')}>
                <IconButton size="small" onClick={() => { setNewName(lock.name); setEditing(true); }} sx={{ color: 'text.secondary' }}>
                  <EditIcon fontSize="small" />
                </IconButton>
              </Tooltip>
            )}
          </Box>
        )}

        {myRole === 'OWNER' && (
          <Button variant="outlined" color="error" size="small" startIcon={<DeleteForeverIcon />} onClick={() => setDeleteOpen(true)}>
            {t('detail_delete')}
          </Button>
        )}
      </Box>

      <LockControlPanel lock={lock} onToggle={handleToggle} myRole={myRole} />

      {/* Tabs */}
      <Box sx={{ mt: 3 }}>
        <Tabs value={tab} onChange={(_, v) => setTab(v)} sx={{ borderBottom: '1px solid #E0E0E0' }}>
          <Tab icon={<InfoOutlinedIcon fontSize="small" />} iconPosition="start" label={t('detail_tab_info')} sx={{ minHeight: 44 }} />
          <Tab icon={<PeopleAltIcon fontSize="small" />} iconPosition="start" label={`${t('detail_tab_users')} (${roles.length})`} sx={{ minHeight: 44 }} />
          {canManage && <Tab icon={<VpnKeyIcon fontSize="small" />} iconPosition="start" label={`${t('detail_tab_keys')} (${keys.length})`} sx={{ minHeight: 44 }} />}
        </Tabs>

        <TabPanel value={tab} index={0}>
          <Box sx={{ bgcolor: '#FFFFFF', border: '1px solid #E0E0E0', borderRadius: 2, px: 3, py: 0.5 }}>
            <InfoRow label={t('detail_info_id')} value={lock.lockId} />
            <Divider />
            <InfoRow label={t('detail_info_status')} value={lock.status} />
            <Divider />
            <InfoRow label={t('detail_info_state')} value={lock.locked ? t('lock_locked') : t('lock_unlocked')} />
            <Divider />
            <InfoRow label={t('detail_info_heartbeat')} value={lock.lastHeartBeatAt ? new Date(lock.lastHeartBeatAt).toLocaleString() : '—'} />
          </Box>
        </TabPanel>

        <TabPanel value={tab} index={1}>
          <LockRolesTab roles={roles} myRole={myRole} myEmail={user?.email}
            onAdd={handleAddUser} onRemove={handleRemoveUser} onChangeRole={handleChangeRole} />
        </TabPanel>

        {canManage && (
          <TabPanel value={tab} index={2}>
            <AccessKeysTab keys={keys} myRole={myRole} onGenerate={handleGenerateKey} onRevoke={handleRevokeKey} />
          </TabPanel>
        )}
      </Box>

      <ConfirmDialog
        open={deleteOpen}
        title={t('detail_delete_title')}
        message={`${t('detail_delete_msg')} "${lock.name}" ${t('detail_delete_msg2')}`}
        onConfirm={handleDelete}
        onCancel={() => setDeleteOpen(false)}
        confirmLabel={t('confirm_delete')}
      />
      <ErrorSnackbar message={error} onClose={() => setError('')} />
    </Box>
  );
}
