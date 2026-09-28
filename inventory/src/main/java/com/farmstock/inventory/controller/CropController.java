package com.farmstock.inventory.controller;

import com.farmstock.inventory.dto.CropRequest;
import com.farmstock.inventory.dto.CropResponse;
import com.farmstock.inventory.service.CropService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CropResponse create(@Valid @RequestBody CropRequest request) {
        return CropResponse.from(cropService.create(request));
    }

    @GetMapping
    public List<CropResponse> getAll() {
        return cropService.findAll().stream()
                .map(CropResponse::from)
                .toList();
    }
}
