#!/bin/bash

echo "========================================"
echo "  User Management System - Startup"
echo "========================================"
echo ""

echo "[1] Checking Docker..."
if ! command -v docker &> /dev/null; then
    echo "ERROR: Docker is not installed!"
    echo "Please install Docker Desktop first."
    exit 1
fi
echo "Docker is installed!"
echo ""

echo "[2] Starting services..."
docker-compose up -d
echo ""

echo "[3] Waiting for services to start (30 seconds)..."
sleep 30
echo ""

echo "[4] Checking services status..."
docker-compose ps
echo ""

echo "========================================"
echo "  System is ready!"
echo "========================================"
echo ""
echo "Frontend: http://localhost:3000"
echo "Backend:  http://localhost:8080"
echo ""
echo "Press Enter to view logs (Ctrl+C to exit)..."
read

docker-compose logs -f
