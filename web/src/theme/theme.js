import { createTheme } from '@mui/material/styles';

// SmartLock minimal theme — white / blue / black
// Matches the mobile client palette:
//   Primary:     #0F62FE  (IBM Blue)
//   Background:  #FFFFFF
//   Surface:     #F4F4F4
//   Text:        #161616
//   Secondary text: #6E7E91
//   Border:      #E0E0E0
//   Error:       #DA1E28

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#0F62FE',
      light: '#4589FF',
      dark: '#0043CE',
      contrastText: '#FFFFFF',
    },
    secondary: {
      main: '#6E7E91',
      contrastText: '#FFFFFF',
    },
    background: {
      default: '#F4F4F4',
      paper: '#FFFFFF',
    },
    text: {
      primary: '#161616',
      secondary: '#6E7E91',
    },
    divider: '#E0E0E0',
    error: { main: '#DA1E28' },
    success: { main: '#198038' },
    warning: { main: '#F1C21B' },
  },
  typography: {
    fontFamily: "'Inter', 'IBM Plex Sans', 'Roboto', sans-serif",
    h4: { fontWeight: 700, letterSpacing: '-0.02em', color: '#161616' },
    h5: { fontWeight: 700, letterSpacing: '-0.01em', color: '#161616' },
    h6: { fontWeight: 600, color: '#161616' },
    body1: { color: '#161616' },
    body2: { color: '#6E7E91' },
    button: { fontWeight: 600, letterSpacing: '0.01em', textTransform: 'none' },
    subtitle2: { fontWeight: 600, color: '#161616' },
    caption: { color: '#6E7E91' },
  },
  shape: { borderRadius: 8 },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          backgroundColor: '#F4F4F4',
          color: '#161616',
          scrollbarWidth: 'thin',
          scrollbarColor: '#C6C6C6 transparent',
        },
      },
    },
    MuiButton: {
      defaultProps: { disableElevation: true },
      styleOverrides: {
        root: {
          borderRadius: 6,
          padding: '10px 20px',
          fontWeight: 600,
          transition: 'all 0.15s ease',
        },
        contained: {
          '&:hover': { backgroundColor: '#0043CE' },
        },
        outlined: {
          borderColor: '#E0E0E0',
          color: '#161616',
          '&:hover': { borderColor: '#0F62FE', color: '#0F62FE', backgroundColor: '#EBF2FF' },
        },
        text: {
          color: '#0F62FE',
          '&:hover': { backgroundColor: '#EBF2FF' },
        },
      },
    },
    MuiCard: {
      defaultProps: { elevation: 0 },
      styleOverrides: {
        root: {
          backgroundColor: '#FFFFFF',
          border: '1px solid #E0E0E0',
          borderRadius: 10,
          transition: 'border-color 0.15s ease, box-shadow 0.15s ease',
          '&:hover': {
            borderColor: '#C6C6C6',
            boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
          },
        },
      },
    },
    MuiPaper: {
      defaultProps: { elevation: 0 },
      styleOverrides: {
        root: {
          backgroundColor: '#FFFFFF',
          border: '1px solid #E0E0E0',
        },
      },
    },
    MuiTextField: {
      defaultProps: { variant: 'outlined', size: 'small' },
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            backgroundColor: '#FFFFFF',
            borderRadius: 6,
            '& fieldset': { borderColor: '#E0E0E0' },
            '&:hover fieldset': { borderColor: '#A8A8A8' },
            '&.Mui-focused fieldset': { borderColor: '#0F62FE', borderWidth: 2 },
          },
          '& .MuiInputLabel-root': { color: '#6E7E91' },
          '& .MuiInputLabel-root.Mui-focused': { color: '#0F62FE' },
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          borderRadius: 4,
          fontWeight: 600,
          fontSize: '0.75rem',
        },
      },
    },
    MuiAppBar: {
      defaultProps: { elevation: 0 },
      styleOverrides: {
        root: {
          backgroundColor: '#FFFFFF',
          borderBottom: '1px solid #E0E0E0',
          color: '#161616',
        },
      },
    },
    MuiTab: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          fontWeight: 600,
          fontSize: '0.875rem',
          color: '#6E7E91',
          '&.Mui-selected': { color: '#0F62FE' },
        },
      },
    },
    MuiTabs: {
      styleOverrides: {
        indicator: { backgroundColor: '#0F62FE', height: 2 },
      },
    },
    MuiTableCell: {
      styleOverrides: {
        head: { fontWeight: 600, color: '#6E7E91', fontSize: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.05em' },
      },
    },
    MuiDivider: {
      styleOverrides: { root: { borderColor: '#E0E0E0' } },
    },
    MuiIconButton: {
      styleOverrides: {
        root: {
          borderRadius: 6,
          '&:hover': { backgroundColor: '#F4F4F4' },
        },
      },
    },
    MuiTooltip: {
      styleOverrides: {
        tooltip: {
          backgroundColor: '#161616',
          fontSize: '0.75rem',
          borderRadius: 4,
        },
      },
    },
    MuiDialog: {
      styleOverrides: {
        paper: {
          borderRadius: 12,
          border: '1px solid #E0E0E0',
          boxShadow: '0 8px 40px rgba(0,0,0,0.12)',
        },
      },
    },
    MuiSnackbar: {
      styleOverrides: { root: { borderRadius: 8 } },
    },
    MuiAlert: {
      styleOverrides: {
        root: { borderRadius: 6, border: '1px solid' },
        standardError: { borderColor: '#FCCDC9', backgroundColor: '#FFF1F1' },
        standardInfo: { borderColor: '#BAE0F7', backgroundColor: '#EDF5FF' },
        standardSuccess: { borderColor: '#A7F0BA', backgroundColor: '#DEFBE6' },
      },
    },
    MuiSkeleton: {
      styleOverrides: {
        root: { backgroundColor: '#E8E8E8', borderRadius: 6 },
      },
    },
    MuiSelect: {
      styleOverrides: {
        root: {
          borderRadius: 6,
          backgroundColor: '#FFFFFF',
        },
      },
    },
  },
});

export default theme;
