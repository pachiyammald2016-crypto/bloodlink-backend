package com.bloodlink.controller;

import com.bloodlink.service.EmailService;
import com.bloodlink.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = "*")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        
        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent successfully to " + email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String code) {
        if (email == null || code == null) {
            return ResponseEntity.badRequest().body("Email and code are required");
        }
        
        boolean isValid = otpService.verifyOtp(email, code);
        
        if (isValid) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email verified successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(Map.of("message", "Invalid or expired OTP"));
        }
    }
}
