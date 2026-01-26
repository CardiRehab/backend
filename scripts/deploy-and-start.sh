#!/bin/bash

# Complete deployment script: build, deploy, and start service
# Usage: sudo ./scripts/deploy-and-start.sh [preprod|prod]

set -e

ENVIRONMENT=${1:-prod}
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

if [ "$ENVIRONMENT" != "preprod" ] && [ "$ENVIRONMENT" != "prod" ]; then
    echo "Error: Environment must be 'preprod' or 'prod'"
    exit 1
fi

echo "========================================="
echo "Deploying and starting: $ENVIRONMENT"
echo "========================================="

# Step 1: Build
echo ""
echo "Step 1: Building application..."
cd "$SCRIPT_DIR"
./deploy.sh "$ENVIRONMENT"

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

# Step 2: Stop existing service (if running)
echo ""
echo "Step 2: Stopping existing service..."
sudo systemctl stop "cardirehab-backend-${ENVIRONMENT}" 2>/dev/null || true

# Step 3: Set permissions
echo ""
echo "Step 3: Setting permissions..."
sudo chown -R www-data:www-data "$SCRIPT_DIR"
sudo chown -R www-data:www-data /var/log/herplatform* 2>/dev/null || true
sudo chown -R www-data:www-data /var/secure_files 2>/dev/null || true

# Step 4: Start service
echo ""
echo "Step 4: Starting service..."
sudo systemctl start "cardirehab-backend-${ENVIRONMENT}"

# Step 5: Wait and check status
echo ""
echo "Step 5: Checking service status..."
sleep 3
sudo systemctl status "cardirehab-backend-${ENVIRONMENT}" --no-pager | head -15

echo ""
echo "========================================="
echo "Deployment complete!"
echo "========================================="
echo ""
echo "Service: cardirehab-backend-${ENVIRONMENT}"
echo "Check logs: sudo journalctl -u cardirehab-backend-${ENVIRONMENT} -f"
