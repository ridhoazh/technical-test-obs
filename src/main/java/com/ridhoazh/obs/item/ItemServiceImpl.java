package com.ridhoazh.obs.item;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ridhoazh.obs.inventory.InventoryService;
import com.ridhoazh.obs.utils.BaseSearchParams;

import io.micrometer.common.util.StringUtils;

// @formatter:off
/**
 * 🧠 Created by: Ridho Azhari Riyadi
 * 🗓️ Date: Apr 24, 2025
 * 💻 Auto-generated because Ridho too lazy to type this manually
 */
// @formatter:on

@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final InventoryService inventoryService;

    public ItemServiceImpl(ItemRepository itemRepository,
            InventoryService inventoryService) {
        this.itemRepository = itemRepository;
        this.inventoryService = inventoryService;
    }

    @Override
    public Page<Item> search(BaseSearchParams searchParams, Pageable pageable) {
        String keywords = searchParams.getKeywords();
        Long idParam = StringUtils.isBlank(keywords) ? null
                : Long.valueOf(keywords);

        Page<Item> items = itemRepository.search(idParam, keywords, pageable);
        items.forEach(item -> {
            item.setCurrentStock(inventoryService.getActualStock(item.getId()));
        });

        return items;
    }

    @Override
    public Item detail(Long id) {
        return findItemById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Item item) {
        itemRepository.delete(item);
    }

    @Override
    @Transactional
    public void save(Item item) {
        itemRepository.saveAndFlush(item);
    }

    @Override
    public Boolean isExist(Long id) {
        return Boolean.TRUE.equals(findItemById(id).isPresent());
    }

    private Optional<Item> findItemById(Long id) {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setCurrentStock(inventoryService.getActualStock(id));
                    return item;
                });
    }
}
