package com.ridhoazh.obs.item;

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

import com.ridhoazh.obs.inventory.InventoryService;
import com.ridhoazh.obs.item.rest.ItemSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private InventoryService inventoryService;
    private ItemService itemService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        itemService = new ItemServiceImpl(
                itemRepository, inventoryService);
    }

    @Test
    void testSearch() throws Exception {
        PageRequest pageRequest = PageRequest.of(5, 10);
        ItemSearchParams params = new ItemSearchParams();
        params.setKeywords("A");
        when(itemRepository.search(any(), any(), any()))
                .thenReturn(dummyPage());
        itemService.search(params, pageRequest);
    }

    @Test
    void testSearchWithRemainingStock() throws Exception {
        when(inventoryService.getActualStock(any()))
            .thenReturn(10);
        PageRequest pageRequest = PageRequest.of(5, 10);
        ItemSearchParams params = new ItemSearchParams();
        params.setShowRemainingStock(true);
        params.setKeywords("A");
        when(itemRepository.search(any(), any(), any()))
                .thenReturn(dummyPage());
        itemService.search(params, pageRequest);
    }

    @Test
    void testSearchNumeric() throws Exception {
        PageRequest pageRequest = PageRequest.of(5, 10);
        ItemSearchParams params = new ItemSearchParams();
        params.setKeywords("1");
        when(itemRepository.search(any(), any(), any()))
                .thenReturn(dummyPage());
        itemService.search(params, pageRequest);
    }

    @Test
    void testSearchKeywordsNull() throws Exception {
        PageRequest pageRequest = PageRequest.of(5, 10);
        ItemSearchParams params = new ItemSearchParams();
        params.setKeywords(null);
        when(itemRepository.search(any(), any(), any()))
                .thenReturn(dummyPage());
        itemService.search(params, pageRequest);
    }

    @Test
    void testDetailWithRemainingStock() {
        when(inventoryService.getActualStock(any()))
        .thenReturn(10);
        ItemSearchParams params = new ItemSearchParams();
        params.setShowRemainingStock(true);
        when(itemRepository.findById(any())).thenReturn(
                Optional.of(buildEntity(0)));
        itemService.detail(1l,params);
    }
    
    @Test
    void testDetailNotWithRemainingStock() {
        when(inventoryService.getActualStock(any()))
        .thenReturn(10);
        ItemSearchParams params = new ItemSearchParams();
        params.setShowRemainingStock(false);
        when(itemRepository.findById(any())).thenReturn(
                Optional.of(buildEntity(0)));
        itemService.detail(1l,params);
    }

    @Test
    void testDetail() {
        when(itemRepository.findById(any())).thenReturn(
                Optional.of(buildEntity(0)));
        itemService.detail(1l);
    }

    @Test
    void testSave() {
        itemService.save(buildEntity(0));
    }

    @Test
    void testDelete() {
        itemService.delete(buildEntity(0));
    }

    @Test
    void testIsExist() {
        when(itemRepository.findById(any())).thenReturn(
                Optional.of(buildEntity(0)));
        itemService.isExist(1l);
    }

    private Page<Item> dummyPage() {
        int pageParams = 5;
        int pageSizeParams = 10;

        PageRequest pageRequest = PageRequest.of(pageParams, pageSizeParams);
        int count = pageParams * pageSizeParams;
        return new PageImpl<>(buildEntityList(count),
                pageRequest, count);
    }

    private List<Item> buildEntityList(int count) {
        List<Item> datas = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            datas.add(buildEntity(i));
        }
        return datas;
    }

    private Item buildEntity(int count) {
        Item item = buildItem(count);
        return item;
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
