package com.bloodlink.service;

import com.bloodlink.model.Donor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final String GOOGLE_APPS_SCRIPT_URL = "https://script.google.com/macros/s/AKfycbxerRf9UpHf81fSa7GjGM00oW3E2K3rYrMfwPhdibBc85tQOXtNVq3jtls0LJSUzrwy/exec";
    private final RestTemplate restTemplate = new RestTemplate();

    private void sendEmailViaGAS(String to, String subject, String body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> payload = new HashMap<>();
            payload.put("to", to);
            payload.put("subject", subject);
            payload.put("body", body);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_APPS_SCRIPT_URL, request, String.class);
            
            System.out.println("Email API Response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send email via API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendOtpEmail(String toEmail, String otpCode) {
        String subject = "BloodLink - Your Verification Code";
        String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 500px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>"
                + "<h2 style='color: #e63946; text-align: center;'>BloodLink Donor Verification</h2>"
                + "<p>Hello,</p>"
                + "<p>Thank you for initiating the registration process. Please use the following 6-digit OTP to verify your email address:</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<span style='font-size: 24px; font-weight: bold; padding: 10px 20px; background-color: #f8f9fa; border-radius: 5px; border: 1px dashed #e63946; color: #e63946;'>" + otpCode + "</span>"
                + "</div>"
                + "<p>This code is valid for 5 minutes. If you did not request this, please ignore this email.</p>"
                + "<br>"
                + "<p>Best regards,<br><strong>BloodLink Team</strong></p>"
                + "</div>";
                
        sendEmailViaGAS(toEmail, subject, htmlContent);
        System.out.println("OTP email sent successfully to " + toEmail);
    }

    public void sendWelcomeEmail(Donor donor) {
        String donorId = donor.getId() != null ? String.format("%04d", donor.getId()) : String.valueOf(System.currentTimeMillis()).substring(8);
        String subject = "Welcome to BloodLink - Your Donor ID Card";
        
        String htmlContent = "<div style='font-family: \"Helvetica Neue\", Arial, sans-serif; max-width: 550px; margin: auto; background-color: #f9f9f9; padding: 30px; border-radius: 12px;'>"
                + "<p style='text-align: center; color: #444; font-size: 16px; margin-bottom: 25px; line-height: 1.5;'>Hi <b>" + donor.getName() + "</b>,<br>Welcome to the BloodLink community! Here is your official Donor Identification Card.</p>"
                
                + "<!-- ID Card Container -->"
                + "<div style='width: 420px; height: 265px; margin: 0 auto; border-radius: 14px; box-shadow: 0 15px 25px rgba(230, 57, 70, 0.2); overflow: hidden; background-color: #f0ecff; background-image: url(\"https://pachiyammald2016-crypto.github.io/bloodlink-frontend/email-bg.png\"); background-size: cover; background-position: center; border: 1px solid #d0c0ff;'>"
                
                + "<!-- Gradient Overlay -->"
                + "<div style='width: 100%; height: 100%; background: rgba(255, 255, 255, 0.1); color: #111; box-sizing: border-box; padding: 25px;'>"
                
                + "<!-- Header -->"
                + "<table width='100%' cellpadding='0' cellspacing='0' border='0' style='border-bottom: 1px solid rgba(0,0,0,0.15); padding-bottom: 15px; margin-bottom: 15px;'>"
                + "<tr>"
                + "<td align='left' valign='middle'>"
                + "<h2 style='margin: 0; font-size: 24px; font-weight: 900; letter-spacing: 1.5px; text-transform: uppercase; color: #000;'>BLOODLINK</h2>"
                + "<p style='margin: 4px 0 0; font-size: 11px; font-weight: 600; letter-spacing: 0.5px; color: #333;'>DONOR IDENTIFICATION CARD</p>"
                + "</td>"
                + "<td align='right' valign='middle'>"
                + "<div style='background-color: #111; color: white; padding: 8px 16px; border-radius: 8px; font-size: 24px; font-weight: 900; box-shadow: 0 4px 10px rgba(0,0,0,0.15); display: inline-block;'>"
                + donor.getBloodGroup()
                + "</div>"
                + "</td>"
                + "</tr>"
                + "</table>"

                + "<!-- Details -->"
                + "<table width='100%' cellpadding='0' cellspacing='0' border='0' style='font-size: 14px; line-height: 1.7; color: #000;'>"
                + "<tr>"
                + "<td width='30%' style='color: #444; text-transform: uppercase; font-size: 10px; font-weight: bold; letter-spacing: 1px; padding-bottom: 6px;'>Name</td>"
                + "<td width='70%' style='font-size: 18px; font-weight: 900; text-transform: uppercase; padding-bottom: 6px;'>" + donor.getName() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='color: #444; text-transform: uppercase; font-size: 10px; font-weight: bold; letter-spacing: 1px; padding-bottom: 6px;'>Location</td>"
                + "<td style='font-size: 15px; font-weight: 600; padding-bottom: 6px;'>" + donor.getArea() + ", " + donor.getCity() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='color: #444; text-transform: uppercase; font-size: 10px; font-weight: bold; letter-spacing: 1px; padding-bottom: 6px;'>Contact</td>"
                + "<td style='font-size: 15px; font-weight: 600; padding-bottom: 6px;'>+91 " + donor.getPhone() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='color: #444; text-transform: uppercase; font-size: 10px; font-weight: bold; letter-spacing: 1px;'>Card No.</td>"
                + "<td style='font-size: 15px; font-family: monospace; font-weight: 900; letter-spacing: 1px; color: #b30000;'>BLD-" + donorId + "</td>"
                + "</tr>"
                + "</table>"
                
                + "</div>"
                + "</div>"
                + "<p style='text-align: center; color: #888; font-size: 13px; margin-top: 30px; border-top: 1px solid #ddd; padding-top: 20px;'>Keep this card safe. Thank you for being a lifesaver and a hero to our community!</p>"
                + "</div>";
                
        sendEmailViaGAS(donor.getEmail(), subject, htmlContent);
        System.out.println("Welcome email sent successfully to " + donor.getEmail());
    }
}
