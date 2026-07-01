package com.healthcare.herplatform.config;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.healthcare.herplatform.entity.ERole;
import com.healthcare.herplatform.entity.Role;
import com.healthcare.herplatform.entity.User;
import com.healthcare.herplatform.repository.RoleRepository;
import com.healthcare.herplatform.repository.UserRepository;

/**
 * Idempotent startup seeder. Runs on every boot.
 *
 * <ol>
 *   <li><b>Roles:</b> ensures every {@link ERole} has a row in the {@code roles} table.
 *       The table is otherwise populated manually (ddl-auto only creates schema, not data),
 *       so this guarantees {@code ROLE_ADMIN} exists for the Board-of-Directors login.</li>
 *   <li><b>Board admin:</b> if {@code app.admin.seed.email} and {@code app.admin.seed.password}
 *       are both provided (via env), creates a single ADMIN account (status {@code Active}) that
 *       manages the External Trusted Resources directory. Credentials never live in source — they
 *       come from the environment. Existing users are never modified; passwords are never logged.</li>
 * </ol>
 */
@Component
public class DataSeeder implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Value("${app.admin.seed.enabled:true}")
	private boolean adminSeedEnabled;

	@Value("${app.admin.seed.username:boardadmin}")
	private String adminUsername;

	@Value("${app.admin.seed.email:}")
	private String adminEmail;

	@Value("${app.admin.seed.password:}")
	private String adminPassword;

	@Value("${app.admin.seed.phone:0000000000}")
	private String adminPhone;

	public DataSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.encoder = encoder;
	}

	@Override
	public void run(String... args) {
		seedRoles();
		seedBoardAdmin();
	}

	/** Insert any missing {@link ERole} rows. Safe to run repeatedly. */
	private void seedRoles() {
		for (ERole role : ERole.values()) {
			if (!roleRepository.findByName(role).isPresent()) {
				roleRepository.save(new Role(role));
				log.info("Seeded missing role: {}", role);
			}
		}
	}

	/** Create the Board-of-Directors ADMIN account from env config, if absent. */
	private void seedBoardAdmin() {
		if (!adminSeedEnabled) {
			return;
		}
		String email = adminEmail == null ? "" : adminEmail.trim();
		String username = adminUsername == null ? "" : adminUsername.trim();
		String password = adminPassword == null ? "" : adminPassword;

		if (email.isEmpty() || password.isEmpty()) {
			log.info("Board admin seed skipped: app.admin.seed.email / .password not set.");
			return;
		}
		if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
			log.info("Board admin seed skipped: account already exists (username/email in use).");
			return;
		}

		Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
				.orElseThrow(() -> new IllegalStateException("ROLE_ADMIN missing after role seeding"));
		Set<Role> roles = new HashSet<>();
		roles.add(adminRole);

		// Placeholder profile fields satisfy the entity's @NotBlank/@Size constraints; this account
		// only manages resource listings, it is not a patient/clinician profile.
		User admin = new User(
				new Date(),            // dateTime
				username,              // username
				email,                 // email
				encoder.encode(password),
				"1970-01-01",          // dob
				"NA",                  // age
				"Board",               // fname
				"NA",                  // mname
				"Director",            // lname
				"NA",                  // gender
				adminPhone,            // phone
				"India",               // country
				"NA",                  // state
				"NA",                  // city
				"NA",                  // address
				"Active",              // accountstatus -> can sign in immediately
				"9999-12-31",          // validityperiod (no practical expiry)
				"ADMBOARDDIRECTORS");  // regid
		admin.setRoles(roles);
		userRepository.save(admin);

		log.info("Board admin account created (ADMIN): username={}, email={}", username, email);
	}
}
