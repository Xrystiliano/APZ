import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import {
  Box, Card, CardContent, TextField, Button, Typography,
  InputAdornment, IconButton, Stack, Divider, Alert,
} from '@mui/material';
import LockIcon from '@mui/icons-material/Lock';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import { useAuth } from '../context/AuthContext';
import { useLang } from '../context/LanguageContext';

export default function LoginPage() {
  const { loginUser } = useAuth();
  const { t, lang, switchLang } = useLang();
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPw, setShowPw] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!email || !password) { setError(t('auth_error_empty')); return; }
    setLoading(true); setError('');
    try {
      await loginUser(email, password);
      navigate('/');
    } catch {
      setError(t('auth_error_invalid'));
    } finally { setLoading(false); }
  };

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: '#F4F4F4', display: 'flex', alignItems: 'center', justifyContent: 'center', p: 2 }}>
      <Box sx={{ width: '100%', maxWidth: 400 }}>
        {/* Header row: logo + lang switcher */}
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 4 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
            <Box sx={{ width: 36, height: 36, borderRadius: '8px', bgcolor: 'primary.main', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <LockIcon sx={{ fontSize: 20, color: '#fff' }} />
            </Box>
            <Typography fontWeight={700} fontSize="1.1rem" color="text.primary">SmartLock</Typography>
          </Box>
          <Box sx={{ display: 'flex', gap: 0.5 }}>
            {['en', 'uk'].map(l => (
              <Box
                key={l}
                onClick={() => switchLang(l)}
                sx={{
                  px: 1.2, py: 0.4, borderRadius: '5px', cursor: 'pointer',
                  fontSize: '0.72rem', fontWeight: 700, letterSpacing: '0.05em',
                  border: '1px solid',
                  borderColor: lang === l ? 'primary.main' : '#E0E0E0',
                  bgcolor: lang === l ? 'primary.main' : '#fff',
                  color: lang === l ? '#fff' : 'text.secondary',
                  transition: 'all 0.15s',
                  '&:hover': { borderColor: 'primary.main', color: lang === l ? '#fff' : 'primary.main' },
                }}
              >
                {l.toUpperCase()}
              </Box>
            ))}
          </Box>
        </Box>

        <Card elevation={0}>
          <CardContent sx={{ p: 3.5 }}>
            <Typography variant="h5" fontWeight={700} sx={{ mb: 0.5 }}>{t('auth_signin_title')}</Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>{t('auth_signin_subtitle')}</Typography>

            {error && <Alert severity="error" sx={{ mb: 2.5, fontSize: '0.85rem' }}>{error}</Alert>}

            <form onSubmit={handleSubmit}>
              <Stack spacing={2}>
                <TextField label={t('auth_email')} type="email" value={email} onChange={(e) => setEmail(e.target.value)} fullWidth autoFocus size="small" />
                <TextField
                  label={t('auth_password')}
                  type={showPw ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  fullWidth size="small"
                  slotProps={{
                    input: {
                      endAdornment: (
                        <InputAdornment position="end">
                          <IconButton onClick={() => setShowPw(!showPw)} size="small" edge="end">
                            {showPw ? <VisibilityOffIcon fontSize="small" /> : <VisibilityIcon fontSize="small" />}
                          </IconButton>
                        </InputAdornment>
                      ),
                    },
                  }}
                />
                <Button type="submit" variant="contained" size="medium" fullWidth disabled={loading} sx={{ mt: 0.5, py: 1.1 }}>
                  {loading ? t('auth_signin_loading') : t('auth_signin_btn')}
                </Button>
              </Stack>
            </form>

            <Divider sx={{ my: 2.5 }}>
              <Typography variant="caption" color="text.secondary">{t('auth_new_to')}</Typography>
            </Divider>
            <Button component={Link} to="/register" variant="outlined" fullWidth size="medium">{t('auth_create_account')}</Button>
          </CardContent>
        </Card>
      </Box>
    </Box>
  );
}
