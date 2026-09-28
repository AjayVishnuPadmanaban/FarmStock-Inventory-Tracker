package com.farmstock.inventory.service;

import com.farmstock.inventory.dto.CropRequest;
import com.farmstock.inventory.entity.Crop;
import com.farmstock.inventory.exception.ResourceNotFoundException;
import com.farmstock.inventory.repository.CropRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CropService {

    private final CropRepository cropRepository;

    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    @Transactional
    public Crop create(CropRequest request) {
        if (cropRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new IllegalArgumentException("Crop already exists: " + request.name());
        }

        Crop crop = new Crop(
                request.name().trim(),
                request.unit() == null || request.unit().isBlank() ? "kg" : request.unit().trim()
        );
        return cropRepository.save(crop);
    }

    @Transactional(readOnly = true)
    public List<Crop> findAll() {
        return cropRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Crop getById(Long id) {
        return cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + id));
    }

    /** Loads the crop with a write lock. Must be called inside a transaction. */
    @Transactional
    public Crop getByIdForUpdate(Long id) {
        return cropRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + id));
    }
}
