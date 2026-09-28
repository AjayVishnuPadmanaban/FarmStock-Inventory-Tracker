package com.farmstock.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Crop name is required")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 50)
    private String unit = "kg";

    @OneToMany(mappedBy = "crop")
    private List<HarvestBatch> harvestBatches = new ArrayList<>();

    @OneToMany(mappedBy = "crop")
    private List<Sale> sales = new ArrayList<>();

    public Crop() {}

    public Crop(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getUnit() { return unit; }
    public List<HarvestBatch> getHarvestBatches() { return harvestBatches; }
    public List<Sale> getSales() { return sales; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setHarvestBatches(List<HarvestBatch> harvestBatches) { this.harvestBatches = harvestBatches; }
    public void setSales(List<Sale> sales) { this.sales = sales; }
}
