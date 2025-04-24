package com.ridhoazh.obs.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// @formatter:off
/**
 * 🧠 Created by: Ridho Azhari Riyadi
 * 🗓️ Date: Apr 23, 2025
 * 💻 Auto-generated because Ridho too lazy to type this manually
 */
// @formatter:on

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i "
            + "WHERE (:name IS NULL OR UPPER(i.name) LIKE CONCAT('%', UPPER(:name), '%'))"
            + "AND (:id IS NULL OR i.id = :id)")
    Page<Item> search(
            @Param("id") Long id,
            @Param("name") String name,
            Pageable pageable);

}
