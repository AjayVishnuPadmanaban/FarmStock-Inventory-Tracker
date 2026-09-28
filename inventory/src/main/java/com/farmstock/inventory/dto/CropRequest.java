package com.farmstock.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CropRequest(
        @NotBlank(message = "Crop name is required")
        @Size(max = 100, message = "Crop name must be at most 100 characters") String name,
        @Size(max = 50, message = "Unit must be at most 50 characters") String unit
) {}
