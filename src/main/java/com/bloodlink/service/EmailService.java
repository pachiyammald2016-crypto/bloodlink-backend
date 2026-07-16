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
        String subject = "Welcome to BloodLink, Lifesaver!";
        String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 500px; margin: auto; border: 1px solid #e0e0e0; border-radius: 12px; overflow: hidden;'>"
                + "<div style='background: linear-gradient(135deg, #e63946, #9b2226); padding: 30px 20px; text-align: center; color: white;'>"
                + "<h2 style='margin: 0; font-size: 24px; font-weight: bold; letter-spacing: 1px;'>BloodLink Donor</h2>"
                + "<p style='margin-top: 5px; opacity: 0.9;'>Welcome to the Lifesavers Community!</p>"
                + "</div>"
                + "<div style='padding: 30px; background-color: #ffffff;'>"
                + "<p style='font-size: 16px; margin-bottom: 20px;'>Hi <strong style='color: #e63946;'>" + donor.getName() + "</strong>,</p>"
                + "<p style='color: #555; line-height: 1.6; margin-bottom: 25px;'>"
                + "Thank you for registering as a blood donor on BloodLink. Your willingness to donate blood can save countless lives. Below are your registration details:"
                + "</p>"
                + "<div style='background: #f8f9fa; border: 1px solid #eeeeee; border-radius: 10px; padding: 20px;'>"
                + "<table style='width: 100%; border-collapse: collapse;'>"
                
                + "<tr>"
                + "<td style='padding: 10px 0; border-bottom: 1px solid #eeeeee; color: #777;'>Blood Group</td>"
                + "<td style='padding: 10px 0; border-bottom: 1px solid #eeeeee; text-align: right;'><strong style='color: #e63946; font-size: 18px;'>" + donor.getBloodGroup() + "</strong></td>"
                + "</tr>"
                
                + "<tr>"
                + "<td style='padding: 10px 0; border-bottom: 1px solid #eeeeee; color: #777;'>Location</td>"
                + "<td style='padding: 10px 0; border-bottom: 1px solid #eeeeee; text-align: right; color: #333;'><strong>" + donor.getArea() + ", " + donor.getCity() + "</strong></td>"
                + "</tr>"
                
                + "<tr>"
                + "<td style='padding: 10px 0; border-bottom: 1px solid #eeeeee; color: #777;'>Phone</td>"
                + "<td style='padding: 10px 0; border-bottom: 1px solid #eeeeee; text-align: right; color: #333;'><strong>+91 " + donor.getPhone() + "</strong></td>"
                + "</tr>"
                
                + "<tr>"
                + "<td style='padding: 10px 0; color: #777;'>Email</td>"
                + "<td style='padding: 10px 0; text-align: right; color: #333;'><strong>" + donor.getEmail() + "</strong></td>"
                + "</tr>"
                
                + "</table>"
                + "</div>"
                + "<div style='margin-top: 30px; text-align: center;'>"
                + "<p style='color: #888; font-size: 14px;'>You can update your availability anytime by logging into BloodLink.</p>"
                + "</div>"
                + "</div>"
                + "</div>";
                
        sendEmailViaGAS(donor.getEmail(), subject, htmlContent);
        System.out.println("Welcome email sent successfully to " + donor.getEmail());
    }
}
