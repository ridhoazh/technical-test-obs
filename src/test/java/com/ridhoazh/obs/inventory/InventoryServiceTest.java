package com.ridhoazh.obs.inventory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.ridhoazh.obs.item.Item;
import com.ridhoazh.obs.utils.BaseSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

public class InventoryServiceTest {
    @Mock
    private InventoryRepository inventoryRepository;
    private InventoryService inventoryService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        inventoryService = new InventoryServiceImpl(
                inventoryRepository);
    }

    @Test
    void testSearch() throws Exception {
        PageRequest pageRequest = PageRequest.of(5, 10);
        BaseSearchParams params = new BaseSearchParams();
        params.setKeywords("A");
        when(inventoryRepository.search(any(), any()))
                .thenReturn(dummyPage());
        inventoryService.search(params, pageRequest);
    }

    @Test
    void testSearchNumeric() throws Exception {
        PageRequest pageRequest = PageRequest.of(5, 10);
        BaseSearchParams params = new BaseSearchParams();
        params.setKeywords("1");
        when(inventoryRepository.search(any(), any()))
                .thenReturn(dummyPage());
        inventoryService.search(params, pageRequest);
    }

    @Test
    void testSearchKeywordsNull() throws Exception {
        PageRequest pageRequest = PageRequest.of(5, 10);
        BaseSearchParams params = new BaseSearchParams();
        params.setKeywords(null);
        when(inventoryRepository.search(any(), any()))
                .thenReturn(dummyPage());
        inventoryService.search(params, pageRequest);
    }

    @Test
    void testDetail() {
        when(inventoryRepository.findById(any())).thenReturn(
                Optional.of(buildEntity(0)));
        inventoryService.detail(1l);
    }

    @Test
    void testSave() {
        inventoryService.save(buildEntity(0));
    }

    @Test
    void testSaveAll() {
        inventoryService.saveAll(buildEntityList(2));
    }

    @Test
    void testDelete() {
        inventoryService.delete(buildEntity(0));
    }

    @Test
    void testIsExist() {
        when(inventoryRepository.findById(any())).thenReturn(
                Optional.of(buildEntity(0)));
        inventoryService.isExist(1l);
    }

    @Test
    void testGetActualStock() {
        when(inventoryRepository.findItemsById(any())).thenReturn(
                buildEntityList(10));
        inventoryService.getActualStock(1l);
    }

    @Test
    void testStockUpdate() {
        inventoryService.stockUpdate(buildEntity(0));
    }

    @Test
    void testStockUpdateList() {
        inventoryService.stockUpdate(buildEntityList(2));
    }

    private Page<Inventory> dummyPage() {
        int pageParams = 5;
        int pageSizeParams = 10;

        PageRequest pageRequest = PageRequest.of(pageParams, pageSizeParams);
        int count = pageParams * pageSizeParams;
        return new PageImpl<>(buildEntityList(count),
                pageRequest, count);
    }

    private List<Inventory> buildEntityList(int count) {
        List<Inventory> datas = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            datas.add(buildEntity(i));
        }
        return datas;
    }

    private Inventory buildEntity(int count) {
        Item item = buildItem(count);
        Inventory inventory = new Inventory();
        inventory.setId(Long.valueOf(count));
        inventory.setType(count % 2 == 0 ? "W" : "T");
        inventory.setQty(count);
        inventory.setItem(item);
        return inventory;
    }

    private Item buildItem(int count) {
        Item item = new Item();
        item.setId(Long.valueOf(count));
        item.setCurrentStock(count);
        item.setName("ITEM" + count);
        item.setPrice(count);
        return item;
    }
}
