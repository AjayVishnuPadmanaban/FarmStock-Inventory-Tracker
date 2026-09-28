package com.farmstock.inventory.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleRequest(
        @NotNull(message = "Crop ID is required") Long cropId,
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Quantity allows at most 10 digits and 2 decimals") BigDecimal quantity,
        @NotNull(message = "Price per unit is required")
        @Positive(message = "Price per unit must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Price allows at most 10 digits and 2 decimals") BigDecimal pricePerUnit,
        @NotNull(message = "Sale date is required") LocalDate saleDate,
        @Size(max = 150, message = "Buyer must be at most 150 characters") String buyer
) {}
