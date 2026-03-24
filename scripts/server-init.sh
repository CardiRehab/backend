#!/bin/bash

# One-time server setup for CI/CD pipeline
# Run this ONCE on your server to create the directory structure
# Usage: sudo bash server-init.sh <github-repo-url>

set -e

REPO_URL=${1}

if [ -z "$REPO_URL" ]; then
    echo "Usage: sudo bash $0 <github-repo-ssh-url>"
    echo "Example: sudo bash $0 git@github.com:youruser/backend.git"
    exit 1
fi

if [ "$EUID" -ne 0 ]; then
    echo "Error: Run as root (sudo)"
    exit 1
fi

DEPLOY_USER=${SUDO_USER:-$(whoami)}
PREPROD_DIR="/var/www/html/backend-preprod"
PROD_DIR="/var/www/html/backend-prod"

echo "========================================="
echo "Server Init for CI/CD"
echo "========================================="
echo "Repo: $REPO_URL"
echo "Deploy user: $DEPLOY_USER"
echo "Preprod dir: $PREPROD_DIR"
echo "Prod dir:    $PROD_DIR"
echo ""

# --- Preprod ---
echo "[1/6] Setting up preprod directory..."
if [ -d "$PREPROD_DIR" ]; then
    echo "  $PREPROD_DIR already exists, skipping clone."
else
    git clone "$REPO_URL" "$PREPROD_DIR"
fi
cd "$PREPROD_DIR"
git checkout main
git pull origin main
chown -R "$DEPLOY_USER":"$DEPLOY_USER" "$PREPROD_DIR"

# --- Prod ---
echo "[2/6] Setting up production directory..."
if [ -d "$PROD_DIR" ]; then
    echo "  $PROD_DIR already exists, skipping clone."
else
    git clone "$REPO_URL" "$PROD_DIR"
fi
cd "$PROD_DIR"
git checkout production 2>/dev/null || git checkout -b production origin/production 2>/dev/null || {
    echo "  'production' branch not found on remote. Creating from main..."
    git checkout main
    git checkout -b production
    git push -u origin production
}
chown -R "$DEPLOY_USER":"$DEPLOY_USER" "$PROD_DIR"

# --- Env files ---
echo "[3/6] Checking environment files..."
if [ -f "/var/www/html/backend/.env.preprod" ] && [ ! -f "$PREPROD_DIR/.env.preprod" ]; then
    cp "/var/www/html/backend/.env.preprod" "$PREPROD_DIR/.env.preprod"
    echo "  Copied existing .env.preprod"
elif [ ! -f "$PREPROD_DIR/.env.preprod" ]; then
    echo "  WARNING: No .env.preprod found. Copy .env.example and configure:"
    echo "    cp $PREPROD_DIR/.env.example $PREPROD_DIR/.env.preprod"
fi

if [ -f "/var/www/html/backend/.env.prod" ] && [ ! -f "$PROD_DIR/.env.prod" ]; then
    cp "/var/www/html/backend/.env.prod" "$PROD_DIR/.env.prod"
    echo "  Copied existing .env.prod"
elif [ ! -f "$PROD_DIR/.env.prod" ]; then
    echo "  WARNING: No .env.prod found. Copy .env.example and configure:"
    echo "    cp $PROD_DIR/.env.example $PROD_DIR/.env.prod"
fi

# --- Log dirs ---
echo "[4/6] Creating log directories..."
mkdir -p /var/log/herplatform
mkdir -p /var/log/herplatform-preprod
mkdir -p /var/secure_files/files
chown -R www-data:www-data /var/log/herplatform*
chown -R www-data:www-data /var/secure_files

# --- Systemd services ---
echo "[5/6] Installing systemd services..."
cp "$PREPROD_DIR/scripts/cardirehab-backend-preprod.service" /etc/systemd/system/
cp "$PROD_DIR/scripts/cardirehab-backend-prod.service" /etc/systemd/system/
chmod 644 /etc/systemd/system/cardirehab-backend-preprod.service
chmod 644 /etc/systemd/system/cardirehab-backend-prod.service
systemctl daemon-reload
systemctl enable cardirehab-backend-preprod
systemctl enable cardirehab-backend-prod

# --- Sudoers for deploy user (passwordless systemctl + chown for CI) ---
echo "[6/6] Configuring sudo permissions for deploy user..."
SUDOERS_FILE="/etc/sudoers.d/cardirehab-deploy"
cat > "$SUDOERS_FILE" <<SUDOERS
# Allow deploy user to manage cardirehab services and set permissions without password
$DEPLOY_USER ALL=(ALL) NOPASSWD: /usr/bin/systemctl restart cardirehab-backend-*
$DEPLOY_USER ALL=(ALL) NOPASSWD: /usr/bin/systemctl stop cardirehab-backend-*
$DEPLOY_USER ALL=(ALL) NOPASSWD: /usr/bin/systemctl start cardirehab-backend-*
$DEPLOY_USER ALL=(ALL) NOPASSWD: /usr/bin/systemctl status cardirehab-backend-*
$DEPLOY_USER ALL=(ALL) NOPASSWD: /usr/bin/systemctl is-active cardirehab-backend-*
$DEPLOY_USER ALL=(ALL) NOPASSWD: /usr/bin/journalctl -u cardirehab-backend-*
$DEPLOY_USER ALL=(ALL) NOPASSWD: /bin/chown -R www-data\:www-data /var/www/html/backend-*
$DEPLOY_USER ALL=(ALL) NOPASSWD: /bin/chown -R www-data\:www-data /var/log/herplatform*
$DEPLOY_USER ALL=(ALL) NOPASSWD: /bin/chown -R www-data\:www-data /var/secure_files*
SUDOERS
chmod 440 "$SUDOERS_FILE"

echo ""
echo "========================================="
echo "Setup complete!"
echo "========================================="
echo ""
echo "Next steps:"
echo "  1. Configure env files if not already done:"
echo "     nano $PREPROD_DIR/.env.preprod"
echo "     nano $PROD_DIR/.env.prod"
echo ""
echo "  2. Do an initial build + start for each:"
echo "     cd $PREPROD_DIR && ./mvnw clean package -DskipTests"
echo "     sudo systemctl start cardirehab-backend-preprod"
echo ""
echo "     cd $PROD_DIR && ./mvnw clean package -DskipTests"
echo "     sudo systemctl start cardirehab-backend-prod"
echo ""
echo "  3. Add GitHub secrets in your repo settings:"
echo "     SERVER_HOST     = your server IP or hostname"
echo "     SERVER_USER     = $DEPLOY_USER"
echo "     SERVER_SSH_KEY  = contents of ~/.ssh/id_ed25519 (private key)"
echo "     SERVER_SSH_PORT = 22 (optional, if non-standard)"
echo ""
echo "  4. Push code to 'main' → auto-deploys to preprod"
echo "     Merge to 'production' → auto-deploys to production"
