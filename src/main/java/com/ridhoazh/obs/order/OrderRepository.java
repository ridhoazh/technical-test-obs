package com.ridhoazh.obs.order;

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

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o "
            + "WHERE (:id IS NULL OR o.orderNo = :id)"
            + "OR (:id IS NULL OR o.item.id = :itemId)")
    Page<Order> search(
            @Param("id") String id,
            @Param("itemId") Long itemId,
            Pageable pageable);

}
