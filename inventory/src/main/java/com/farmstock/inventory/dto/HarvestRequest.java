package com.farmstock.inventory.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HarvestRequest(
        @NotNull(message = "Crop ID is required") Long cropId,
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Quantity allows at most 10 digits and 2 decimals") BigDecimal quantity,
        @NotNull(message = "Harvest date is required") LocalDate harvestDate,
        @Size(max = 255, message = "Notes must be at most 255 characters") String notes
) {}
