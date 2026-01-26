#!/bin/bash

# Helper script to load environment variables from .env file
# Usage: source scripts/load-env.sh [local|preprod|prod]

ENVIRONMENT=${1:-local}
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env.${ENVIRONMENT}"

if [ ! -f "$ENV_FILE" ]; then
    echo "Error: Environment file not found: $ENV_FILE" >&2
    return 1 2>/dev/null || exit 1
fi

# Load environment variables (skip comments and empty lines)
while IFS= read -r line || [ -n "$line" ]; do
    # Skip comments and empty lines
    [[ "$line" =~ ^[[:space:]]*# ]] && continue
    [[ -z "${line// }" ]] && continue
    
    # Export the variable
    export "$line"
done < "$ENV_FILE"

export SPRING_PROFILES_ACTIVE="$ENVIRONMENT"

echo "Loaded environment: $ENVIRONMENT"
echo "Spring Profile: $SPRING_PROFILES_ACTIVE"
