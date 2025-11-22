@echo off
echo ========================================
echo   User Management System - Startup
echo ========================================
echo.

echo [1] Checking Docker...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not installed!
    echo Please install Docker Desktop first.
    pause
    exit /b 1
)
echo Docker is installed!
echo.

echo [2] Starting services...
docker-compose up -d
echo.

echo [3] Waiting for services to start (30 seconds)...
timeout /t 30 /nobreak >nul
echo.

echo [4] Checking services status...
docker-compose ps
echo.

echo ========================================
echo   System is ready!
echo ========================================
echo.
echo Frontend: http://localhost:3000
echo Backend:  http://localhost:8080
echo.
echo Press any key to view logs (Ctrl+C to exit)...
pause >nul

docker-compose logs -f
