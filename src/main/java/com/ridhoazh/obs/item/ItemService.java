package com.ridhoazh.obs.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ridhoazh.obs.utils.BaseSearchParams;

// @formatter:off
/**
 * 🧠 Created by: Ridho Azhari Riyadi
 * 🗓️ Date: Apr 24, 2025
 * 💻 Auto-generated because Ridho too lazy to type this manually
 */
// @formatter:on

public interface ItemService {

    Page<Item> search(BaseSearchParams searchParams, Pageable pageable);

    Item detail(Long id);

    void save(Item item);

    void delete(Item item);

    Boolean isExist(Long id);

}
