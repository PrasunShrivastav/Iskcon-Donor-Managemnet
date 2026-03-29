import React, { useState, useMemo } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Outlet } from 'react-router-dom';
import {
  Box,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Typography,
  Toolbar,
  AppBar,
  CssBaseline,
  ThemeProvider,
  createTheme,
  Switch,
  IconButton,
} from '@mui/material';
import Brightness4Icon from '@mui/icons-material/Brightness4';
import Brightness7Icon from '@mui/icons-material/Brightness7';

const drawerWidth = 240;

export default function Dashboard() {
  const [darkMode, setDarkMode] = useState(true);

  const theme = useMemo(
    () =>
      createTheme({
        palette: {
          mode: darkMode ? 'dark' : 'light',
          background: {
            default: darkMode ? '#121212' : '#f4f6f8',
            paper: darkMode ? '#1e1e1e' : '#fff',
          },
        },
        components: {
          MuiDrawer: {
            styleOverrides: {
              paper: {
                backgroundColor: darkMode ? '#1f1f1f' : '#fff',
              },
            },
          },
        },
      }),
    [darkMode]
  );

const location = useLocation();
const navigate = useNavigate();

const navItems = [
  { label: 'Dashboard', path: '/dashboard' },
  { label: 'Donors', path: '/dashboard/donors' },
  { label: 'Donations', path: '/dashboard/donations' },
  { label: 'Causes', path: '/dashboard/causes' },
  { label: 'Category', path: '/dashboard/category' },
  { label: 'Options', path: '/dashboard/options' },
  { label: 'Options Group', path: '/dashboard/options-group' },
  { label: 'Settings', path: '/dashboard/settings' }
];

  return (
    <ThemeProvider theme={theme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />

        {/* Top App Bar */}
        <AppBar position="fixed" sx={{ zIndex: (t) => t.zIndex.drawer + 1 }}>
        <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
  {/* Left Side: Logo and Title */}
  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
    <img
      src="https://iskcongev.com/wp-content/uploads/2022/11/Logo-GEV-07.png"
      alt="ISKCON GEV LOGO"
      width={40}
      height={40}
    />
    <Typography variant="h6" noWrap>
      Donation Admin Panel
    </Typography>
  </Box>

  {/* Right Side: Dark Mode Toggle */}
  <Box sx={{ display: 'flex', alignItems: 'center' }}>
    <IconButton color="inherit" onClick={() => setDarkMode(!darkMode)}>
      {darkMode ? <Brightness7Icon /> : <Brightness4Icon />}
    </IconButton>
    <Switch
      checked={darkMode}
      onChange={() => setDarkMode(!darkMode)}
      color="default"
    />
  </Box>
</Toolbar>

        </AppBar>

        {/* Sidebar Drawer */}
        <Drawer
          variant="permanent"
          sx={{
            width: drawerWidth,
            flexShrink: 0,
            [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
          }}
        >
          <Toolbar />
          <Box sx={{ overflow: 'auto' }}>
          <List>
          {navItems.map(({ label, path }) => (
            <ListItem key={label} disablePadding>
              <ListItemButton
                onClick={() => navigate(path)}
                selected={location.pathname === path}
                sx={{
                  '&.Mui-selected': {
                    backgroundColor: 'primary.main',
                    color: '#fff',
                    '&:hover': {
                      backgroundColor: 'primary.dark',
                    },
                  },
                }}
              >
        <ListItemText primary={label} />
      </ListItemButton>
    </ListItem>
  ))}
</List>

          </Box>
        </Drawer>

        {/* Main Content */}
        <Box
          component="main"
          sx={{
            flexGrow: 1,
            p: 3,
            minHeight: '100vh',
            backgroundColor: theme.palette.background.default,
            color: theme.palette.text.primary,
          }}
        >
          <Toolbar />
          {/* <Typography variant="h4">Welcome, Admin</Typography>
          <Typography variant="body1" sx={{ mt: 2 }}>
            Select a section from the sidebar to manage your donation platform.
          </Typography> */}
          <Outlet /> 
        </Box>
      </Box>
    </ThemeProvider>
  );
}