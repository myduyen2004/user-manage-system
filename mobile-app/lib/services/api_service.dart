import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/user.dart';

class ApiService {
  // QUAN TRỌNG: Đổi baseUrl khi chạy thật
  // Android Emulator: http://10.0.2.2:8080/api
  // iOS Simulator: http://localhost:8080/api
  // Real device: http://YOUR_COMPUTER_IP:8080/api
  static const String baseUrl = 'http://10.0.2.2:8080/api';
  
  String? _token;

  // Load token từ storage
  Future<void> loadToken() async {
    final prefs = await SharedPreferences.getInstance();
    _token = prefs.getString('token');
  }

  // Save token
  Future<void> saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('token', token);
    _token = token;
  }

  // Clear token (logout)
  Future<void> clearToken() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('token');
    _token = null;
  }

  // Headers với token
  Map<String, String> get headers {
    final Map<String, String> header = {
      'Content-Type': 'application/json',
    };
    if (_token != null) {
      header['Authorization'] = 'Bearer $_token';
    }
    return header;
  }

  // ==================== AUTH APIs ====================

  // Register
  Future<Map<String, dynamic>> register({
    required String username,
    required String email,
    required String password,
    required String fullName,
    required String role,
  }) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/register'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'username': username,
          'email': email,
          'password': password,
          'fullName': fullName,
          'role': role,
        }),
      );

      if (response.statusCode == 200 || response.statusCode == 201) {
        final data = jsonDecode(response.body);
        await saveToken(data['token']);
        return {'success': true, 'data': data};
      } else {
        final error = jsonDecode(response.body);
        return {'success': false, 'error': error['message'] ?? 'Registration failed'};
      }
    } catch (e) {
      return {'success': false, 'error': 'Network error: $e'};
    }
  }

  // Login
  Future<Map<String, dynamic>> login({
    required String username,
    required String password,
  }) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/login'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'username': username,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await saveToken(data['token']);
        return {'success': true, 'data': data};
      } else {
        final error = jsonDecode(response.body);
        return {'success': false, 'error': error['message'] ?? 'Login failed'};
      }
    } catch (e) {
      return {'success': false, 'error': 'Network error: $e'};
    }
  }

  // ==================== USER APIs ====================

  // Get all users
  Future<List<User>> getUsers() async {
    try {
      await loadToken();
      final response = await http.get(
        Uri.parse('$baseUrl/users'),
        headers: headers,
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        return data.map((json) => User.fromJson(json)).toList();
      } else {
        throw Exception('Failed to load users');
      }
    } catch (e) {
      throw Exception('Network error: $e');
    }
  }

  // Get user by ID
  Future<User> getUserById(int id) async {
    try {
      await loadToken();
      final response = await http.get(
        Uri.parse('$baseUrl/users/$id'),
        headers: headers,
      );

      if (response.statusCode == 200) {
        return User.fromJson(jsonDecode(response.body));
      } else {
        throw Exception('Failed to load user');
      }
    } catch (e) {
      throw Exception('Network error: $e');
    }
  }

  // Get users by role
  Future<List<User>> getUsersByRole(String role) async {
    try {
      await loadToken();
      final response = await http.get(
        Uri.parse('$baseUrl/users/role/$role'),
        headers: headers,
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        return data.map((json) => User.fromJson(json)).toList();
      } else {
        throw Exception('Failed to load users');
      }
    } catch (e) {
      throw Exception('Network error: $e');
    }
  }

  // Update user
  Future<User> updateUser(int id, Map<String, dynamic> userData) async {
    try {
      await loadToken();
      final response = await http.put(
        Uri.parse('$baseUrl/users/$id'),
        headers: headers,
        body: jsonEncode(userData),
      );

      if (response.statusCode == 200) {
        return User.fromJson(jsonDecode(response.body));
      } else {
        throw Exception('Failed to update user');
      }
    } catch (e) {
      throw Exception('Network error: $e');
    }
  }

  // Delete user
  Future<void> deleteUser(int id) async {
    try {
      await loadToken();
      final response = await http.delete(
        Uri.parse('$baseUrl/users/$id'),
        headers: headers,
      );

      if (response.statusCode != 200 && response.statusCode != 204) {
        throw Exception('Failed to delete user');
      }
    } catch (e) {
      throw Exception('Network error: $e');
    }
  }
}