package com.ridhoazh.obs.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ridhoazh.obs.item.rest.ItemSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 24, 2025
 */
// @formatter:on

public interface ItemService {

    Page<Item> search(ItemSearchParams searchParams, Pageable pageable);

    Item detail(Long id, ItemSearchParams searchParams);

    Item detail(Long id);

    void save(Item item);

    void delete(Item item);

    Boolean isExist(Long id);

}
