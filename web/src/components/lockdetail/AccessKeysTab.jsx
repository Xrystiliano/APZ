import { useState } from 'react';
import {
  Box, Typography, Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow, Paper, IconButton, Tooltip, Chip, Button,
  TextField, Stack, Dialog, DialogTitle, DialogContent, DialogActions,
  Alert, CircularProgress,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import KeyIcon from '@mui/icons-material/VpnKey';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import CheckIcon from '@mui/icons-material/Check';
import { useLang } from '../../context/LanguageContext';

function formatDate(str) {
  if (!str) return '—';
  try { return new Date(str).toLocaleString(); } catch { return str; }
}

export default function AccessKeysTab({ keys, myRole, onGenerate, onRevoke }) {
  const { t } = useLang();
  const [genOpen, setGenOpen] = useState(false);
  const [validFrom, setValidFrom] = useState('');
  const [validUntil, setValidUntil] = useState('');
  const [generating, setGenerating] = useState(false);
  const [newToken, setNewToken] = useState(null);
  const [copied, setCopied] = useState(false);
  const [genError, setGenError] = useState('');

  const canManage = myRole === 'ADMIN' || myRole === 'OWNER';
  const canCreate = ['MEMBER', 'ADMIN', 'OWNER'].includes(myRole);

  const handleGenerate = async () => {
    if (!validFrom || !validUntil) { setGenError(t('keys_error_dates')); return; }
    if (new Date(validUntil) <= new Date(validFrom)) { setGenError(t('keys_error_order')); return; }
    setGenerating(true); setGenError('');
    try {
      const token = await onGenerate(validFrom, validUntil);
      if (token) setNewToken(token);
      else handleCloseGen();
    } catch (e) {
      setGenError(e?.response?.data?.message || t('keys_failed_generate'));
    } finally { setGenerating(false); }
  };

  const handleCopy = () => {
    navigator.clipboard.writeText(newToken);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleCloseGen = () => {
    setGenOpen(false); setNewToken(null);
    setValidFrom(''); setValidUntil(''); setGenError('');
  };

  return (
    <Box>
      {canCreate && (
        <Box sx={{ mb: 2, display: 'flex', justifyContent: 'flex-end' }}>
          <Button variant="contained" size="small" startIcon={<KeyIcon />} onClick={() => setGenOpen(true)}>
            {t('keys_generate')}
          </Button>
        </Box>
      )}

      {!canManage && (
        <Alert severity="info" sx={{ mb: 2, fontSize: '0.85rem' }}>{t('keys_admin_only')}</Alert>
      )}

      <TableContainer component={Paper} elevation={0} sx={{ border: '1px solid #E0E0E0', borderRadius: 2 }}>
        <Table size="small">
          <TableHead>
            <TableRow sx={{ bgcolor: '#F4F4F4' }}>
              {[t('keys_token'), t('keys_valid_from'), t('keys_valid_until'), t('keys_status'), ...(canManage ? [''] : [])].map((h, i) => (
                <TableCell key={i} sx={{ borderBottom: '1px solid #E0E0E0', py: 1.2 }}>{h}</TableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {keys.length === 0 ? (
              <TableRow>
                <TableCell colSpan={5} align="center" sx={{ py: 4, color: 'text.secondary', border: 0 }}>
                  {t('keys_no_keys')}
                </TableCell>
              </TableRow>
            ) : keys.map((key) => (
              <TableRow key={key.accessKeyId} hover>
                <TableCell sx={{ borderBottom: '1px solid #F4F4F4', py: 1.2 }}>
                  <Typography variant="caption" sx={{ fontFamily: 'monospace', color: 'text.secondary' }}>
                    {key.accessToken ? `${key.accessToken.substring(0, 18)}…` : '—'}
                  </Typography>
                </TableCell>
                <TableCell sx={{ borderBottom: '1px solid #F4F4F4', py: 1.2 }}>
                  <Typography variant="body2" color="text.secondary">{formatDate(key.validFrom)}</Typography>
                </TableCell>
                <TableCell sx={{ borderBottom: '1px solid #F4F4F4', py: 1.2 }}>
                  <Typography variant="body2" color="text.secondary">{formatDate(key.validUntil)}</Typography>
                </TableCell>
                <TableCell sx={{ borderBottom: '1px solid #F4F4F4', py: 1.2 }}>
                  <Chip
                    label={key.active ? t('keys_active') : t('keys_revoked')}
                    size="small"
                    sx={{
                      fontSize: '0.72rem', fontWeight: 600, height: 22, borderRadius: '4px',
                      bgcolor: key.active ? '#DEFBE6' : '#F4F4F4',
                      color: key.active ? '#198038' : '#6E7E91',
                      border: `1px solid ${key.active ? '#A7F0BA' : '#E0E0E0'}`,
                    }}
                  />
                </TableCell>
                {canManage && (
                  <TableCell align="right" sx={{ borderBottom: '1px solid #F4F4F4', py: 1.2 }}>
                    <Tooltip title={t('keys_revoke')}>
                      <IconButton size="small" onClick={() => onRevoke(key.accessKeyId)} sx={{ '&:hover': { color: 'error.main' } }}>
                        <DeleteIcon fontSize="small" />
                      </IconButton>
                    </Tooltip>
                  </TableCell>
                )}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Generate Dialog */}
      <Dialog open={genOpen} onClose={handleCloseGen} maxWidth="xs" fullWidth>
        <DialogTitle sx={{ fontWeight: 700, fontSize: '1rem', pb: 1 }}>{t('keys_dialog_title')}</DialogTitle>
        <DialogContent>
          {newToken ? (
            <Box sx={{ mt: 1 }}>
              <Alert severity="success" sx={{ mb: 2, fontSize: '0.83rem' }}>{t('keys_dialog_hint')}</Alert>
              <Paper elevation={0} sx={{ p: 1.5, bgcolor: '#F4F4F4', border: '1px solid #E0E0E0', borderRadius: 1.5, display: 'flex', alignItems: 'center', gap: 1 }}>
                <Typography variant="caption" sx={{ fontFamily: 'monospace', wordBreak: 'break-all', flex: 1, color: '#161616' }}>
                  {newToken}
                </Typography>
                <Tooltip title={copied ? t('keys_copied') : t('keys_copy')}>
                  <IconButton size="small" onClick={handleCopy} sx={{ color: copied ? 'success.main' : 'text.secondary', flexShrink: 0 }}>
                    {copied ? <CheckIcon fontSize="small" /> : <ContentCopyIcon fontSize="small" />}
                  </IconButton>
                </Tooltip>
              </Paper>
            </Box>
          ) : (
            <Stack spacing={2} sx={{ mt: 1 }}>
              {genError && <Alert severity="error" sx={{ fontSize: '0.83rem' }}>{genError}</Alert>}
              <TextField label={t('keys_dialog_from')} type="datetime-local" value={validFrom} onChange={(e) => setValidFrom(e.target.value)} fullWidth size="small" InputLabelProps={{ shrink: true }} />
              <TextField label={t('keys_dialog_until')} type="datetime-local" value={validUntil} onChange={(e) => setValidUntil(e.target.value)} fullWidth size="small" InputLabelProps={{ shrink: true }} />
            </Stack>
          )}
        </DialogContent>
        <DialogActions sx={{ px: 3, pb: 2.5, gap: 1 }}>
          <Button onClick={handleCloseGen} variant="outlined" size="small">{newToken ? t('keys_dialog_done') : t('confirm_cancel')}</Button>
          {!newToken && (
            <Button onClick={handleGenerate} variant="contained" size="small" disabled={generating}>
              {generating ? <CircularProgress size={15} color="inherit" /> : t('keys_dialog_generate')}
            </Button>
          )}
        </DialogActions>
      </Dialog>
    </Box>
  );
}
