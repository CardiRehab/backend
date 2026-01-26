#!/bin/bash

# Deployment script for CardiRehab Backend
# Usage: ./deploy.sh [local|preprod|prod]

set -e

ENVIRONMENT=${1:-prod}
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env.${ENVIRONMENT}"

if [ ! -f "$ENV_FILE" ]; then
    echo "Error: Environment file not found: $ENV_FILE"
    echo "Available environments: local, preprod, prod"
    exit 1
fi

echo "========================================="
echo "Deploying for environment: $ENVIRONMENT"
echo "========================================="

# Load environment variables from .env file
export $(grep -v '^#' "$ENV_FILE" | xargs)

# Export Spring profile
export SPRING_PROFILES_ACTIVE="$ENVIRONMENT"

echo "Environment variables loaded from: $ENV_FILE"
echo "Spring Profile: $SPRING_PROFILES_ACTIVE"
echo ""

# Build the application
echo "Building application..."
cd "$SCRIPT_DIR"
chmod +x mvnw 2>/dev/null || true
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

echo ""
echo "Build completed successfully!"
echo "WAR file: $SCRIPT_DIR/target/crehab.war"
echo ""
echo "To run the application:"
echo "  java -jar $SCRIPT_DIR/target/crehab.war"
echo ""
echo "Or use the systemd service:"
echo "  sudo systemctl restart cardirehab-backend-${ENVIRONMENT}"
