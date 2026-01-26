#!/bin/bash

# Setup systemd service for specified environment
# Usage: sudo ./scripts/setup-service.sh [preprod|prod]

set -e

ENVIRONMENT=${1:-prod}
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SERVICE_FILE="${SCRIPT_DIR}/scripts/cardirehab-backend-${ENVIRONMENT}.service"
SYSTEMD_PATH="/etc/systemd/system/cardirehab-backend-${ENVIRONMENT}.service"

if [ ! -f "$SERVICE_FILE" ]; then
    echo "Error: Service file not found: $SERVICE_FILE"
    echo "Available environments: preprod, prod"
    exit 1
fi

if [ "$EUID" -ne 0 ]; then 
    echo "Error: This script must be run as root (use sudo)"
    exit 1
fi

echo "Setting up systemd service for environment: $ENVIRONMENT"
echo "Copying service file to: $SYSTEMD_PATH"

cp "$SERVICE_FILE" "$SYSTEMD_PATH"
chmod 644 "$SYSTEMD_PATH"

echo "Reloading systemd daemon..."
systemctl daemon-reload

echo "Enabling service to start on boot..."
systemctl enable "cardirehab-backend-${ENVIRONMENT}"

echo ""
echo "Service setup complete!"
echo ""
echo "To start the service:"
echo "  sudo systemctl start cardirehab-backend-${ENVIRONMENT}"
echo ""
echo "To check status:"
echo "  sudo systemctl status cardirehab-backend-${ENVIRONMENT}"
