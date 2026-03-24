# Deployment Guide - CI/CD with GitHub Actions

## Overview

The application deploys automatically via GitHub Actions using SSH:

- **Push/merge to `main`** → deploys to **preprod** (`backend-preprod` on server)
- **Push/merge to `production`** → deploys to **production** (`backend-prod` on server)

Both environments run on the **same server** as separate systemd services with separate directories. No Docker needed.

## Architecture

```
GitHub                          Your Server
┌──────────┐   SSH deploy   ┌─────────────────────────────────────┐
│  main    ├───────────────►│ /var/www/html/backend-preprod       │
│  branch  │                │   service: cardirehab-backend-preprod│
│          │                │   port: 9596                        │
├──────────┤                ├─────────────────────────────────────┤
│production├───────────────►│ /var/www/html/backend-prod          │
│  branch  │                │   service: cardirehab-backend-prod  │
│          │                │   port: 9595                        │
└──────────┘                └─────────────────────────────────────┘
```

## Workflow

1. Develop features locally on any branch
2. Push or merge to `main` → **auto-deploys to preprod**
3. Test and verify on preprod
4. Merge `main` into `production` → **auto-deploys to production**

## One-Time Server Setup

Run this once on your server to set up the directory structure:

```bash
sudo bash scripts/server-init.sh git@github.com:youruser/backend.git
```

This script:
- Clones the repo into `/var/www/html/backend-preprod` (tracks `main`)
- Clones the repo into `/var/www/html/backend-prod` (tracks `production`)
- Copies existing `.env` files if found
- Installs systemd services
- Configures passwordless sudo for the deploy user (only for service management)

After running, configure your env files:
```bash
nano /var/www/html/backend-preprod/.env.preprod
nano /var/www/html/backend-prod/.env.prod
```

## GitHub Secrets

Add these in your GitHub repo → Settings → Secrets and variables → Actions:

| Secret | Description | Example |
|--------|-------------|---------|
| `SERVER_HOST` | Server IP or hostname | `203.0.113.50` |
| `SERVER_USER` | SSH username on server | `deploy` |
| `SERVER_SSH_KEY` | Private SSH key (full content) | Contents of `~/.ssh/id_ed25519` |
| `SERVER_SSH_PORT` | SSH port (optional, defaults to 22) | `22` |

### Generating an SSH key pair for deployment

```bash
# On your local machine (or the server)
ssh-keygen -t ed25519 -C "github-actions-deploy" -f ~/.ssh/deploy_key

# Add the public key to the server's authorized_keys
ssh-copy-id -i ~/.ssh/deploy_key.pub youruser@yourserver

# Copy the private key content — paste this as SERVER_SSH_KEY in GitHub
cat ~/.ssh/deploy_key
```

## Local Development

No changes to local dev workflow:

```bash
cp .env.example .env.local
nano .env.local
./run.sh local
```

## Environment Files

| File | Purpose | Committed? |
|------|---------|------------|
| `.env.example` | Template with placeholders | Yes |
| `.env.local` | Local development | No |
| `.env.preprod` | Preprod (on server only) | No |
| `.env.prod` | Production (on server only) | No |

## Manual Deployment (if needed)

SSH into the server and run:

```bash
# Preprod
cd /var/www/html/backend-preprod
bash scripts/ci-deploy.sh preprod

# Production
cd /var/www/html/backend-prod
bash scripts/ci-deploy.sh prod
```

## Service Management

```bash
# Preprod
sudo systemctl status cardirehab-backend-preprod
sudo systemctl restart cardirehab-backend-preprod
sudo journalctl -u cardirehab-backend-preprod -f

# Production
sudo systemctl status cardirehab-backend-prod
sudo systemctl restart cardirehab-backend-prod
sudo journalctl -u cardirehab-backend-prod -f
```

## File Structure (on server)

```
/var/www/html/
├── backend-preprod/          # main branch
│   ├── .env.preprod          # preprod env vars (not in git)
│   ├── scripts/
│   │   ├── ci-deploy.sh      # CI deploy script
│   │   └── ...
│   ├── target/
│   │   └── crehab.war
│   └── src/...
│
├── backend-prod/             # production branch
│   ├── .env.prod             # prod env vars (not in git)
│   ├── scripts/
│   │   ├── ci-deploy.sh
│   │   └── ...
│   ├── target/
│   │   └── crehab.war
│   └── src/...
```

## Troubleshooting

### GitHub Actions workflow failed

Check the Actions tab in your GitHub repo for logs. Common issues:
- SSH key not configured correctly
- Server not reachable (firewall/port)
- Maven build failed (check build logs on server)

### Service won't start after deploy

```bash
# Check logs
sudo journalctl -u cardirehab-backend-{preprod|prod} -n 50

# Verify WAR file
ls -lh /var/www/html/backend-{preprod|prod}/target/crehab.war

# Verify env file
cat /var/www/html/backend-{preprod|prod}/.env.{preprod|prod}
```

### Port conflict

Preprod uses port **9596**, production uses port **9595**. Both run simultaneously. If there's a conflict:

```bash
sudo ss -tlnp | grep -E '9595|9596'
```
