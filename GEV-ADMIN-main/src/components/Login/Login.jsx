import {useState,useEffect} from 'react';
import {
  Box,
  Button,
  CssBaseline,
  TextField,
  Typography,
  Paper,
  FormControlLabel,
  Checkbox,
  Link,
  Stack,
} from '@mui/material';
import './login.css'
import GEVImage from '../../assets/iskcon-gev-admin.jpg';
import {loginUser} from '../api/auth';
import { useAuth } from "../../contexts/AuthContext";

export default function Login() {

  const { login } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      console.log("isnide login");
      console.log(email);
      console.log(password);

      const res = await loginUser(email, password);
      setError(null);
      login(res.data);
    } catch (err) {
      setError('Invalid credentials');
      console.error('Login failed:', err);
    }
  };

  return (
    <>
      <CssBaseline />
      <Box
        sx={{
          height: '100vh',
          width: '100vw',
          backgroundImage: `url(${GEVImage})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          padding: 2,
        }}
      >
        <Paper
          elevation={6}
          sx={{
            width: '100%',
            maxWidth: 600,
            padding: 4,
            borderRadius: 4,
            textAlign: 'center',
            backgroundColor: 'rgba(0, 0, 0, 0.1)',
            backdropFilter: 'blur(30px)',
            WebkitBackdropFilter: 'blur(30px)',
            color: 'white',
            boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
          }}
        >
          <Typography variant="h4" sx={{ mb: 1}}>
          <img src="https://iskcongev.com/wp-content/uploads/2022/11/Logo-GEV-07.png" alt="ISKCON GEV LOGO"  />
          </Typography>
            <Typography variant="h4" sx={{ mb: 1, color: 'white', fontFamily: "Bebas Neue, sans-serif",  letterSpacing: '2px', }}>
            ADMIN
          </Typography>

          <Box component="form" onSubmit={handleSubmit} noValidate>
            <TextField
              fullWidth
              label="Email"
              type="email"
              margin="normal"
              variant="outlined"
              value={email} onChange={(e) => setEmail(e.target.value)}
            sx={{backgroundColor:'rgba(255, 255, 255, 0.4)', borderRadius: '5px',}}
            />
            <TextField
              fullWidth
              label="Password"
              type="password"
              margin="normal"
              variant="outlined"
              value={password} onChange={(e) => setPassword(e.target.value)}
            sx={{backgroundColor:'rgba(255, 255, 255, 0.4)', borderRadius: '5px',}}
            />

            {error && <Typography
            sx={{fontSize:18, fontWeight:'bold', textShadow:'2px'}}
            color="error">{error}</Typography>}

            <Box
              sx={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                mt: 1,
              }}
            >
              <FormControlLabel
                control={<Checkbox color="primary" />}
                label="Remember me"
                sx={{
                    '& .MuiFormControlLabel-label': {
                      fontWeight: 'normal',
                }
                }}
              />
              <Link href="#" sx={{underline:"hover", fontSize:"1rem", fontWeight:"bold"}}>
                Forgot password?
              </Link>
            </Box>

            <Button
              fullWidth
              variant="contained"
              type='submit'
              sx={{
                mt: 3,
                backgroundColor: 'rgba(40, 222, 64, 0.5)',
                fontFamily: "Bebas Neue, sans-serif",
                fontSize: '20px',
                letterSpacing: '2px',
                '&:hover': {
                backgroundColor: '#1366d6',
               
                },
              }}
            >
              LOGIN
            </Button>
          </Box>

          <Stack direction="row" justifyContent="center" mt={3}>
            <Typography variant="body2" sx={{ mr: 1 }}>
              Don’t have an account?
            </Typography>
            <Link href="#" underline="hover" fontSize="0.9rem">
              Register
            </Link>
          </Stack>
        </Paper>
      </Box>
    </>
  );
}
