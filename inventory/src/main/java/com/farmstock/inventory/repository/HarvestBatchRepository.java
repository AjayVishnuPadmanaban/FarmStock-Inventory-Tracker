package com.farmstock.inventory.repository;

import com.farmstock.inventory.entity.HarvestBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface HarvestBatchRepository extends JpaRepository<HarvestBatch, Long> {

    /** Returns null when the crop has no harvests; callers must treat null as zero. */
    @Query("select sum(h.quantity) from HarvestBatch h where h.crop.id = :cropId")
    BigDecimal sumQuantityByCrop(@Param("cropId") Long cropId);
}
