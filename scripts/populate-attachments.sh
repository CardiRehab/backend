#!/bin/bash

# Simple script to populate attachments table
# Usage: 
#   ./scripts/populate-attachments.sh           # Uses .env.local (default)
#   ./scripts/populate-attachments.sh local     # Uses .env.local
#   ./scripts/populate-attachments.sh prod      # Uses .env.prod
#   ./scripts/populate-attachments.sh preprod   # Uses .env.preprod
#   ENV=prod ./scripts/populate-attachments.sh  # Uses .env.prod (via env var)

set -e  # Exit on error

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

# Determine which environment to use
# Priority: 1) Command line argument, 2) ENV environment variable, 3) Default to 'local'
ENV_TYPE="${1:-${ENV:-local}}"

echo "=========================================="
echo "Populate Attachments Script"
echo "Environment: $ENV_TYPE"
echo "=========================================="
echo ""

# Change to project directory
cd "$PROJECT_DIR"

# Determine .env file based on environment
ENV_FILE=".env.$ENV_TYPE"

# Check if .env file exists and source it
if [ -f "$ENV_FILE" ]; then
    echo "Loading environment variables from $ENV_FILE..."
    set -a  # Automatically export all variables
    source "$ENV_FILE"
    set +a
    echo "✓ Environment variables loaded from $ENV_FILE"
    echo ""
else
    echo "⚠ Warning: $ENV_FILE not found."
    echo "Available .env files:"
    ls -1 .env.* 2>/dev/null | sed 's/^/  - /' || echo "  (none found)"
    echo ""
    echo "Using default values or environment variables."
    echo ""
fi

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven (mvn) is not installed or not in PATH"
    echo "Please install Maven to run this script"
    exit 1
fi

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java to run this script"
    exit 1
fi

echo "Compiling project..."
mvn compile -q

echo "Running populate attachments script..."
echo ""

# Debug: Show which database we're connecting to
if [ -n "$DB_URL" ]; then
    echo "Database URL: $DB_URL"
else
    echo "⚠ Warning: DB_URL not set, will use default (herplat)"
fi
echo ""

# Run the Java standalone script with environment variables explicitly passed
mvn exec:java \
    -Dexec.mainClass="com.healthcare.herplatform.scripts.StandalonePopulateAttachments" \
    -Dexec.classpathScope=runtime \
    -q

echo ""
echo "=========================================="
echo "Script execution completed!"
echo "=========================================="
