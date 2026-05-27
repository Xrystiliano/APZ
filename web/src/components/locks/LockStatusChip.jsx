import { Chip } from '@mui/material';
import WifiIcon from '@mui/icons-material/Wifi';
import WifiOffIcon from '@mui/icons-material/WifiOff';
import { useLang } from '../../context/LanguageContext';

export default function LockStatusChip({ status }) {
  const { t } = useLang();

  const cfg = status === 'ONLINE'
    ? { label: t('status_online'), bgcolor: '#DEFBE6', color: '#198038', borderColor: '#A7F0BA', Icon: WifiIcon }
    : { label: t('status_offline'), bgcolor: '#F4F4F4', color: '#6E7E91', borderColor: '#E0E0E0', Icon: WifiOffIcon };

  return (
    <Chip
      size="small"
      label={cfg.label}
      icon={<cfg.Icon style={{ fontSize: 13, color: cfg.color }} />}
      sx={{
        bgcolor: cfg.bgcolor,
        color: cfg.color,
        border: `1px solid ${cfg.borderColor}`,
        fontWeight: 600,
        fontSize: '0.72rem',
        height: 22,
        '& .MuiChip-icon': { ml: '6px' },
      }}
    />
  );
}
