#!/bin/bash

# Run script for CardiRehab Backend
# Usage: ./run.sh [local|preprod|prod]

set -e

ENVIRONMENT=${1:-local}
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env.${ENVIRONMENT}"

if [ ! -f "$ENV_FILE" ]; then
    echo "Error: Environment file not found: $ENV_FILE"
    echo "Available environments: local, preprod, prod"
    exit 1
fi

# Load environment variables from .env file (xargs breaks on inline "#" comments and spaces)
while IFS= read -r line || [ -n "$line" ]; do
    [[ "$line" =~ ^[[:space:]]*# ]] && continue
    [[ -z "${line// }" ]] && continue
    line=$(sed -e 's/[[:space:]]\{1,\}#.*$//' <<< "$line")
    [[ -z "${line// }" ]] && continue
    line="${line//$'\r'/}"
    key="${line%%=*}"
    val="${line#*=}"
    [[ "$key" == "$line" ]] && continue
    if [[ "$val" =~ ^\"(.*)\"$ ]]; then
        val="${BASH_REMATCH[1]}"
    elif [[ "$val" =~ ^\'(.*)\'$ ]]; then
        val="${BASH_REMATCH[1]}"
    fi
    export "${key}=${val}"
done < "$ENV_FILE"

# Export Spring profile
export SPRING_PROFILES_ACTIVE="$ENVIRONMENT"

echo "Starting application with environment: $ENVIRONMENT"
echo "Spring Profile: $SPRING_PROFILES_ACTIVE"
echo ""

# Run the application
cd "$SCRIPT_DIR"
java -jar target/crehab.war
