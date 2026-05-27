import { useState } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import {
  AppBar, Toolbar, Typography, IconButton, Box, Avatar,
  Menu, MenuItem, ListItemIcon, Divider, Container, ToggleButtonGroup, ToggleButton,
} from '@mui/material';
import LockIcon from '@mui/icons-material/Lock';
import PersonIcon from '@mui/icons-material/Person';
import LogoutIcon from '@mui/icons-material/Logout';
import HomeIcon from '@mui/icons-material/Home';
import { useAuth } from '../../context/AuthContext';
import { useLang } from '../../context/LanguageContext';

export default function AppLayout() {
  const { user, logout } = useAuth();
  const { lang, switchLang, t } = useLang();
  const navigate = useNavigate();
  const location = useLocation();
  const [anchorEl, setAnchorEl] = useState(null);

  const initials = user?.fullName
    ? user.fullName.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)
    : user?.email?.[0]?.toUpperCase() || 'U';

  const handleLogout = () => { setAnchorEl(null); logout(); navigate('/login'); };

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'background.default', display: 'flex', flexDirection: 'column' }}>
      <AppBar position="sticky">
        <Container maxWidth="lg">
          <Toolbar disableGutters sx={{ gap: 1, minHeight: 56 }}>
            {/* Logo */}
            <Box
              onClick={() => navigate('/')}
              sx={{ display: 'flex', alignItems: 'center', gap: 1, cursor: 'pointer', mr: 2 }}
            >
              <Box sx={{
                width: 30, height: 30, borderRadius: '7px',
                bgcolor: 'primary.main',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                flexShrink: 0,
              }}>
                <LockIcon sx={{ fontSize: 16, color: '#fff' }} />
              </Box>
              <Typography fontWeight={700} fontSize="0.95rem" color="text.primary">
                SmartLock
              </Typography>
            </Box>

            {/* Nav link */}
            <Box
              onClick={() => navigate('/')}
              sx={{
                display: 'flex', alignItems: 'center', gap: 0.5,
                px: 1.5, py: 0.75, borderRadius: 1, cursor: 'pointer',
                color: location.pathname === '/' ? 'primary.main' : 'text.secondary',
                bgcolor: location.pathname === '/' ? '#EBF2FF' : 'transparent',
                fontSize: '0.875rem', fontWeight: 600,
                '&:hover': { bgcolor: '#F4F4F4' },
                transition: 'all 0.15s ease',
              }}
            >
              <HomeIcon fontSize="small" />
              {t('nav_locks')}
            </Box>

            <Box sx={{ flex: 1 }} />

            {/* Language switcher */}
            <ToggleButtonGroup
              value={lang}
              exclusive
              onChange={(_, val) => val && switchLang(val)}
              size="small"
              sx={{
                height: 30,
                '& .MuiToggleButton-root': {
                  px: 1.25, py: 0,
                  fontSize: '0.72rem',
                  fontWeight: 700,
                  letterSpacing: '0.05em',
                  border: '1px solid #E0E0E0',
                  color: 'text.secondary',
                  textTransform: 'none',
                  lineHeight: 1,
                  '&.Mui-selected': {
                    bgcolor: 'primary.main',
                    color: '#fff',
                    borderColor: 'primary.main',
                    '&:hover': { bgcolor: 'primary.dark' },
                  },
                  '&:hover': { bgcolor: '#F4F4F4' },
                },
              }}
            >
              <ToggleButton value="en">EN</ToggleButton>
              <ToggleButton value="uk">UK</ToggleButton>
            </ToggleButtonGroup>

            {/* Avatar menu */}
            <IconButton
              onClick={(e) => setAnchorEl(e.currentTarget)}
              size="small"
              sx={{ p: 0, ml: 0.5 }}
            >
              <Avatar sx={{
                width: 32, height: 32, fontSize: '0.8rem', fontWeight: 700,
                bgcolor: 'primary.main', color: '#fff',
              }}>
                {initials}
              </Avatar>
            </IconButton>

            <Menu
              anchorEl={anchorEl}
              open={Boolean(anchorEl)}
              onClose={() => setAnchorEl(null)}
              transformOrigin={{ horizontal: 'right', vertical: 'top' }}
              anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
              slotProps={{
                paper: {
                  sx: { mt: 1, minWidth: 180, border: '1px solid #E0E0E0', boxShadow: '0 4px 20px rgba(0,0,0,0.1)', borderRadius: 2 },
                },
              }}
            >
              <Box sx={{ px: 2, py: 1.5 }}>
                <Typography variant="subtitle2" fontWeight={700} noWrap>{user?.fullName || 'User'}</Typography>
                <Typography variant="caption" color="text.secondary" noWrap>{user?.email}</Typography>
              </Box>
              <Divider />
              <MenuItem onClick={() => { setAnchorEl(null); navigate('/profile'); }} sx={{ gap: 1.5, py: 1 }}>
                <ListItemIcon sx={{ minWidth: 'auto' }}><PersonIcon fontSize="small" /></ListItemIcon>
                <Typography variant="body2">{t('nav_profile')}</Typography>
              </MenuItem>
              <Divider />
              <MenuItem onClick={handleLogout} sx={{ gap: 1.5, py: 1 }}>
                <ListItemIcon sx={{ minWidth: 'auto', color: 'error.main' }}><LogoutIcon fontSize="small" /></ListItemIcon>
                <Typography variant="body2" color="error.main">{t('nav_signout')}</Typography>
              </MenuItem>
            </Menu>
          </Toolbar>
        </Container>
      </AppBar>

      <Container maxWidth="lg" sx={{ py: 4, flex: 1 }}>
        <Outlet />
      </Container>
    </Box>
  );
}
