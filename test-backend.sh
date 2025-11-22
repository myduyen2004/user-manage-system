#!/bin/bash

echo "================================"
echo "Testing Backend API"
echo "================================"
echo ""

# Wait for backend to start
echo "[1] Waiting for backend to be ready..."
for i in {1..30}; do
    if curl -s http://localhost:8080/api/auth/register > /dev/null 2>&1; then
        echo "✅ Backend is ready!"
        break
    fi
    echo "Waiting... ($i/30)"
    sleep 2
done

echo ""
echo "[2] Testing Register endpoint..."
REGISTER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@fpt.edu.vn",
    "password": "test123",
    "fullName": "Test User",
    "role": "STUDENT"
  }')

echo "Response: $REGISTER_RESPONSE"
echo ""

echo "[3] Testing Login endpoint..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123"
  }')

echo "Response: $LOGIN_RESPONSE"
echo ""

# Extract token
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo "❌ Failed to get token"
    exit 1
fi

echo "✅ Got token: ${TOKEN:0:20}..."
echo ""

echo "[4] Testing Get Users endpoint (with token)..."
USERS_RESPONSE=$(curl -s -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN")

echo "Response: $USERS_RESPONSE"
echo ""

echo "================================"
echo "✅ All tests completed!"
echo "================================"
