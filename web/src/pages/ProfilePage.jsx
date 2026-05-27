import {
  Box, Typography, Card, CardContent, Avatar, Divider, Chip,
} from '@mui/material';
import PersonIcon from '@mui/icons-material/Person';
import EmailIcon from '@mui/icons-material/Email';
import LockIcon from '@mui/icons-material/Lock';
import { useAuth } from '../context/AuthContext';
import { useLang } from '../context/LanguageContext';

function InfoRow({ icon, label, value }) {
  return (
    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, py: 1.75 }}>
      <Box sx={{
        width: 36, height: 36, borderRadius: '8px', flexShrink: 0,
        bgcolor: '#EBF2FF', border: '1px solid #BAE0F7',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
      }}>
        {icon}
      </Box>
      <Box>
        <Typography variant="caption" color="text.secondary" fontWeight={600} sx={{ textTransform: 'uppercase', letterSpacing: '0.05em', fontSize: '0.7rem' }}>
          {label}
        </Typography>
        <Typography variant="body2" fontWeight={500} sx={{ mt: 0.1 }}>{value || '—'}</Typography>
      </Box>
    </Box>
  );
}

export default function ProfilePage() {
  const { user } = useAuth();
  const { t } = useLang();

  const initials = user?.fullName
    ? user.fullName.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)
    : user?.email?.[0]?.toUpperCase() || 'U';

  return (
    <Box sx={{ maxWidth: 560 }}>
      <Typography variant="h5" fontWeight={700} sx={{ mb: 0.25 }}>{t('profile_title')}</Typography>
      <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>{t('profile_subtitle')}</Typography>

      <Card elevation={0}>
        <CardContent sx={{ p: 3 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2.5, mb: 3 }}>
            <Avatar sx={{ width: 64, height: 64, fontSize: '1.4rem', fontWeight: 700, bgcolor: 'primary.main', color: '#fff', borderRadius: '12px' }}>
              {initials}
            </Avatar>
            <Box>
              <Typography variant="h6" fontWeight={700}>{user?.fullName || 'User'}</Typography>
              <Chip label={t('profile_active')} size="small" sx={{
                mt: 0.5, height: 20, fontSize: '0.7rem', fontWeight: 600,
                borderRadius: '4px', bgcolor: '#DEFBE6', color: '#198038', border: '1px solid #A7F0BA',
              }} />
            </Box>
          </Box>

          <Divider />
          <InfoRow icon={<PersonIcon sx={{ fontSize: 18, color: 'primary.main' }} />} label={t('profile_full_name')} value={user?.fullName} />
          <Divider />
          <InfoRow icon={<EmailIcon sx={{ fontSize: 18, color: 'primary.main' }} />} label={t('profile_email')} value={user?.email} />
          <Divider />
          <InfoRow icon={<LockIcon sx={{ fontSize: 18, color: 'primary.main' }} />} label={t('profile_user_id')} value={user?.id} />
        </CardContent>
      </Card>
    </Box>
  );
}
