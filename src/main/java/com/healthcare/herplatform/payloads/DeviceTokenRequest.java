package com.healthcare.herplatform.payloads;

/** Body for register/unregister device-token endpoints. */
public class DeviceTokenRequest {

	private String token;
	private String platform; // "android" | "ios" (optional)

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
}
