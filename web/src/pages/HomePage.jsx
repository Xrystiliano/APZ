import { useState, useEffect, useCallback } from 'react';
import {
  Box, Typography, Grid, Button, TextField, InputAdornment, Skeleton,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import LockIcon from '@mui/icons-material/Lock';
import * as api from '../api/smartlockApi';
import LockCard from '../components/locks/LockCard';
import CreateLockDialog from '../components/locks/CreateLockDialog';
import ConfirmDialog from '../components/common/ConfirmDialog';
import ErrorSnackbar from '../components/common/ErrorSnackbar';
import { useAuth } from '../context/AuthContext';
import { useLang } from '../context/LanguageContext';

export default function HomePage() {
  const { user } = useAuth();
  const { t } = useLang();
  const [locks, setLocks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [createOpen, setCreateOpen] = useState(false);
  const [deletingLock, setDeletingLock] = useState(null);

  const fetchLocks = useCallback(async () => {
    try {
      const res = await api.getAllLocks();
      setLocks(res.data || []);
    } catch { setError(t('home_failed_load')); }
    finally { setLoading(false); }
  }, [t]);

  useEffect(() => { fetchLocks(); }, [fetchLocks]);

  const handleToggle = async (lockId, currentLocked) => {
    try {
      const res = currentLocked ? await api.unlockLock(lockId) : await api.lockLock(lockId);
      setLocks(prev => prev.map(l => l.lockId === lockId ? res.data : l));
    } catch { setError(t('home_failed_toggle')); }
  };

  const handleCreate = async (name, serialNumber, timezone) => {
    const res = await api.createLock(name, serialNumber, timezone);
    setLocks(prev => [...prev, res.data]);
  };

  const handleDeleteConfirm = async () => {
    if (!deletingLock) return;
    try {
      await api.deleteLock(deletingLock.lockId);
      setLocks(prev => prev.filter(l => l.lockId !== deletingLock.lockId));
    } catch { setError(t('home_failed_delete')); }
    finally { setDeletingLock(null); }
  };

  const filtered = locks.filter(l => l.name.toLowerCase().includes(search.toLowerCase()));
  const devicesLabel = locks.length === 1 ? t('home_subtitle_devices') : t('home_subtitle_devices_pl');

  return (
    <Box>
      {/* Page header */}
      <Box sx={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', mb: 3, flexWrap: 'wrap', gap: 2 }}>
        <Box>
          <Typography variant="h5" fontWeight={700} sx={{ mb: 0.25 }}>{t('home_title')}</Typography>
          <Typography variant="body2" color="text.secondary">
            {user?.fullName || user?.email} · {locks.length} {devicesLabel}
          </Typography>
        </Box>
        <Button variant="contained" size="small" startIcon={<AddIcon />} onClick={() => setCreateOpen(true)}>
          {t('home_add_lock')}
        </Button>
      </Box>

      {/* Search */}
      <TextField
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        placeholder={t('home_search')}
        size="small"
        sx={{ mb: 2.5, maxWidth: 320 }}
        slotProps={{
          input: {
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon sx={{ fontSize: 18, color: 'text.secondary' }} />
              </InputAdornment>
            ),
          },
        }}
      />

      {/* Grid */}
      {loading ? (
        <Grid container spacing={2}>
          {[1, 2, 3, 4, 5, 6].map(n => (
            <Grid item xs={12} sm={6} md={4} key={n}>
              <Skeleton variant="rounded" height={180} />
            </Grid>
          ))}
        </Grid>
      ) : filtered.length === 0 ? (
        <Box sx={{ py: 10, textAlign: 'center', border: '2px dashed #E0E0E0', borderRadius: 2, bgcolor: '#FFFFFF' }}>
          <Box sx={{
            width: 56, height: 56, borderRadius: '10px',
            bgcolor: '#EBF2FF', border: '1px solid #BAE0F7',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            mx: 'auto', mb: 2,
          }}>
            <LockIcon sx={{ fontSize: 28, color: 'primary.main' }} />
          </Box>
          <Typography variant="subtitle2" fontWeight={700} sx={{ mb: 0.5 }}>
            {search ? t('home_no_results') : t('home_empty_title')}
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2.5 }}>
            {search ? `${t('home_no_results_subtitle')} "${search}"` : t('home_empty_subtitle')}
          </Typography>
          {!search && (
            <Button variant="contained" size="small" startIcon={<AddIcon />} onClick={() => setCreateOpen(true)}>
              {t('home_add_lock')}
            </Button>
          )}
        </Box>
      ) : (
        <>
          <Grid container spacing={2}>
            {filtered.map(lock => (
              <Grid item xs={12} sm={6} md={4} key={lock.lockId}>
                <LockCard lock={lock} onToggle={handleToggle} onDelete={setDeletingLock} />
              </Grid>
            ))}
          </Grid>
          <Box sx={{ mt: 2.5, display: 'flex', gap: 3, color: 'text.secondary' }}>
            <Typography variant="caption">{t('home_stats_total')}: <strong style={{ color: '#161616' }}>{locks.length}</strong></Typography>
            <Typography variant="caption">{t('home_stats_locked')}: <strong style={{ color: '#0F62FE' }}>{locks.filter(l => l.locked).length}</strong></Typography>
            <Typography variant="caption">{t('home_stats_unlocked')}: <strong style={{ color: '#198038' }}>{locks.filter(l => !l.locked).length}</strong></Typography>
            <Typography variant="caption">{t('home_stats_online')}: <strong style={{ color: '#198038' }}>{locks.filter(l => l.status === 'ONLINE').length}</strong></Typography>
          </Box>
        </>
      )}

      <CreateLockDialog open={createOpen} onClose={() => setCreateOpen(false)} onCreate={handleCreate} />
      <ConfirmDialog
        open={Boolean(deletingLock)}
        title={t('detail_delete_title')}
        message={`${t('detail_delete_msg')} "${deletingLock?.name}"? ${t('detail_delete_msg2')}`}
        onConfirm={handleDeleteConfirm}
        onCancel={() => setDeletingLock(null)}
        confirmLabel={t('confirm_delete')}
      />
      <ErrorSnackbar message={error} onClose={() => setError('')} />
    </Box>
  );
}
