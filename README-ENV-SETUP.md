# Environment-Based Configuration Setup

This document summarizes the environment-based configuration system that has been implemented.

## What Changed

### 1. Environment-Specific Property Files
- `application.properties` - Base configuration with environment variable placeholders
- `application-local.properties` - Local development overrides
- `application-stage.properties` - Stage/preprod overrides
- `application-prod.properties` - Production overrides

### 2. Environment Variable Files (.env)
- `.env.example` - Template file (committed to git)
- `.env.local` - Local development (not committed)
- `.env.stage` - Stage environment (not committed)
- `.env.prod` - Production environment (not committed)

### 3. Deployment Scripts
- `deploy.sh` - Builds application for specified environment
- `run.sh` - Runs application locally for testing
- `scripts/deploy-and-start.sh` - Complete deployment (build + start service)
- `scripts/setup-service.sh` - One-time systemd service setup
- `scripts/load-env.sh` - Helper to load environment variables

### 4. Systemd Service Files
- `scripts/cardirehab-backend-stage.service` - Stage service
- `scripts/cardirehab-backend-prod.service` - Production service

## How to Use

### Initial Setup (One Time)

1. **Configure environment files:**
   ```bash
   cp .env.example .env.prod
   nano .env.prod  # Update with production values
   
   cp .env.example .env.stage
   nano .env.stage  # Update with stage values
   ```

2. **Setup systemd services:**
   ```bash
   sudo ./scripts/setup-service.sh prod
   sudo ./scripts/setup-service.sh stage
   ```

### Daily Deployment

**Production:**
```bash
cd /var/www/html/backend
git pull origin main
sudo ./scripts/deploy-and-start.sh prod
```

**Stage:**
```bash
cd /var/www/html/backend
git pull origin main
sudo ./scripts/deploy-and-start.sh stage
```

### Switching Between Environments

Since both use the same folder, you can switch easily:

```bash
# Switch to stage
sudo systemctl stop cardirehab-backend-prod
sudo ./scripts/deploy-and-start.sh stage

# Switch to production
sudo systemctl stop cardirehab-backend-stage
sudo ./scripts/deploy-and-start.sh prod
```

## Benefits

1. **Easy Environment Switching** - Just change the environment parameter
2. **Version Control Safe** - Sensitive data in .env files (not committed)
3. **Same Folder** - Both stage and production can use the same codebase
4. **Simple Deployment** - One command: `./scripts/deploy-and-start.sh {env}`
5. **Configuration Management** - All config in one place per environment

## Files to Commit

✅ **Commit these:**
- `.env.example` (template)
- `application-*.properties` files
- `deploy.sh`, `run.sh`
- `scripts/*.sh` and `scripts/*.service`
- `DEPLOYMENT.md`
- Updated `.gitignore`

❌ **Don't commit:**
- `.env.local`
- `.env.stage`
- `.env.prod`
- Any actual `.env` files with real credentials

## Next Steps

1. Review and update `.env.prod` and `.env.stage` with actual values
2. Test deployment: `sudo ./scripts/deploy-and-start.sh stage`
3. Commit changes to git
4. Pull on local machine and test with `./run.sh local`
