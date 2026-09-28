package com.farmstock.inventory.repository;

import com.farmstock.inventory.entity.Crop;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CropRepository extends JpaRepository<Crop, Long> {
    Optional<Crop> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);

    /** Row-locks the crop so concurrent sales of the same crop are serialised. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Crop c where c.id = :id")
    Optional<Crop> findByIdForUpdate(@Param("id") Long id);
}
