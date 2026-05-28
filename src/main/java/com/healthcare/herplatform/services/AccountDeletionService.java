package com.healthcare.herplatform.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.healthcare.herplatform.entity.User;
import com.healthcare.herplatform.repository.UserRepository;

/**
 * Daily sweep that permanently deletes accounts whose deletion window has
 * elapsed. Users initiate deletion via /api/user/delete-account (status set
 * to "Deleting"); they may reactivate within the window via
 * /api/auth/reactivate-account. Anything still "Deleting" past the window is
 * purged here, after which the person must register again. The window length is
 * configured by app.account.deletion-window-days (shared with AuthController).
 */
@Service
public class AccountDeletionService {

	private static final String PENDING_STATUS = "Deleting";

	@Value("${app.account.deletion-window-days:30}")
	private long deletionWindowDays;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountPurger accountPurger;

	/** Runs daily at 03:00 server time. */
	@Scheduled(cron = "0 0 3 * * *")
	public void purgeExpiredAccounts() {
		Date cutoff = new Date(System.currentTimeMillis() - deletionWindowDays * 24L * 60 * 60 * 1000);
		List<User> expired = userRepository.findByAccountstatusAndDeletionRequestedAtBefore(PENDING_STATUS, cutoff);
		if (expired == null || expired.isEmpty()) {
			return;
		}
		for (User user : expired) {
			try {
				accountPurger.purge(user.getId(), user.getUsername());
				System.out.println("[AccountDeletion] Purged account id=" + user.getId());
			} catch (Exception e) {
				// Log and continue so one bad record doesn't block the rest of the sweep.
				System.out.println("[AccountDeletion] Failed to purge account id=" + user.getId() + ": "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
