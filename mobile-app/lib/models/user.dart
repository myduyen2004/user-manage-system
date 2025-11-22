class User {
  final int id;
  final String username;
  final String email;
  final String fullName;
  final String role;
  final bool active;
  final DateTime? createdAt;

  User({
    required this.id,
    required this.username,
    required this.email,
    required this.fullName,
    required this.role,
    required this.active,
    this.createdAt,
  });

  // Parse từ JSON (từ API)
  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'],
      username: json['username'],
      email: json['email'],
      fullName: json['fullName'],
      role: json['role'],
      active: json['active'] ?? true,
      createdAt: json['createdAt'] != null 
          ? DateTime.parse(json['createdAt']) 
          : null,
    );
  }

  // Convert sang JSON (gửi API)
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'username': username,
      'email': email,
      'fullName': fullName,
      'role': role,
      'active': active,
    };
  }

  // Role display
  String get roleDisplay {
    switch (role) {
      case 'ADMIN':
        return 'Administrator';
      case 'LECTURER':
        return 'Lecturer';
      case 'STUDENT':
        return 'Student';
      default:
        return role;
    }
  }

  // Role color
  String get roleColor {
    switch (role) {
      case 'ADMIN':
        return '#F44336'; // Red
      case 'LECTURER':
        return '#2196F3'; // Blue
      case 'STUDENT':
        return '#4CAF50'; // Green
      default:
        return '#9E9E9E'; // Grey
    }
  }
}