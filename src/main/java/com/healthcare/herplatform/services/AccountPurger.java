package com.healthcare.herplatform.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.herplatform.repository.ActivitiesRepository;
import com.healthcare.herplatform.repository.AlcoholTobaccoRepository;
import com.healthcare.herplatform.repository.AssignedActivitiesRepository;
import com.healthcare.herplatform.repository.AssignedUsersRepository;
import com.healthcare.herplatform.repository.ChatAttachmentsRepository;
import com.healthcare.herplatform.repository.DeviceTokenRepository;
import com.healthcare.herplatform.repository.Gad7Repository;
import com.healthcare.herplatform.repository.HadsRepository;
import com.healthcare.herplatform.repository.MessageRepository;
import com.healthcare.herplatform.repository.OtherActivitiesRepository;
import com.healthcare.herplatform.repository.Phq9Repository;
import com.healthcare.herplatform.repository.SmwtRepository;
import com.healthcare.herplatform.repository.UserJoinUserAssignedRepository;
import com.healthcare.herplatform.repository.UserRepository;
import com.healthcare.herplatform.repository.WeekDayRepository;
import com.healthcare.herplatform.repository.WeekNameRepository;

/**
 * Permanently removes a single user and all of their data in one transaction.
 * Kept as a separate bean so the {@code @Transactional} boundary applies when
 * invoked from {@link AccountDeletionService}'s scheduled sweep (a self-call
 * would bypass the proxy). One failing user therefore rolls back only that user.
 */
@Component
public class AccountPurger {

	@Autowired private UserRepository userRepository;
	@Autowired private ActivitiesRepository activitiesRepository;
	@Autowired private AssignedActivitiesRepository assignedActivitiesRepository;
	@Autowired private Gad7Repository gad7Repository;
	@Autowired private Phq9Repository phq9Repository;
	@Autowired private HadsRepository hadsRepository;
	@Autowired private SmwtRepository smwtRepository;
	@Autowired private AlcoholTobaccoRepository alcoholTobaccoRepository;
	@Autowired private OtherActivitiesRepository otherActivitiesRepository;
	@Autowired private WeekDayRepository weekDayRepository;
	@Autowired private WeekNameRepository weekNameRepository;
	@Autowired private AssignedUsersRepository assignedUsersRepository;
	@Autowired private UserJoinUserAssignedRepository userJoinUserAssignedRepository;
	@Autowired private DeviceTokenRepository deviceTokenRepository;
	@Autowired private MessageRepository messageRepository;
	@Autowired private ChatAttachmentsRepository chatAttachmentsRepository;

	@Transactional
	public void purge(Long userId, String username) {
		int uid = userId.intValue();

		// Health / activity records keyed by integer user id.
		activitiesRepository.deleteByUserId(uid);
		assignedActivitiesRepository.deleteByUserId(uid);
		gad7Repository.deleteByUserId(uid);
		phq9Repository.deleteByUserId(uid);
		hadsRepository.deleteByUserId(uid);
		smwtRepository.deleteByUserId(uid);
		alcoholTobaccoRepository.deleteByUserId(uid);
		otherActivitiesRepository.deleteByUserId(uid);

		// week_day references week_name, so remove the children first.
		weekDayRepository.deleteByUserId(uid);
		weekNameRepository.deleteByUserId(uid);

		// Assignment + join rows. deleteByAssignedUserId clears the row when the
		// user is a patient; deleteByCrsplUserId clears rows where the user is the
		// clinician so their patients aren't left pointing at a ghost specialist.
		assignedUsersRepository.deleteByAssignedUserId(userId);
		assignedUsersRepository.deleteByCrsplUserId(uid);
		userJoinUserAssignedRepository.deleteByUserId(userId);

		// Records keyed by username.
		deviceTokenRepository.deleteByUsername(username);
		// Purge stored chat-attachment blobs before the messages that reference them.
		List<String> attachmentIds = messageRepository.findFileIdsByUsername(username);
		if (attachmentIds != null && !attachmentIds.isEmpty()) {
			chatAttachmentsRepository.deleteAllById(attachmentIds);
		}
		messageRepository.deleteByUsername(username);

		// Finally the user row itself; JPA also clears the user_roles join rows.
		userRepository.deleteById(userId);
	}
}
