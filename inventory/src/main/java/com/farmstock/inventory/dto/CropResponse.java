package com.farmstock.inventory.dto;

import com.farmstock.inventory.entity.Crop;

public record CropResponse(Long id, String name, String unit) {

    public static CropResponse from(Crop crop) {
        return new CropResponse(crop.getId(), crop.getName(), crop.getUnit());
    }
}
