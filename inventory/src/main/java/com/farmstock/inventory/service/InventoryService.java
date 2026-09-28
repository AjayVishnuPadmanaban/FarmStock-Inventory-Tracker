package com.farmstock.inventory.service;

import com.farmstock.inventory.dto.HarvestRequest;
import com.farmstock.inventory.dto.RevenueResponse;
import com.farmstock.inventory.dto.SaleRequest;
import com.farmstock.inventory.dto.StockResponse;
import com.farmstock.inventory.entity.Crop;
import com.farmstock.inventory.entity.HarvestBatch;
import com.farmstock.inventory.entity.Sale;
import com.farmstock.inventory.exception.InsufficientStockException;
import com.farmstock.inventory.repository.HarvestBatchRepository;
import com.farmstock.inventory.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final CropService cropService;
    private final HarvestBatchRepository harvestRepository;
    private final SaleRepository saleRepository;

    public InventoryService(
            CropService cropService,
            HarvestBatchRepository harvestRepository,
            SaleRepository saleRepository) {
        this.cropService = cropService;
        this.harvestRepository = harvestRepository;
        this.saleRepository = saleRepository;
    }

    @Transactional
    public HarvestBatch addHarvest(HarvestRequest request) {
        Crop crop = cropService.getById(request.cropId());

        HarvestBatch batch = new HarvestBatch();
        batch.setCrop(crop);
        batch.setQuantity(request.quantity());
        batch.setHarvestDate(request.harvestDate());
        batch.setNotes(request.notes());

        return harvestRepository.save(batch);
    }

    @Transactional
    public Sale recordSale(SaleRequest request) {
        // Lock the crop row so two simultaneous sales cannot both pass the stock check.
        Crop crop = cropService.getByIdForUpdate(request.cropId());

        BigDecimal currentStock = totalHarvested(crop.getId()).subtract(totalSold(crop.getId()));

        if (request.quantity().compareTo(currentStock) > 0) {
            throw new InsufficientStockException(
                    "Sale rejected. Available stock for " + crop.getName()
                            + " is " + currentStock + " " + crop.getUnit()
            );
        }

        Sale sale = new Sale();
        sale.setCrop(crop);
        sale.setQuantity(request.quantity());
        sale.setPricePerUnit(request.pricePerUnit());
        sale.setSaleDate(request.saleDate());
        sale.setBuyer(request.buyer());

        return saleRepository.save(sale);
    }

    @Transactional(readOnly = true)
    public List<StockResponse> getStock() {
        return cropService.findAll().stream()
                .map(this::toStockResponse)
                .sorted(Comparator.comparing(StockResponse::cropName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    @Transactional(readOnly = true)
    public StockResponse stockFor(Long cropId) {
        return toStockResponse(cropService.getById(cropId));
    }

    private StockResponse toStockResponse(Crop crop) {
        BigDecimal harvested = totalHarvested(crop.getId());
        BigDecimal sold = totalSold(crop.getId());
        return new StockResponse(
                crop.getId(),
                crop.getName(),
                crop.getUnit(),
                harvested,
                sold,
                harvested.subtract(sold)
        );
    }

    private BigDecimal totalHarvested(Long cropId) {
        BigDecimal total = harvestRepository.sumQuantityByCrop(cropId);
        return total == null ? BigDecimal.ZERO : total;
    }

    private BigDecimal totalSold(Long cropId) {
        BigDecimal total = saleRepository.sumQuantityByCrop(cropId);
        return total == null ? BigDecimal.ZERO : total;
    }

    @Transactional(readOnly = true)
    public List<RevenueResponse> revenue(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Map<Long, Crop> crops = cropService.findAll().stream()
                .collect(Collectors.toMap(Crop::getId, Function.identity()));

        Map<Long, BigDecimal> revenueByCrop = saleRepository.findBySaleDateBetween(startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(
                        sale -> sale.getCrop().getId(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                sale -> sale.getQuantity().multiply(sale.getPricePerUnit()),
                                BigDecimal::add
                        )
                ));

        List<RevenueResponse> result = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : revenueByCrop.entrySet()) {
            Crop crop = crops.get(entry.getKey());
            if (crop != null) {
                result.add(new RevenueResponse(crop.getId(), crop.getName(), entry.getValue()));
            }
        }

        return result.stream()
                .sorted(Comparator.comparing(RevenueResponse::cropName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }
}
