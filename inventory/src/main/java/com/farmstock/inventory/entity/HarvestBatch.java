package com.farmstock.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "harvest_batches")
public class HarvestBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @NotNull(message = "Harvest date is required")
    @Column(nullable = false)
    private LocalDate harvestDate;

    @Column(length = 255)
    private String notes;

    public HarvestBatch() {}

    public Long getId() { return id; }
    public Crop getCrop() { return crop; }
    public BigDecimal getQuantity() { return quantity; }
    public LocalDate getHarvestDate() { return harvestDate; }
    public String getNotes() { return notes; }

    public void setId(Long id) { this.id = id; }
    public void setCrop(Crop crop) { this.crop = crop; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public void setHarvestDate(LocalDate harvestDate) { this.harvestDate = harvestDate; }
    public void setNotes(String notes) { this.notes = notes; }
}
