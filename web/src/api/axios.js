import axios from 'axios';

const api = axios.create({
  // Empty baseURL → uses Vite dev-server proxy for /api/* in dev,
  // and the same origin in production (serve via nginx / Spring Boot static).
  // Override by setting VITE_API_URL only if NOT using the proxy.
  baseURL: '',
  headers: { 'Content-Type': 'application/json' },
});

// Attach JWT to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('jwt_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// On 401 → redirect to login
api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.clear();
      window.location.href = '/login';
    }
    return Promise.reject(err);
  }
);

export default api;
