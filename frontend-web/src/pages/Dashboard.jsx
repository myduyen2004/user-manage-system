import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { userAPI } from '../services/api';

export default function Dashboard() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('ALL');
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    fetchUsers();
  }, [filter]);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      let response;
      
      if (filter === 'ALL') {
        response = await userAPI.getAllUsers();
      } else {
        response = await userAPI.getUsersByRole(filter);
      }
      
      setUsers(response.data.data);
    } catch (error) {
      console.error('Error fetching users:', error);
      if (error.response?.status === 403) {
        alert('You do not have permission to view users');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this user?')) return;
    
    try {
      await userAPI.deleteUser(id);
      alert('User deleted successfully');
      fetchUsers();
    } catch (error) {
      alert('Failed to delete user');
    }
  };

  const handleToggleStatus = async (id) => {
    try {
      await userAPI.toggleUserStatus(id);
      alert('User status updated');
      fetchUsers();
    } catch (error) {
      alert('Failed to update status');
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header */}
      <header className="bg-white shadow">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-gray-800">Dashboard</h1>
          <div className="flex items-center gap-4">
            <div className="text-right">
              <p className="text-sm text-gray-600">Welcome,</p>
              <p className="font-semibold text-gray-800">{user?.fullName}</p>
              <p className="text-xs text-blue-600">{user?.role}</p>
            </div>
            <button
              onClick={handleLogout}
              className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition"
            >
              Logout
            </button>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-4 py-8">
        {/* Filters */}
        {user?.role === 'ADMIN' && (
          <div className="bg-white rounded-lg shadow p-4 mb-6">
            <div className="flex gap-4">
              <button
                onClick={() => setFilter('ALL')}
                className={`px-4 py-2 rounded ${
                  filter === 'ALL' ? 'bg-blue-600 text-white' : 'bg-gray-200'
                }`}
              >
                All Users
              </button>
              <button
                onClick={() => setFilter('ADMIN')}
                className={`px-4 py-2 rounded ${
                  filter === 'ADMIN' ? 'bg-blue-600 text-white' : 'bg-gray-200'
                }`}
              >
                Admins
              </button>
              <button
                onClick={() => setFilter('LECTURER')}
                className={`px-4 py-2 rounded ${
                  filter === 'LECTURER' ? 'bg-blue-600 text-white' : 'bg-gray-200'
                }`}
              >
                Lecturers
              </button>
              <button
                onClick={() => setFilter('STUDENT')}
                className={`px-4 py-2 rounded ${
                  filter === 'STUDENT' ? 'bg-blue-600 text-white' : 'bg-gray-200'
                }`}
              >
                Students
              </button>
            </div>
          </div>
        )}

        {/* Users Table */}
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="w-full">
            <thead className="bg-gray-800 text-white">
              <tr>
                <th className="px-4 py-3 text-left">ID</th>
                <th className="px-4 py-3 text-left">Username</th>
                <th className="px-4 py-3 text-left">Full Name</th>
                <th className="px-4 py-3 text-left">Email</th>
                <th className="px-4 py-3 text-left">Role</th>
                <th className="px-4 py-3 text-left">Status</th>
                {user?.role === 'ADMIN' && <th className="px-4 py-3 text-left">Actions</th>}
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="7" className="text-center py-8">
                    Loading...
                  </td>
                </tr>
              ) : users.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-8">
                    No users found
                  </td>
                </tr>
              ) : (
                users.map((u) => (
                  <tr key={u.id} className="border-b hover:bg-gray-50">
                    <td className="px-4 py-3">{u.id}</td>
                    <td className="px-4 py-3">{u.username}</td>
                    <td className="px-4 py-3">{u.fullName}</td>
                    <td className="px-4 py-3">{u.email}</td>
                    <td className="px-4 py-3">
                      <span className={`px-2 py-1 rounded text-xs font-semibold ${
                        u.role === 'ADMIN' ? 'bg-red-100 text-red-800' :
                        u.role === 'LECTURER' ? 'bg-blue-100 text-blue-800' :
                        'bg-green-100 text-green-800'
                      }`}>
                        {u.role}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <span className={`px-2 py-1 rounded text-xs font-semibold ${
                        u.active ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                      }`}>
                        {u.active ? 'Active' : 'Inactive'}
                      </span>
                    </td>
                    {user?.role === 'ADMIN' && (
                      <td className="px-4 py-3">
                        <button
                          onClick={() => handleToggleStatus(u.id)}
                          className="bg-yellow-500 text-white px-3 py-1 rounded text-sm mr-2 hover:bg-yellow-600"
                        >
                          Toggle
                        </button>
                        <button
                          onClick={() => handleDelete(u.id)}
                          className="bg-red-500 text-white px-3 py-1 rounded text-sm hover:bg-red-600"
                        >
                          Delete
                        </button>
                      </td>
                    )}
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </main>
    </div>
  );
}
