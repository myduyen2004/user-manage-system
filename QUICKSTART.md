# ⚡ HƯỚNG DẪN CHẠY NHANH

## 🚀 Chạy với Docker (30 giây)

```bash
# Bước 1: Clone project
git clone <repo-url>
cd user-management-system

# Bước 2: Chạy Docker Compose
docker-compose up -d

# Bước 3: Chờ 30 giây để services khởi động
# Kiểm tra status:
docker-compose ps

# Bước 4: Truy cập
```

**✅ Frontend:** http://localhost:3000
**✅ Backend API:** http://localhost:8080/api

---

## 📝 Test hệ thống

### 1. Đăng ký tài khoản mới

Vào http://localhost:3000/register

```
Username: admin
Email: admin@fpt.edu.vn
Password: admin123
Full Name: Admin User
Role: ADMIN
```

Click **Register** → Chuyển sang Login

### 2. Đăng nhập

```
Username: admin
Password: admin123
```

Click **Login** → Vào Dashboard

### 3. Thử các chức năng

✅ Xem danh sách users
✅ Lọc theo role (Admin/Lecturer/Student)
✅ Toggle active/inactive
✅ Delete user
✅ Logout

---

## 🔧 Dừng và Xóa

```bash
# Dừng services
docker-compose down

# Dừng và xóa volumes (database)
docker-compose down -v

# Xóa images
docker rmi user-management-system-backend user-management-system-frontend
```

---

## 🐛 Lỗi thường gặp

### Port 8080 đã được sử dụng?
```bash
# Tìm process đang dùng port 8080
lsof -i :8080

# Kill process
kill -9 <PID>

# Hoặc đổi port trong docker-compose.yml
```

### Backend không kết nối được database?
```bash
# Xem logs
docker logs user-management-backend

# Restart service
docker-compose restart backend
```

### Frontend không load được?
```bash
# Rebuild frontend
docker-compose build frontend
docker-compose up -d frontend
```

---

## 📚 API Testing với curl

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@fpt.edu.vn",
    "password": "password123",
    "fullName": "Test User",
    "role": "STUDENT"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### Get Users (cần token)
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🎓 Học thêm

**Spring Boot:**
- https://spring.io/guides/gs/spring-boot/
- https://www.baeldung.com/spring-boot

**ReactJS:**
- https://react.dev/learn
- https://vitejs.dev/guide/

**Docker:**
- https://docs.docker.com/get-started/

---

**Chúc bạn học tốt! 🎉**
