package com.bloodlink.controller;

import com.bloodlink.model.Donor;
import com.bloodlink.service.DonorService;
import com.bloodlink.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class DonorController {

    @Autowired
    private DonorService donorService;

    @Autowired
    private com.bloodlink.repository.DonorRepository donorRepository;

    @Autowired
    private com.bloodlink.repository.BloodRequestRepository bloodRequestRepository;

    @DeleteMapping("/clear-all-database-data")
    public ResponseEntity<String> clearAllData() {
        donorRepository.deleteAll();
        bloodRequestRepository.deleteAll();
        return ResponseEntity.ok("Database cleared successfully!");
    }

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> registerDonor(@RequestBody Donor donor) {
        try {
            boolean isNewRegistration = (donor.getId() == null);
            Donor savedDonor = donorService.registerDonor(donor);
            
            if (isNewRegistration) {
                // Send welcome HTML email asynchronously to avoid blocking the API response
                new Thread(() -> {
                    emailService.sendWelcomeEmail(savedDonor);
                }).start();
            }
            
            return ResponseEntity.ok(savedDonor);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("Phone number is already registered to another account.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An internal error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginDonor(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        Optional<Donor> existing = donorService.getDonorByEmail(email);
        if (existing.isEmpty()) {
            return ResponseEntity.status(404).body("Please sign up first");
        }
        
        Donor donor = donorService.loginDonor(email, password);
        if (donor != null) {
            return ResponseEntity.ok(donor);
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getDonorByEmail(@PathVariable String email) {
        Optional<Donor> donor = donorService.getDonorByEmail(email);
        if (donor.isPresent()) {
            return ResponseEntity.ok(donor.get());
        } else {
            return ResponseEntity.status(404).body("Donor not found");
        }
    }

    @GetMapping
    public ResponseEntity<List<Donor>> getAllDonors(
            @RequestParam(value = "blood_group", required = false) String bloodGroup,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "availability", required = false) Boolean availability) {
        
        List<Donor> donors = donorService.getAllDonors(bloodGroup, city, availability);
        return ResponseEntity.ok(donors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donor> getDonorById(@PathVariable("id") Long id) {
        return donorService.getDonorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Donor> updateAvailability(
            @PathVariable("id") Long id,
            @RequestParam("availability") Boolean availability) {
        
        return donorService.updateAvailability(id, availability)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/match")
    public ResponseEntity<List<Donor>> findCompatibleDonors(
            @RequestParam("bloodGroup") String bloodGroup,
            @RequestParam("city") String city,
            @RequestParam(value = "area", required = false) String area) {
        
        List<Donor> matchedDonors = donorService.findCompatibleDonors(bloodGroup, city, area);
        return ResponseEntity.ok(matchedDonors);
    }
}
