package com.healthcare.herplatform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Enables Spring's scheduled task support so the account-deletion sweep
 * (AccountDeletionService) runs on its cron schedule.
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
