package com.healthcare.herplatform.payloads;

import java.util.Date;

/**
 * Returned by /api/auth/signin when a user with a pending deletion signs in
 * with valid credentials. Carries no token — the client uses {@code accountstatus}
 * to route the user to the reactivation screen instead of the dashboard.
 */
public class DeletionPendingResponse {
	private String accountstatus = "Deleting";
	private String username;
	private Date deletionRequestedAt;
	private long daysRemaining;
	private String message;

	public DeletionPendingResponse(String username, Date deletionRequestedAt, long daysRemaining, String message) {
		this.username = username;
		this.deletionRequestedAt = deletionRequestedAt;
		this.daysRemaining = daysRemaining;
		this.message = message;
	}

	public String getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getDeletionRequestedAt() {
		return deletionRequestedAt;
	}

	public void setDeletionRequestedAt(Date deletionRequestedAt) {
		this.deletionRequestedAt = deletionRequestedAt;
	}

	public long getDaysRemaining() {
		return daysRemaining;
	}

	public void setDaysRemaining(long daysRemaining) {
		this.daysRemaining = daysRemaining;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
