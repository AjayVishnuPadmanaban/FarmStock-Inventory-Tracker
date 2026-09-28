package com.farmstock.inventory.repository;

import com.farmstock.inventory.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    /** Returns null when the crop has no sales; callers must treat null as zero. */
    @Query("select sum(s.quantity) from Sale s where s.crop.id = :cropId")
    BigDecimal sumQuantityByCrop(@Param("cropId") Long cropId);

    List<Sale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);
}
