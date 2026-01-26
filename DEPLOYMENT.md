# Deployment Guide - Environment-Based Configuration

This guide explains how to deploy the CardiRehab backend application using environment-based configuration.

## Overview

The application now supports multiple environments (local, stage, production) through:
- **Environment-specific property files**: `application-{env}.properties`
- **Environment variable files**: `.env.{env}` files
- **Deployment scripts**: Automated build and deployment

## Quick Start

### For Local Development

```bash
# 1. Copy environment template
cp .env.example .env.local

# 2. Edit .env.local with your local settings
nano .env.local

# 3. Build and run
./deploy.sh local
./run.sh local
```

### For Stage/Preprod Deployment

```bash
# 1. Pull latest code
git pull origin main

# 2. Update .env.stage if needed
nano .env.stage

# 3. Deploy and start
sudo ./scripts/deploy-and-start.sh stage
```

### For Production Deployment

```bash
# 1. Pull latest code
git pull origin main

# 2. Update .env.prod if needed
nano .env.prod

# 3. Deploy and start
sudo ./scripts/deploy-and-start.sh prod
```

## Environment Files

### Structure

- `.env.example` - Template file (committed to git)
- `.env.local` - Local development (not committed)
- `.env.stage` - Stage/preprod environment (not committed)
- `.env.prod` - Production environment (not committed)

### Configuration Variables

All environment files contain:

```bash
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/herplat?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_password

# Server Configuration
SERVER_PORT=9595

# Logging
LOG_FILE_PATH=/var/log/herplatform/application.log

# Secure Files
SECURE_FILES_PATH=/var/secure_files/files

# JWT Secret
JWT_SECRET=CardiRehabSecretKeyX12138yAn4
```

## Deployment Scripts

### `deploy.sh`

Builds the application for a specific environment.

```bash
./deploy.sh [local|stage|prod]
```

**What it does:**
1. Loads environment variables from `.env.{env}` file
2. Sets Spring profile
3. Builds the WAR file using Maven

### `run.sh`

Runs the application locally (for testing).

```bash
./run.sh [local|stage|prod]
```

### `scripts/deploy-and-start.sh`

Complete deployment: build, deploy, and start systemd service.

```bash
sudo ./scripts/deploy-and-start.sh [stage|prod]
```

**What it does:**
1. Builds the application
2. Stops existing service
3. Sets proper permissions
4. Starts the service
5. Checks service status

### `scripts/setup-service.sh`

Sets up systemd service for an environment (one-time setup).

```bash
sudo ./scripts/setup-service.sh [stage|prod]
```

## Systemd Services

### Service Files

- `scripts/cardirehab-backend-stage.service` - Stage environment
- `scripts/cardirehab-backend-prod.service` - Production environment

### Service Management

```bash
# Start service
sudo systemctl start cardirehab-backend-{stage|prod}

# Stop service
sudo systemctl stop cardirehab-backend-{stage|prod}

# Restart service
sudo systemctl restart cardirehab-backend-{stage|prod}

# Check status
sudo systemctl status cardirehab-backend-{stage|prod}

# View logs
sudo journalctl -u cardirehab-backend-{stage|prod} -f
```

## Initial Setup

### First Time Setup for Stage

```bash
# 1. Copy and configure environment file
cp .env.example .env.stage
nano .env.stage  # Update with stage-specific values

# 2. Setup systemd service
sudo ./scripts/setup-service.sh stage

# 3. Deploy and start
sudo ./scripts/deploy-and-start.sh stage
```

### First Time Setup for Production

```bash
# 1. Copy and configure environment file
cp .env.example .env.prod
nano .env.prod  # Update with production-specific values

# 2. Setup systemd service
sudo ./scripts/setup-service.sh prod

# 3. Deploy and start
sudo ./scripts/deploy-and-start.sh prod
```

## Switching Between Environments

Since both stage and production use the same folder, you can easily switch:

```bash
# Switch to stage
sudo systemctl stop cardirehab-backend-prod
sudo ./scripts/deploy-and-start.sh stage

# Switch to production
sudo systemctl stop cardirehab-backend-stage
sudo ./scripts/deploy-and-start.sh prod
```

**Note:** Only one service should run at a time since they use the same WAR file location.

## Updating Configuration

### Update Environment Variables

1. Edit the appropriate `.env.{env}` file:
   ```bash
   nano .env.stage  # or .env.prod
   ```

2. Restart the service:
   ```bash
   sudo systemctl restart cardirehab-backend-{stage|prod}
   ```

### Update Application Properties

1. Edit the appropriate `application-{env}.properties` file:
   ```bash
   nano src/main/resources/application-stage.properties
   ```

2. Rebuild and restart:
   ```bash
   ./deploy.sh stage
   sudo systemctl restart cardirehab-backend-stage
   ```

## Troubleshooting

### Service Won't Start

```bash
# Check service status
sudo systemctl status cardirehab-backend-{env}

# Check logs
sudo journalctl -u cardirehab-backend-{env} -n 50

# Verify environment file exists
ls -la .env.{env}

# Verify WAR file exists
ls -lh target/crehab.war
```

### Environment Variables Not Loading

- Ensure `.env.{env}` file exists and has correct format
- Check file permissions: `chmod 644 .env.{env}`
- Verify systemd service has `EnvironmentFile` pointing to correct file

### Port Already in Use

- Check if another service is running: `sudo netstat -tlnp | grep {PORT}`
- Stop conflicting service or change port in `.env.{env}`

## Best Practices

1. **Never commit `.env` files** - They contain sensitive information
2. **Always use `.env.example`** as a template
3. **Test locally first** - Use `./run.sh local` before deploying
4. **Backup before changes** - Especially for production
5. **One environment at a time** - Don't run stage and prod simultaneously

## File Structure

```
backend/
├── .env.example              # Template (committed)
├── .env.local                # Local dev (not committed)
├── .env.stage                # Stage env (not committed)
├── .env.prod                 # Production env (not committed)
├── deploy.sh                 # Build script
├── run.sh                    # Run script
├── scripts/
│   ├── deploy-and-start.sh   # Full deployment
│   ├── setup-service.sh      # Service setup
│   ├── load-env.sh           # Env loader helper
│   ├── cardirehab-backend-stage.service
│   └── cardirehab-backend-prod.service
└── src/main/resources/
    ├── application.properties        # Base config
    ├── application-local.properties  # Local overrides
    ├── application-stage.properties  # Stage overrides
    └── application-prod.properties   # Prod overrides
```
