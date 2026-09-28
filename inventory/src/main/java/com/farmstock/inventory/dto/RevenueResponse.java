package com.farmstock.inventory.dto;

import java.math.BigDecimal;

public record RevenueResponse(
        Long cropId,
        String cropName,
        BigDecimal revenue
) {}
