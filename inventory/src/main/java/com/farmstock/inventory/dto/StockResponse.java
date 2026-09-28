package com.farmstock.inventory.dto;

import java.math.BigDecimal;

public record StockResponse(
        Long cropId,
        String cropName,
        String unit,
        BigDecimal totalHarvested,
        BigDecimal totalSold,
        BigDecimal currentStock
) {}
