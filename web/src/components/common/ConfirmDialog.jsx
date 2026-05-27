import {
  Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button,
} from '@mui/material';
import { useLang } from '../../context/LanguageContext';

export default function ConfirmDialog({ open, title, message, onConfirm, onCancel, confirmLabel, confirmColor = 'error' }) {
  const { t } = useLang();
  return (
    <Dialog open={open} onClose={onCancel} maxWidth="xs" fullWidth>
      <DialogTitle sx={{ fontWeight: 700, fontSize: '1rem', pb: 0.5 }}>{title}</DialogTitle>
      <DialogContent>
        <DialogContentText sx={{ fontSize: '0.875rem', color: 'text.secondary' }}>{message}</DialogContentText>
      </DialogContent>
      <DialogActions sx={{ px: 3, pb: 2.5, gap: 1 }}>
        <Button onClick={onCancel} variant="outlined" size="small">{t('confirm_cancel')}</Button>
        <Button onClick={onConfirm} variant="contained" color={confirmColor} size="small">
          {confirmLabel || t('confirm_delete')}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
