package com.healthcare.herplatform.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from.email}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetLink = frontendUrl + "/ForgotPassword?token=" + token;
        String htmlBody = buildPasswordResetEmailHtml(resetLink);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + resendApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("from", "CardiRehab <" + fromEmail + ">");
        body.put("to", Collections.singletonList(toEmail));
        body.put("subject", "Reset your CardiRehab password");
        body.put("html", htmlBody);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(RESEND_API_URL, request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }

    private String buildPasswordResetEmailHtml(String resetLink) {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 32px;'>"
                + "<h2 style='color: #001062;'>Reset Your CardiRehab Password</h2>"
                + "<p style='color: #333;'>You requested a password reset for your CardiRehab account.</p>"
                + "<p style='color: #333;'>Click the button below to reset your password. "
                + "This link expires in <strong>1 hour</strong>.</p>"
                + "<a href='" + resetLink + "' "
                + "style='display: inline-block; padding: 12px 28px; background-color: #001062; "
                + "color: white; text-decoration: none; border-radius: 6px; "
                + "font-weight: bold; margin: 20px 0;'>Reset Password</a>"
                + "<p style='color: #666; font-size: 14px;'>If you did not request a password reset, "
                + "you can safely ignore this email. Your password will not change.</p>"
                + "<p style='color: #999; font-size: 12px;'>If the button above does not work, "
                + "copy and paste this link into your browser:<br>"
                + "<a href='" + resetLink + "' style='color: #001062;'>" + resetLink + "</a></p>"
                + "<hr style='border: none; border-top: 1px solid #eee; margin-top: 32px;'/>"
                + "<p style='color: #999; font-size: 12px;'>"
                + "CardiRehab &mdash; Safe, Doctor-Supervised Cardiac Rehab at Home</p>"
                + "</div>";
    }
}
