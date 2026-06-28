package com.bloodlink.service;

import com.bloodlink.model.BloodRequest;
import com.bloodlink.repository.BloodRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService {

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    public BloodRequest createRequest(BloodRequest request) {
        if (request.getStatus() == null) {
            request.setStatus("Active");
        }
        return bloodRequestRepository.save(request);
    }

    public List<BloodRequest> getAllRequests() {
        return bloodRequestRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<BloodRequest> getRequestById(Long id) {
        return bloodRequestRepository.findById(id);
    }

    public Optional<BloodRequest> updateStatus(Long id, String status) {
        return bloodRequestRepository.findById(id).map(request -> {
            request.setStatus(status);
            return bloodRequestRepository.save(request);
        });
    }
}
