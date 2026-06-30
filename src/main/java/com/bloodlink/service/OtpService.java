package com.bloodlink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    // In-memory store for OTPs: Target (email) -> OtpDetails
    private final ConcurrentHashMap<String, OtpDetails> otpStorage = new ConcurrentHashMap<>();

    private static class OtpDetails {
        String code;
        LocalDateTime expiryTime;

        OtpDetails(String code) {
            this.code = code;
            this.expiryTime = LocalDateTime.now().plusMinutes(5); // 5 minutes validity
        }

        boolean isExpired() {
            return LocalDateTime.now().isAfter(expiryTime);
        }
    }

    public String generateAndSendOtp(String email) {
        // Generate a 4-digit OTP
        String otp = String.format("%04d", new Random().nextInt(10000));
        otpStorage.put(email, new OtpDetails(otp));

        System.out.println("[OTP SYSTEM] Generated OTP for " + email + " is: " + otp);

        // Send real Email if JavaMailSender is configured and username is not empty
        if (mailSender != null && !mailUsername.isEmpty()) {
            try {
                sendOtpEmail(email, otp);
                System.out.println("[OTP SYSTEM] Real Email OTP successfully sent to " + email);
            } catch (Exception e) {
                System.err.println("[OTP SYSTEM] Failed to send email: " + e.getMessage());
                System.err.println("[OTP SYSTEM] Fallback: Please use code " + otp + " from console to verify.");
            }
        } else {
            System.out.println("[OTP SYSTEM] Email SMTP credentials not configured. Please use code " + otp + " from console for testing.");
        }

        return otp;
    }

    public boolean verifyOtp(String email, String code) {
        OtpDetails details = otpStorage.get(email);
        if (details == null) {
            return false;
        }
        if (details.isExpired()) {
            otpStorage.remove(email);
            return false;
        }
        boolean isValid = details.code.equals(code);
        if (isValid) {
            otpStorage.remove(email); // Burn OTP after successful verification
        }
        return isValid;
    }

    private void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(toEmail);
        message.setSubject("BloodLink Registration Verification Code");
        message.setText("Hello,\n\nYour verification code for BloodLink donor registration is: " + otp + "\n\nThis code is valid for 5 minutes. Please do not share it with anyone.\n\nThank you,\nBloodLink Team");
        mailSender.send(message);
    }
}
