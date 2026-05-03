package com.healthcare.herplatform.payloads;

public class AppVersionResponse {

	private String platform;
	private String latestVersion;
	private String minSupportedVersion;
	private String updateUrl;

	public AppVersionResponse() {
	}

	public AppVersionResponse(String platform, String latestVersion, String minSupportedVersion, String updateUrl) {
		this.platform = platform;
		this.latestVersion = latestVersion;
		this.minSupportedVersion = minSupportedVersion;
		this.updateUrl = updateUrl;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}

	public String getMinSupportedVersion() {
		return minSupportedVersion;
	}

	public void setMinSupportedVersion(String minSupportedVersion) {
		this.minSupportedVersion = minSupportedVersion;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}
}
