#!/bin/bash

# CI/CD deploy script - called by GitHub Actions via SSH
# Usage: ./scripts/ci-deploy.sh <preprod|prod>

set -e

ENVIRONMENT=${1}

if [ -z "$ENVIRONMENT" ] || { [ "$ENVIRONMENT" != "preprod" ] && [ "$ENVIRONMENT" != "prod" ]; }; then
    echo "Usage: $0 <preprod|prod>"
    exit 1
fi

if [ "$ENVIRONMENT" = "preprod" ]; then
    DEPLOY_DIR="/var/www/html/backend-preprod"
    BRANCH="main"
else
    DEPLOY_DIR="/var/www/html/backend-prod"
    BRANCH="production"
fi

SERVICE_NAME="cardirehab-backend-${ENVIRONMENT}"

echo "========================================="
echo "Deploying $ENVIRONMENT from branch: $BRANCH"
echo "Directory: $DEPLOY_DIR"
echo "========================================="

cd "$DEPLOY_DIR"

echo "[1/5] Pulling latest code..."
git fetch origin
git reset --hard "origin/${BRANCH}"

echo "[2/5] Building application..."
./mvnw clean package -DskipTests

echo "[3/5] Setting permissions..."
sudo chown -R www-data:www-data "$DEPLOY_DIR"
sudo chown -R www-data:www-data /var/log/herplatform* 2>/dev/null || true
sudo chown -R www-data:www-data /var/secure_files 2>/dev/null || true

echo "[4/5] Restarting service..."
sudo systemctl restart "$SERVICE_NAME"

echo "[5/5] Verifying..."
sleep 5

if sudo systemctl is-active --quiet "$SERVICE_NAME"; then
    echo "Service $SERVICE_NAME is running."
    sudo systemctl status "$SERVICE_NAME" --no-pager | head -10
    echo ""
    echo "Deployment successful!"
else
    echo "Service $SERVICE_NAME failed to start!"
    sudo journalctl -u "$SERVICE_NAME" -n 30 --no-pager
    exit 1
fi
