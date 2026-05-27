import { Snackbar, Alert } from '@mui/material';

export default function ErrorSnackbar({ message, onClose }) {
  return (
    <Snackbar
      open={Boolean(message)}
      autoHideDuration={4000}
      onClose={onClose}
      anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
    >
      <Alert onClose={onClose} severity="error" variant="filled" sx={{ width: '100%', fontSize: '0.875rem' }}>
        {message}
      </Alert>
    </Snackbar>
  );
}
