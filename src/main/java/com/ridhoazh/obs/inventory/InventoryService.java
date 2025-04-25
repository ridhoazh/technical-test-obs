package com.ridhoazh.obs.inventory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ridhoazh.obs.utils.BaseSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

public interface InventoryService {
    Page<Inventory> search(BaseSearchParams searchParams, Pageable pageable);

    Inventory detail(Long id);

    void save(Inventory inventory);

    void saveAll(List<Inventory> inventory);

    void delete(Inventory inventory);

    Boolean isExist(Long id);

    Integer getActualStock(Long id);

    void stockUpdate(Inventory inventory);

    void stockUpdate(List<Inventory> inventory);

    Boolean isItemHaveTransaction(Long itemId);

}
