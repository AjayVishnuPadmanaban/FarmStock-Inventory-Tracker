package com.farmstock.inventory.controller;

import com.farmstock.inventory.dto.HarvestRequest;
import com.farmstock.inventory.dto.RevenueResponse;
import com.farmstock.inventory.dto.SaleRequest;
import com.farmstock.inventory.dto.StockResponse;
import com.farmstock.inventory.entity.HarvestBatch;
import com.farmstock.inventory.entity.Sale;
import com.farmstock.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/harvests")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> addHarvest(@Valid @RequestBody HarvestRequest request) {
        HarvestBatch batch = inventoryService.addHarvest(request);
        return Map.of(
                "message", "Harvest batch recorded successfully",
                "id", batch.getId()
        );
    }

    @PostMapping("/sales")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> recordSale(@Valid @RequestBody SaleRequest request) {
        Sale sale = inventoryService.recordSale(request);
        return Map.of(
                "message", "Sale recorded successfully",
                "id", sale.getId()
        );
    }

    @GetMapping("/stock")
    public List<StockResponse> getStock() {
        return inventoryService.getStock();
    }

    @GetMapping("/stock/{cropId}")
    public StockResponse getStockForCrop(@PathVariable Long cropId) {
        return inventoryService.stockFor(cropId);
    }

    @GetMapping("/reports/revenue")
    public List<RevenueResponse> revenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return inventoryService.revenue(startDate, endDate);
    }
}
