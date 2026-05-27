import { createContext, useContext, useState, useCallback } from 'react';
import * as api from '../api/smartlockApi';

const AuthContext = createContext(null);

const TOKEN_KEY = 'jwt_token';
const USER_KEY = 'smartlock_user';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const stored = localStorage.getItem(USER_KEY);
      return stored ? JSON.parse(stored) : null;
    } catch {
      return null;
    }
  });

  const persistSession = (jwtToken, userData) => {
    localStorage.setItem(TOKEN_KEY, jwtToken);
    localStorage.setItem(USER_KEY, JSON.stringify(userData));
    setUser(userData);
  };

  const loginUser = useCallback(async (email, password) => {
    const res = await api.login(email, password);
    const { jwtToken, id, email: userEmail, fullName } = res.data;
    persistSession(jwtToken, { id, email: userEmail, fullName });
    return res.data;
  }, []);

  const registerUser = useCallback(async (email, password, fullName) => {
    const res = await api.register(email, password, fullName);
    const { jwtToken, id, email: userEmail, fullName: fn } = res.data;
    persistSession(jwtToken, { id, email: userEmail, fullName: fn });
    return res.data;
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    setUser(null);
  }, []);

  const isAuthenticated = Boolean(user && localStorage.getItem(TOKEN_KEY));

  return (
    <AuthContext.Provider value={{ user, isAuthenticated, loginUser, registerUser, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
};
