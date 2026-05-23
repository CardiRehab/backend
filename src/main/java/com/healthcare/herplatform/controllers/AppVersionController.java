package com.healthcare.herplatform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.herplatform.config.AppVersionConfig;
import com.healthcare.herplatform.payloads.AppVersionResponse;

@RestController
@RequestMapping("/api/auth")
public class AppVersionController {

	@Autowired
	private AppVersionConfig appVersionConfig;

	@GetMapping("/app-version")
	public ResponseEntity<AppVersionResponse> getAppVersion(
			@RequestParam(value = "platform", required = false) String platform) {

		String normalized = platform == null ? "" : platform.trim().toLowerCase();
		AppVersionConfig.Platform cfg;
		String resolved;

		if ("ios".equals(normalized)) {
			cfg = appVersionConfig.getIos();
			resolved = "ios";
		} else {
			cfg = appVersionConfig.getAndroid();
			resolved = "android";
		}

		return ResponseEntity.ok(new AppVersionResponse(
				resolved,
				cfg.getLatest(),
				cfg.getMin(),
				cfg.getUpdateUrl()));
	}
}
