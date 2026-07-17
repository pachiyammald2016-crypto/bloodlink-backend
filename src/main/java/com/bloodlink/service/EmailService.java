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
                + "<div style='width: 450px; height: 283px; margin: 0 auto; border-radius: 12px; box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3); overflow: hidden; background-color: #aa0000; background-image: url(\"https://pachiyammald2016-crypto.github.io/bloodlink-frontend/card-bg-plain.jpg\"); background-size: cover; background-position: center; border: 1px solid #7a0000;'>"
                
                + "<!-- Content Container (pushed to the right) -->"
                + "<div style='width: 100%; height: 100%; box-sizing: border-box; padding-left: 210px; padding-top: 25px; padding-right: 25px; padding-bottom: 25px;'>"
                
                + "<!-- Header -->"
                + "<table width='100%' cellpadding='0' cellspacing='0' border='0' style='border-bottom: 1px solid rgba(255,255,255,0.3); padding-bottom: 10px; margin-bottom: 12px;'>"
                + "<tr>"
                + "<td align='left' valign='middle'>"
                + "<p style='margin: 0; font-size: 16px; font-weight: 900; letter-spacing: 1px; text-transform: uppercase; color: #ffffff;'>DONOR ID</p>"
                + "</td>"
                + "<td align='right' valign='middle'>"
                + "<div style='background-color: #ffffff; color: #cc0000; padding: 4px 10px; border-radius: 4px; font-size: 16px; font-weight: 900; box-shadow: 0 2px 5px rgba(0,0,0,0.3); display: inline-block;'>"
                + donor.getBloodGroup()
                + "</div>"
                + "</td>"
                + "</tr>"
                + "</table>"

                + "<!-- Details -->"
                + "<table width='100%' cellpadding='0' cellspacing='0' border='0' style='font-size: 13px; line-height: 1.4; color: #ffffff;'>"
                + "<tr>"
                + "<td style='color: #ffcccc; text-transform: uppercase; font-size: 9px; font-weight: bold; letter-spacing: 0.5px; padding-bottom: 2px;'>Name</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='font-size: 16px; font-weight: 900; text-transform: uppercase; padding-bottom: 8px;'>" + donor.getName() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='color: #ffcccc; text-transform: uppercase; font-size: 9px; font-weight: bold; letter-spacing: 0.5px; padding-bottom: 2px;'>Location</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='font-size: 13px; font-weight: 600; padding-bottom: 8px;'>" + donor.getArea() + ", " + donor.getCity() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='color: #ffcccc; text-transform: uppercase; font-size: 9px; font-weight: bold; letter-spacing: 0.5px; padding-bottom: 2px;'>Contact</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='font-size: 13px; font-weight: 600; padding-bottom: 8px;'>+91 " + donor.getPhone() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='color: #ffcccc; text-transform: uppercase; font-size: 9px; font-weight: bold; letter-spacing: 0.5px; padding-bottom: 2px;'>Card No.</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='font-size: 13px; font-family: monospace; font-weight: 900; letter-spacing: 1px; color: #ffeb3b;'>BLD-" + donorId + "</td>"
                + "</tr>"
                + "</table>"
                + "</div>"
                
                + "</div>"
                + "</div>"
                + "<p style='text-align: center; color: #888; font-size: 13px; margin-top: 30px; border-top: 1px solid #ddd; padding-top: 20px;'>Keep this card safe. Thank you for being a lifesaver and a hero to our community!</p>"
                + "</div>";
                
        sendEmailViaGAS(donor.getEmail(), subject, htmlContent);
        System.out.println("Welcome email sent successfully to " + donor.getEmail());
    }
}
