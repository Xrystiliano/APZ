import { useState } from 'react';
import {
  Box, Typography, Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow, Paper, IconButton, Tooltip, Chip,
  TextField, Button, Select, MenuItem, FormControl, InputLabel,
  Stack, CircularProgress,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import EditIcon from '@mui/icons-material/Edit';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import { useLang } from '../../context/LanguageContext';

const ROLES = ['GUEST', 'MEMBER', 'ADMIN', 'OWNER'];

const roleStyle = {
  OWNER:  { bgcolor: '#EBF2FF', color: '#0043CE', border: '#BAE0F7' },
  ADMIN:  { bgcolor: '#EBF2FF', color: '#0F62FE', border: '#BAE0F7' },
  MEMBER: { bgcolor: '#DEFBE6', color: '#198038', border: '#A7F0BA' },
  GUEST:  { bgcolor: '#F4F4F4', color: '#6E7E91', border: '#E0E0E0' },
};

function RoleChip({ role }) {
  const s = roleStyle[role] || roleStyle.GUEST;
  return (
    <Chip label={role} size="small" sx={{
      fontSize: '0.72rem', fontWeight: 600, height: 22, borderRadius: '4px',
      bgcolor: s.bgcolor, color: s.color, border: `1px solid ${s.border}`,
    }} />
  );
}

export default function LockRolesTab({ roles, myRole, myEmail, onAdd, onRemove, onChangeRole }) {
  const { t } = useLang();
  const [inviteEmail, setInviteEmail] = useState('');
  const [inviteRole, setInviteRole] = useState('GUEST');
  const [inviting, setInviting] = useState(false);
  const [editingEmail, setEditingEmail] = useState(null);
  const [editRole, setEditRole] = useState('');

  const canManage = myRole === 'OWNER' || myRole === 'ADMIN';

  const handleInvite = async () => {
    if (!inviteEmail.trim()) return;
    setInviting(true);
    await onAdd(inviteEmail.trim(), inviteRole);
    setInviteEmail(''); setInviteRole('GUEST'); setInviting(false);
  };

  const handleEditConfirm = async () => {
    await onChangeRole(editingEmail, editRole);
    setEditingEmail(null);
  };

  return (
    <Box>
      {canManage && (
        <Paper elevation={0} sx={{ p: 2, mb: 2.5, border: '1px solid #E0E0E0', borderRadius: 2 }}>
          <Typography variant="subtitle2" fontWeight={700} sx={{ mb: 1.5 }}>{t('roles_invite')}</Typography>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} alignItems="flex-end">
            <TextField
              label={t('roles_email')}
              value={inviteEmail}
              onChange={(e) => setInviteEmail(e.target.value)}
              size="small" fullWidth
              placeholder="user@example.com"
              onKeyDown={(e) => e.key === 'Enter' && handleInvite()}
            />
            <FormControl size="small" sx={{ minWidth: 120 }}>
              <InputLabel>{t('roles_role')}</InputLabel>
              <Select value={inviteRole} label={t('roles_role')} onChange={(e) => setInviteRole(e.target.value)}>
                {ROLES.filter(r => r !== 'OWNER').map(r => <MenuItem key={r} value={r}>{r}</MenuItem>)}
              </Select>
            </FormControl>
            <Button
              variant="contained" size="small"
              startIcon={inviting ? <CircularProgress size={13} color="inherit" /> : <PersonAddIcon />}
              onClick={handleInvite}
              disabled={inviting || !inviteEmail.trim()}
              sx={{ whiteSpace: 'nowrap', minWidth: 100, py: '7px' }}
            >
              {t('roles_invite_btn')}
            </Button>
          </Stack>
        </Paper>
      )}

      <TableContainer component={Paper} elevation={0} sx={{ border: '1px solid #E0E0E0', borderRadius: 2 }}>
        <Table size="small">
          <TableHead>
            <TableRow sx={{ bgcolor: '#F4F4F4' }}>
              <TableCell sx={{ borderBottom: '1px solid #E0E0E0', py: 1.2 }}>User</TableCell>
              <TableCell sx={{ borderBottom: '1px solid #E0E0E0', py: 1.2 }}>{t('roles_role')}</TableCell>
              {canManage && <TableCell align="right" sx={{ borderBottom: '1px solid #E0E0E0', py: 1.2 }}>{t('roles_actions')}</TableCell>}
            </TableRow>
          </TableHead>
          <TableBody>
            {roles.length === 0 ? (
              <TableRow>
                <TableCell colSpan={3} align="center" sx={{ py: 4, color: 'text.secondary', border: 0 }}>
                  {t('roles_no_users')}
                </TableCell>
              </TableRow>
            ) : roles.map((row) => (
              <TableRow key={row.email || row.userID} hover>
                <TableCell sx={{ borderBottom: '1px solid #F4F4F4', py: 1.2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Typography variant="body2" fontWeight={500}>{row.email}</Typography>
                    {row.email === myEmail && (
                      <Chip label={t('roles_you')} size="small" sx={{
                        height: 18, fontSize: '0.68rem', borderRadius: '3px',
                        bgcolor: '#EBF2FF', color: 'primary.main', border: '1px solid #BAE0F7',
                      }} />
                    )}
                  </Box>
                </TableCell>
                <TableCell sx={{ borderBottom: '1px solid #F4F4F4', py: 1.2 }}>
                  {editingEmail === row.email ? (
                    <Select size="small" value={editRole} onChange={(e) => setEditRole(e.target.value)} sx={{ minWidth: 100, fontSize: '0.8rem' }}>
                      {ROLES.filter(r => r !== 'OWNER').map(r => <MenuItem key={r} value={r}>{r}</MenuItem>)}
                    </Select>
                  ) : <RoleChip role={row.lockRole} />}
                </TableCell>
                {canManage && (
                  <TableCell align="right" sx={{ borderBottom: '1px solid #F4F4F4', py: 1.2 }}>
                    <Box sx={{ display: 'flex', gap: 0.5, justifyContent: 'flex-end' }}>
                      {editingEmail === row.email ? (
                        <>
                          <IconButton size="small" onClick={handleEditConfirm} sx={{ color: 'success.main' }}><CheckIcon fontSize="small" /></IconButton>
                          <IconButton size="small" onClick={() => setEditingEmail(null)}><CloseIcon fontSize="small" /></IconButton>
                        </>
                      ) : (
                        <>
                          {row.lockRole !== 'OWNER' && row.email !== myEmail && (
                            <Tooltip title={t('roles_change_role')}>
                              <IconButton size="small" onClick={() => { setEditingEmail(row.email); setEditRole(row.lockRole); }}>
                                <EditIcon fontSize="small" />
                              </IconButton>
                            </Tooltip>
                          )}
                          {row.email !== myEmail && row.lockRole !== 'OWNER' && (
                            <Tooltip title={t('roles_remove')}>
                              <IconButton size="small" onClick={() => onRemove(row.email)} sx={{ '&:hover': { color: 'error.main' } }}>
                                <DeleteIcon fontSize="small" />
                              </IconButton>
                            </Tooltip>
                          )}
                        </>
                      )}
                    </Box>
                  </TableCell>
                )}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}
