package com.ridhoazh.obs.item;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ridhoazh.obs.inventory.InventoryService;
import com.ridhoazh.obs.item.rest.ItemSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 24, 2025
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
    public Page<Item> search(ItemSearchParams searchParams, Pageable pageable) {
        String keywords = searchParams.getKeywords();
        Long idParam = StringUtils.isBlank(keywords)
                || !StringUtils.isNumeric(keywords) ? null
                        : Long.valueOf(keywords);

        Page<Item> items = itemRepository.search(idParam, keywords, pageable);
        if (Boolean.TRUE.equals(searchParams.isShowRemainingStock())) {
            items.forEach(item -> {
                item.setCurrentStock(
                        inventoryService.getActualStock(item.getId()));
            });
        }

        return items;
    }

    @Override
    public Item detail(Long id, ItemSearchParams searchParams) {
        Optional<Item> item = findItemById(id);
        if (Boolean.TRUE.equals(searchParams.isShowRemainingStock())) {
            item.map(i -> {
                i.setCurrentStock(inventoryService.getActualStock(id));
                return i;
            });
        }
        return item.get();
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
        return itemRepository.findById(id);

    }

    @Override
    public Item detail(Long id) {
        return findItemById(id).orElse(null);
    }
}
