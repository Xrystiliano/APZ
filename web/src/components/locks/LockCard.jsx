import { useState } from 'react';
import {
  Card, CardContent, CardActions, Typography, Box,
  IconButton, Tooltip, Chip, CircularProgress, Button,
} from '@mui/material';
import LockOpenIcon from '@mui/icons-material/LockOpen';
import LockIcon from '@mui/icons-material/Lock';
import SettingsIcon from '@mui/icons-material/Settings';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import { useNavigate } from 'react-router-dom';
import LockStatusChip from './LockStatusChip';
import { useLang } from '../../context/LanguageContext';

export default function LockCard({ lock, onToggle, onDelete }) {
  const navigate = useNavigate();
  const { t } = useLang();
  const [toggling, setToggling] = useState(false);
  const isLocked = lock.locked;

  const handleToggle = async (e) => {
    e.stopPropagation();
    setToggling(true);
    await onToggle(lock.lockId, isLocked);
    setToggling(false);
  };

  const handleSettings = (e) => {
    e.stopPropagation();
    navigate(`/locks/${lock.lockId}`);
  };

  const handleDelete = (e) => {
    e.stopPropagation();
    onDelete(lock);
  };

  return (
    <Card onClick={handleSettings} sx={{ cursor: 'pointer', height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardContent sx={{ flex: 1, pb: 1 }}>
        <Box sx={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', mb: 2 }}>
          <Box sx={{
            width: 44, height: 44, borderRadius: '8px',
            bgcolor: isLocked ? '#EBF2FF' : '#F4F4F4',
            border: `1px solid ${isLocked ? '#BAE0F7' : '#E0E0E0'}`,
            display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
          }}>
            {isLocked
              ? <LockIcon sx={{ fontSize: 22, color: 'primary.main' }} />
              : <LockOpenIcon sx={{ fontSize: 22, color: 'text.secondary' }} />
            }
          </Box>
          <LockStatusChip status={lock.status} />
        </Box>

        <Typography variant="subtitle2" fontWeight={700} noWrap sx={{ mb: 0.5 }}>{lock.name}</Typography>

        <Chip
          label={isLocked ? t('lock_locked') : t('lock_unlocked')}
          size="small"
          sx={{
            fontWeight: 600, fontSize: '0.72rem', height: 20,
            bgcolor: isLocked ? '#EBF2FF' : '#F4F4F4',
            color: isLocked ? 'primary.main' : 'text.secondary',
            border: `1px solid ${isLocked ? '#BAE0F7' : '#E0E0E0'}`,
            borderRadius: '4px',
          }}
        />

        {lock.lastHeartBeatAt && (
          <Typography variant="caption" color="text.secondary" sx={{ mt: 1.5, display: 'block' }}>
            {t('lock_last_seen')} {new Date(lock.lastHeartBeatAt).toLocaleString()}
          </Typography>
        )}
      </CardContent>

      <CardActions sx={{ px: 1.5, pb: 1.5, pt: 0, gap: 0.5, borderTop: '1px solid #F4F4F4' }} onClick={e => e.stopPropagation()}>
        <Button
          size="small"
          variant={isLocked ? 'contained' : 'outlined'}
          startIcon={toggling
            ? <CircularProgress size={13} color="inherit" />
            : (isLocked ? <LockOpenIcon fontSize="small" /> : <LockIcon fontSize="small" />)
          }
          onClick={handleToggle}
          disabled={toggling}
          sx={{ fontSize: '0.78rem', px: 1.5, py: 0.5, flex: 1 }}
        >
          {isLocked ? t('lock_btn_unlock') : t('lock_btn_lock')}
        </Button>

        <Tooltip title={t('detail_tab_info')}>
          <IconButton size="small" onClick={handleSettings} sx={{ color: 'text.secondary' }}>
            <SettingsIcon fontSize="small" />
          </IconButton>
        </Tooltip>

        <Tooltip title={t('confirm_delete')}>
          <IconButton size="small" onClick={handleDelete} sx={{ color: 'text.secondary', '&:hover': { color: 'error.main' } }}>
            <DeleteIcon fontSize="small" />
          </IconButton>
        </Tooltip>
      </CardActions>
    </Card>
  );
}
