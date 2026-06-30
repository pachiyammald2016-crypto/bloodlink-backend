package com.bloodlink.controller;

import com.bloodlink.model.Donor;
import com.bloodlink.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<Donor> registerDonor(@RequestBody Donor donor) {
        Donor savedDonor = donorService.registerDonor(donor);
        return ResponseEntity.ok(savedDonor);
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
