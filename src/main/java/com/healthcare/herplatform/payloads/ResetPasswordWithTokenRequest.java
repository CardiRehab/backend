package com.healthcare.herplatform.payloads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ResetPasswordWithTokenRequest {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 8, max = 24)
    private String newPassword;

    @NotBlank
    private String confirmPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
