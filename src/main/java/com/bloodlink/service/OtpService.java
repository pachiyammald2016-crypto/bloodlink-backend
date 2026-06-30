package com.bloodlink.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;
import java.time.LocalDateTime;

@Service
public class OtpService {

    private static class OtpDetails {
        String otp;
        LocalDateTime expiryTime;

        OtpDetails(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
    }

    // Stores email -> OtpDetails
    private final ConcurrentHashMap<String, OtpDetails> otpCache = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateOtp(String email) {
        // Generate a 6 digit OTP
        String otp = String.format("%06d", random.nextInt(1000000));
        
        // OTP valid for 5 minutes
        OtpDetails details = new OtpDetails(otp, LocalDateTime.now().plusMinutes(5));
        otpCache.put(email, details);
        
        return otp;
    }

    public boolean verifyOtp(String email, String inputOtp) {
        OtpDetails details = otpCache.get(email);
        
        if (details == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(details.expiryTime)) {
            otpCache.remove(email); // Expired
            return false;
        }
        
        if (details.otp.equals(inputOtp)) {
            otpCache.remove(email); // OTP used successfully, remove it
            return true;
        }
        
        return false;
    }
}
