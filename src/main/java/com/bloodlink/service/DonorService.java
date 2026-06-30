package com.bloodlink.service;

import com.bloodlink.model.Donor;
import com.bloodlink.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DonorService {

    @Autowired
    private DonorRepository donorRepository;

    public Donor registerDonor(Donor donor) {
        // If availability is not explicitly set, default to true
        if (donor.getAvailability() == null) {
            donor.setAvailability(true);
        }
        
        // If donor already exists with this email, update instead of creating new
        Optional<Donor> existing = donorRepository.findByEmail(donor.getEmail());
        if (existing.isPresent()) {
            Donor e = existing.get();
            e.setName(donor.getName());
            e.setBloodGroup(donor.getBloodGroup());
            e.setCity(donor.getCity());
            e.setArea(donor.getArea());
            e.setPhone(donor.getPhone());
            e.setLastDonation(donor.getLastDonation());
            e.setAvailability(donor.getAvailability());
            e.setBio(donor.getBio());
            if (donor.getPassword() != null && !donor.getPassword().isEmpty()) {
                e.setPassword(donor.getPassword());
            }
            return donorRepository.save(e);
        }
        
        return donorRepository.save(donor);
    }

    public Donor loginDonor(String email, String password) {
        Optional<Donor> existing = donorRepository.findByEmail(email);
        if (existing.isPresent()) {
            Donor donor = existing.get();
            if (donor.getPassword() != null && donor.getPassword().equals(password)) {
                return donor;
            }
        }
        return null;
    }

    public Optional<Donor> getDonorByEmail(String email) {
        return donorRepository.findByEmail(email);
    }

    public List<Donor> getAllDonors(String bloodGroup, String city, Boolean availability) {
        List<Donor> allDonors = donorRepository.findAll();
        
        return allDonors.stream()
                .filter(d -> bloodGroup == null || bloodGroup.equalsIgnoreCase("All") || d.getBloodGroup().equalsIgnoreCase(bloodGroup))
                .filter(d -> city == null || city.equalsIgnoreCase("All") || d.getCity().equalsIgnoreCase(city))
                .filter(d -> availability == null || d.getAvailability().equals(availability))
                .collect(Collectors.toList());
    }

    public Optional<Donor> getDonorById(Long id) {
        return donorRepository.findById(id);
    }

    public Optional<Donor> updateAvailability(Long id, Boolean availability) {
        return donorRepository.findById(id).map(donor -> {
            donor.setAvailability(availability);
            return donorRepository.save(donor);
        });
    }

    public List<Donor> findCompatibleDonors(String recipientBloodGroup, String city, String area) {
        List<Donor> allDonors = donorRepository.findAll();
        List<String> compatibleGroups = getCompatibleDonorGroups(recipientBloodGroup);

        return allDonors.stream()
                // Must be available
                .filter(d -> d.getAvailability() != null && d.getAvailability())
                // Must be compatible blood group
                .filter(d -> compatibleGroups.contains(d.getBloodGroup()))
                // Must match city OR match area
                .filter(d -> {
                    boolean cityMatch = city != null && !city.trim().isEmpty() && d.getCity() != null && d.getCity().equalsIgnoreCase(city);
                    boolean areaMatch = area != null && !area.trim().isEmpty() && d.getArea() != null && d.getArea().equalsIgnoreCase(area);
                    return cityMatch || areaMatch;
                })
                // Sort by: exact city match first, then others
                .sorted((d1, d2) -> {
                    boolean d1CityMatch = city != null && d1.getCity() != null && d1.getCity().equalsIgnoreCase(city);
                    boolean d2CityMatch = city != null && d2.getCity() != null && d2.getCity().equalsIgnoreCase(city);
                    
                    if (d1CityMatch && !d2CityMatch) return -1;
                    if (!d1CityMatch && d2CityMatch) return 1;
                    
                    // Secondary sort by area if city match status is the same
                    boolean d1AreaMatch = area != null && d1.getArea() != null && d1.getArea().equalsIgnoreCase(area);
                    boolean d2AreaMatch = area != null && d2.getArea() != null && d2.getArea().equalsIgnoreCase(area);
                    
                    if (d1AreaMatch && !d2AreaMatch) return -1;
                    if (!d1AreaMatch && d2AreaMatch) return 1;
                    
                    return 0;
                })
                .collect(Collectors.toList());
    }

    private List<String> getCompatibleDonorGroups(String recipientGroup) {
        if (recipientGroup == null) return List.of();
        switch (recipientGroup.toUpperCase().trim()) {
            case "O-": return List.of("O-");
            case "O+": return List.of("O-", "O+");
            case "A-": return List.of("O-", "A-");
            case "A+": return List.of("O-", "O+", "A-", "A+");
            case "B-": return List.of("O-", "B-");
            case "B+": return List.of("O-", "O+", "B-", "B+");
            case "AB-": return List.of("O-", "A-", "B-", "AB-");
            case "AB+": return List.of("O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+");
            default: return List.of();
        }
    }
}
