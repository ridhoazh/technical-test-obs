package com.ridhoazh.obs.inventory;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ridhoazh.obs.utils.BaseSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Page<Inventory> search(BaseSearchParams searchParams,
            Pageable pageable) {
        String keywords = searchParams.getKeywords();
        Long searchParam = StringUtils.isBlank(keywords)
                || !StringUtils.isNumeric(keywords) ? null
                        : Long.valueOf(keywords);
        return inventoryRepository.search(searchParam,
                pageable);
    }

    @Override
    public Inventory detail(Long id) {
        return findItemById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Inventory inventory) {
        inventoryRepository.delete(inventory);
    }

    @Override
    @Transactional
    public void save(Inventory inventory) {
        inventoryRepository.saveAndFlush(inventory);
    }

    @Override
    @Transactional
    public void saveAll(List<Inventory> inventory) {
        inventoryRepository.saveAllAndFlush(inventory);
    }

    @Override
    public Boolean isExist(Long id) {
        return Boolean.TRUE.equals(findItemById(id).isPresent());
    }

    private Optional<Inventory> findItemById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Integer getActualStock(Long id) {
        List<Inventory> inventories = inventoryRepository.findItemsById(id);
        return inventories.stream()
                .mapToInt(
                        i -> i.getType().equals("T") ? i.getQty() : -i.getQty())
                .sum();
    }

    @Override
    public void stockUpdate(Inventory inventory) {
        save(inventory);
    }

    @Override
    public void stockUpdate(List<Inventory> inventory) {
        saveAll(inventory);
    }

    @Override
    public Boolean isItemHaveTransaction(Long itemId) {
        return inventoryRepository.existsByItem_Id(itemId);
    }

}
