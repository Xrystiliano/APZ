import { useState } from 'react';
import {
  Dialog, DialogTitle, DialogContent, DialogActions,
  TextField, Button, Stack, Typography, Alert,
} from '@mui/material';
import { useLang } from '../../context/LanguageContext';

export default function CreateLockDialog({ open, onClose, onCreate }) {
  const { t } = useLang();
  const [form, setForm] = useState({ name: '', serialNumber: '', timezone: 'Europe/Kyiv' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const handleSubmit = async () => {
    if (!form.name.trim() || !form.serialNumber.trim()) { setError(t('create_error_required')); return; }
    setLoading(true); setError('');
    try {
      await onCreate(form.name.trim(), form.serialNumber.trim(), form.timezone.trim() || 'UTC');
      setForm({ name: '', serialNumber: '', timezone: 'Europe/Kyiv' });
      onClose();
    } catch (e) {
      setError(e?.response?.data?.message || t('create_failed'));
    } finally { setLoading(false); }
  };

  const handleClose = () => {
    setForm({ name: '', serialNumber: '', timezone: 'Europe/Kyiv' });
    setError(''); onClose();
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
      <DialogTitle sx={{ fontWeight: 700, fontSize: '1rem', pb: 0.5 }}>{t('create_title')}</DialogTitle>
      <DialogContent>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2, mt: 0.5 }}>{t('create_subtitle')}</Typography>
        {error && <Alert severity="error" sx={{ mb: 2, fontSize: '0.83rem' }}>{error}</Alert>}
        <Stack spacing={2}>
          <TextField label={t('create_name')} name="name" value={form.name} onChange={handleChange} fullWidth autoFocus placeholder={t('create_name_ph')} size="small" />
          <TextField label={t('create_serial')} name="serialNumber" value={form.serialNumber} onChange={handleChange} fullWidth placeholder={t('create_serial_ph')} size="small" />
          <TextField label={t('create_timezone')} name="timezone" value={form.timezone} onChange={handleChange} fullWidth size="small" />
        </Stack>
      </DialogContent>
      <DialogActions sx={{ px: 3, pb: 2.5, gap: 1 }}>
        <Button onClick={handleClose} variant="outlined" size="small">{t('confirm_cancel')}</Button>
        <Button onClick={handleSubmit} variant="contained" size="small" disabled={loading}>
          {loading ? t('create_btn_loading') : t('create_btn')}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
