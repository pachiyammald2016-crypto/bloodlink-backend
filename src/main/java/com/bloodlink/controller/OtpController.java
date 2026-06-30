package com.bloodlink.controller;

import com.bloodlink.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();
        if (email == null || !email.contains("@")) {
            response.put("success", false);
            response.put("message", "Invalid email address format.");
            return ResponseEntity.badRequest().body(response);
        }

        String cleanedEmail = email.trim();
        otpService.generateAndSendOtp(cleanedEmail);

        response.put("success", true);
        response.put("message", "OTP sent successfully.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyOtp(
            @RequestParam("email") String email,
            @RequestParam("code") String code) {
        
        Map<String, Object> response = new HashMap<>();
        if (email == null || code == null) {
            response.put("success", false);
            response.put("message", "Email and OTP code are required.");
            return ResponseEntity.badRequest().body(response);
        }

        boolean isValid = otpService.verifyOtp(email.trim(), code.trim());
        response.put("success", isValid);
        response.put("message", isValid ? "OTP verified successfully." : "Invalid or expired OTP code.");

        if (isValid) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
