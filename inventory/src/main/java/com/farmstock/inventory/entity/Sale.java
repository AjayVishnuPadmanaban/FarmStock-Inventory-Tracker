package com.farmstock.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales")
public class Sale {

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

    @NotNull(message = "Price per unit is required")
    @Positive(message = "Price per unit must be greater than zero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerUnit;

    @NotNull(message = "Sale date is required")
    @Column(nullable = false)
    private LocalDate saleDate;

    @Column(length = 150)
    private String buyer;

    public Sale() {}

    public Long getId() { return id; }
    public Crop getCrop() { return crop; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public LocalDate getSaleDate() { return saleDate; }
    public String getBuyer() { return buyer; }

    public void setId(Long id) { this.id = id; }
    public void setCrop(Crop crop) { this.crop = crop; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
    public void setBuyer(String buyer) { this.buyer = buyer; }
}
