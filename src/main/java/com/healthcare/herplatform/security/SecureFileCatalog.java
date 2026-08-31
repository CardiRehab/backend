package com.healthcare.herplatform.security;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.healthcare.herplatform.config.SecureFilesConfig;
import com.healthcare.herplatform.config.SecureFilesConfig.CatalogItem;

/**
 * The closed set of files {@code GET /api/auth/files/{filename}} will serve (V-02).
 *
 * <p><b>What was wrong.</b> The endpoint took the URL segment as a filesystem path and served
 * whatever was under {@code secure.files.base-path} by that name to any logged-in caller. Path
 * traversal was blocked, so the blast radius was one directory — but the control was directory
 * hygiene, not code: the endpoint was safe only for as long as nobody hand-placed something
 * sensitive in there, which cannot be evidenced to an auditor. It also served
 * {@code Content-Disposition: inline} with a type sniffed from the file, so an HTML or SVG file
 * dropped into the directory would have executed on the application's own origin and could read
 * the JWT out of {@code localStorage} (V-11).
 *
 * <p><b>What this does.</b> The URL segment is no longer a path. It is looked up in a fixed
 * catalogue declared in configuration, and a filename that is not in that catalogue is refused
 * before the filesystem is touched at all. Adding material is a configuration change, not a code
 * change, and the set of things the endpoint can ever return is now enumerable from
 * {@code application.properties} rather than from whatever is on the disk today.
 *
 * <p><b>Why the filename stayed in the URL.</b> Opaque ids would have been cleaner, but the shipped
 * mobile app hard-codes these filenames and a change there ships through app-store review. As in
 * V-01, the fix validates what the client sends rather than changing the contract.
 *
 * <p>Refusals are logged at WARN with the requested filename, the principal and the endpoint —
 * never with file contents — and surface as a bare 403 through {@link EmptyBodyAccessDeniedHandler},
 * so "not catalogued" and "does not exist" are indistinguishable to a caller guessing names.
 */
@Component
public class SecureFileCatalog {

	private static final Logger log = LoggerFactory.getLogger(SecureFileCatalog.class);

	private final SecureFilesConfig secureFilesConfig;

	/** Filename to item. Built once at startup and never mutated, so it needs no synchronisation. */
	private Map<String, CatalogItem> byFilename = Collections.emptyMap();

	public SecureFileCatalog(SecureFilesConfig secureFilesConfig) {
		this.secureFilesConfig = secureFilesConfig;
	}

	@PostConstruct
	void buildIndex() {
		Map<String, CatalogItem> index = new LinkedHashMap<>();

		for (CatalogItem item : secureFilesConfig.getCatalog()) {
			if (isBlank(item.getFilename()) || isBlank(item.getContentType())) {
				// A half-declared entry would otherwise become a filename that resolves to a null
				// content type at request time. Drop it at startup, loudly, rather than at 3am.
				log.error("SECURE_FILE_CATALOG_INVALID id={} filename={} reason=filename and content-type are both required",
						item.getId(), item.getFilename());
				continue;
			}
			CatalogItem clash = index.put(item.getFilename(), item);
			if (clash != null) {
				log.error("SECURE_FILE_CATALOG_DUPLICATE filename={} kept-id={} discarded-id={}",
						item.getFilename(), item.getId(), clash.getId());
			}
		}

		this.byFilename = Collections.unmodifiableMap(index);
		log.info("SECURE_FILE_CATALOG_LOADED entries={} base-path={}", index.size(),
				secureFilesConfig.getBasePath());
		warnAboutMissingFiles();
	}

	/**
	 * The catalogued item for this filename, or a refusal.
	 *
	 * @throws AccessDeniedException if the filename is not in the catalogue — including every
	 *     traversal attempt, every guess at a file that happens to exist on disk, and every guess at
	 *     one that does not.
	 */
	public CatalogItem require(Authentication authentication, String requestedFilename) {
		if (isBlank(requestedFilename)) {
			throw deny(authentication, String.valueOf(requestedFilename), "no filename requested");
		}
		CatalogItem item = byFilename.get(requestedFilename);
		if (item == null) {
			throw deny(authentication, requestedFilename, "filename not in catalogue");
		}
		return item;
	}

	/** Every catalogued filename, in declaration order. For tests and diagnostics. */
	public Set<String> filenames() {
		return byFilename.keySet();
	}

	/**
	 * A catalogued file that is not on disk is a deployment fault, not an attack, and the operator
	 * should hear about it at startup rather than from a patient. It is a warning and not a failed
	 * startup: a missing education PDF is not a reason to take the whole service down.
	 */
	private void warnAboutMissingFiles() {
		String basePath = secureFilesConfig.getBasePath();
		if (isBlank(basePath)) {
			log.error("SECURE_FILE_CATALOG_INVALID reason=secure.files.base-path is not set");
			return;
		}
		Path baseDir = Paths.get(basePath).toAbsolutePath().normalize();
		for (String filename : byFilename.keySet()) {
			if (!Files.isReadable(baseDir.resolve(filename))) {
				log.warn("SECURE_FILE_MISSING filename={} base-path={}", filename, baseDir);
			}
		}
	}

	private AccessDeniedException deny(Authentication authentication, String requestedFilename,
			String reason) {
		// Same WARN shape as PatientAccessGuard so one grep finds every refused request. The
		// requested filename is attacker-controlled text, so it is logged as the subject and
		// nothing is ever read from the file itself.
		log.warn("AUTHZ_DENY principal={} roles={} subject={} endpoint={} reason={}",
				authentication != null ? authentication.getName() : "anonymous",
				authentication != null && authentication.getAuthorities() != null
						? authentication.getAuthorities().toString()
						: "[]",
				requestedFilename,
				endpoint(),
				reason);
		return new AccessDeniedException("Forbidden");
	}

	private static boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private static String endpoint() {
		ServletRequestAttributes attributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return "unknown";
		}
		HttpServletRequest request = attributes.getRequest();
		return request.getMethod() + " " + request.getRequestURI();
	}
}
