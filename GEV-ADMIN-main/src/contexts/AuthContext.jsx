import { createContext, useState, useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const token = Cookies.get("jwt");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        console.log("[AuthContext] Decoded token on load:", decoded);
        setUser(decoded);
      } catch (err) {
        console.error("[AuthContext] Invalid token", err);
        setUser(null);
      }
    }
    setIsLoading(false);
  }, []);

  const login = (userData) => {
    setUser(userData);
    navigate("/dashboard");
  };

  const logout = () => {
    setUser(null);
    Cookies.remove("jwt");
    navigate("/login");
  };

  return (  
    <AuthContext.Provider value={{ user, login, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
