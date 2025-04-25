package com.ridhoazh.obs.inventory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 23, 2025
 */
// @formatter:on

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("SELECT i FROM Inventory i "
            + "WHERE (:id IS NULL OR i.id = :id)"
            + "OR (:id IS NULL OR i.item.id = :id)")
    Page<Inventory> search(
            @Param("id") Long id,
            Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE i.item.id = :id ")
    List<Inventory> findItemsById(
            @Param("id") Long id);

    boolean existsByItem_Id(Long itemId);
}
