import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function PrivateRoute({ children }) {
  const { user, loading } = useAuth();

  if (loading) {
    return <div className="min-h-screen flex items-center justify-center">
      <div className="text-xl">Loading...</div>
    </div>;
  }

  return user ? children : <Navigate to="/login" />;
}
