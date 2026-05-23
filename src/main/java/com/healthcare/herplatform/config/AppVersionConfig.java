package com.healthcare.herplatform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.version")
public class AppVersionConfig {

	private Platform android = new Platform();
	private Platform ios = new Platform();

	public Platform getAndroid() {
		return android;
	}

	public void setAndroid(Platform android) {
		this.android = android;
	}

	public Platform getIos() {
		return ios;
	}

	public void setIos(Platform ios) {
		this.ios = ios;
	}

	public static class Platform {
		private String latest;
		private String min;
		private String updateUrl;

		public String getLatest() {
			return latest;
		}

		public void setLatest(String latest) {
			this.latest = latest;
		}

		public String getMin() {
			return min;
		}

		public void setMin(String min) {
			this.min = min;
		}

		public String getUpdateUrl() {
			return updateUrl;
		}

		public void setUpdateUrl(String updateUrl) {
			this.updateUrl = updateUrl;
		}
	}
}
