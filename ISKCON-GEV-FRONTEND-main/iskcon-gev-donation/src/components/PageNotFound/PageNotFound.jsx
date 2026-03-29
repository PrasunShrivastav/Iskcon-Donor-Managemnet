import React from 'react';
import { Box, Typography, Button, useTheme } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Player } from '@lottiefiles/react-lottie-player';
import NotFoundAnimation from '../../assets/404-lottie.json'; // 🧸 Add your own Lottie JSON here

export default function PageNotFound() {
  const navigate = useNavigate();
  const theme = useTheme();

  return (
    <Box
      sx={{
        minHeight: '100vh',
        background: theme.palette.mode === 'dark'
          ? 'linear-gradient(135deg, #0f0f0f, #1a1a1a)'
          : 'linear-gradient(135deg, #e0eafc, #cfdef3)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        flexDirection: 'column',
        backdropFilter: 'blur(12px)',
        px: 3,
      }}
    >

      {/* Lottie Animation */}
      <Player
        autoplay
        loop
        src={NotFoundAnimation}
        style={{ height: '250px', width: '250px', marginTop: '-20px' }}
      />

      {/* Message */}
      <Typography variant="h5" sx={{ mb: 1, color: theme.palette.text.primary }}>
        Oops! Page Not Found
      </Typography>
      <Typography variant="body1" sx={{ maxWidth: 500, mb: 4, color: theme.palette.text.secondary }}>
        The page you're looking for doesn’t exist or has been moved. Let’s get you back on track!
      </Typography>

      {/* Go Home Button */}
      <Button
        variant="outlined"
        onClick={() => navigate('/dashboard')}
        sx={{
          borderRadius: 5,
          px: 4,
          py: 1.5,
          fontWeight: 'bold',
          borderColor: theme.palette.primary.main,
          color: theme.palette.primary.main,
          '&:hover': {
            backgroundColor: theme.palette.primary.main,
            color: '#fff',
          },
        }}
      >
        Back to Dashboard
      </Button>
    </Box>
  );
}
