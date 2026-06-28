package com.bloodlink.repository;

import com.bloodlink.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    List<Donor> findByBloodGroup(String bloodGroup);
    List<Donor> findByCity(String city);
    List<Donor> findByAvailability(Boolean availability);
    List<Donor> findByBloodGroupAndCityAndAvailability(String bloodGroup, String city, Boolean availability);
}
