import { useState } from 'react';
import {
  Box, Typography, Button, Paper, Chip, CircularProgress,
} from '@mui/material';
import LockIcon from '@mui/icons-material/Lock';
import LockOpenIcon from '@mui/icons-material/LockOpen';
import { useLang } from '../../context/LanguageContext';

export default function LockControlPanel({ lock, onToggle, myRole }) {
  const { t } = useLang();
  const [toggling, setToggling] = useState(false);
  const isLocked = lock?.locked;
  const canControl = ['GUEST', 'MEMBER', 'ADMIN', 'OWNER'].includes(myRole);

  const handleToggle = async () => {
    setToggling(true);
    await onToggle(lock.lockId, isLocked);
    setToggling(false);
  };

  if (!lock) return null;

  return (
    <Paper elevation={0} sx={{ p: 3, border: '1px solid #E0E0E0', borderRadius: 2 }}>
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 3 }}>
        <Box sx={{
          width: 72, height: 72, borderRadius: '12px', flexShrink: 0,
          bgcolor: isLocked ? '#EBF2FF' : '#F4F4F4',
          border: `2px solid ${isLocked ? '#BAE0F7' : '#E0E0E0'}`,
          display: 'flex', alignItems: 'center', justifyContent: 'center',
        }}>
          {isLocked
            ? <LockIcon sx={{ fontSize: 36, color: 'primary.main' }} />
            : <LockOpenIcon sx={{ fontSize: 36, color: 'text.secondary' }} />
          }
        </Box>

        <Box sx={{ flex: 1 }}>
          <Typography variant="h5" fontWeight={700} sx={{ mb: 0.5 }}>
            {isLocked ? t('lock_locked') : t('lock_unlocked')}
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 1.5 }}>
            {isLocked ? t('lock_access_restricted') : t('lock_access_open')}
          </Typography>
          <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
            <Chip size="small" label={`${t('lock_status')}: ${lock.status}`} sx={{
              fontSize: '0.72rem', fontWeight: 600, height: 22, borderRadius: '4px',
              bgcolor: lock.status === 'ONLINE' ? '#DEFBE6' : '#F4F4F4',
              color: lock.status === 'ONLINE' ? '#198038' : '#6E7E91',
              border: `1px solid ${lock.status === 'ONLINE' ? '#A7F0BA' : '#E0E0E0'}`,
            }} />
            <Chip size="small" label={`${t('lock_role')}: ${myRole}`} sx={{
              fontSize: '0.72rem', fontWeight: 600, height: 22, borderRadius: '4px',
              bgcolor: '#EBF2FF', color: 'primary.main', border: '1px solid #BAE0F7',
            }} />
          </Box>
        </Box>

        {canControl ? (
          <Button
            variant={isLocked ? 'contained' : 'outlined'}
            size="large"
            onClick={handleToggle}
            disabled={toggling}
            startIcon={toggling
              ? <CircularProgress size={18} color="inherit" />
              : (isLocked ? <LockOpenIcon /> : <LockIcon />)
            }
            sx={{ minWidth: 130, flexShrink: 0 }}
          >
            {toggling ? '…' : (isLocked ? t('lock_btn_unlock') : t('lock_btn_lock'))}
          </Button>
        ) : (
          <Chip label={t('lock_no_permission')} size="small" />
        )}
      </Box>
    </Paper>
  );
}
