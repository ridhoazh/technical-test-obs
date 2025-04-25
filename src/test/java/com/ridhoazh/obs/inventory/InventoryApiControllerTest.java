package com.ridhoazh.obs.inventory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ridhoazh.obs.exception.ApiExceptionHandler;
import com.ridhoazh.obs.inventory.rest.InventoryApiController;
import com.ridhoazh.obs.inventory.rest.InventoryModel;
import com.ridhoazh.obs.item.Item;
import com.ridhoazh.obs.item.ItemService;
import com.ridhoazh.obs.sequence.SequenceGenerator;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

public class InventoryApiControllerTest {
    private MockMvc mockMvc;

    private ObjectWriter objectWriter = new ObjectMapper().writer();

    @Mock
    private ItemService itemService;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private SequenceGenerator sequenceGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new InventoryApiController(inventoryService,
                                sequenceGenerator, itemService))
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void testDetailInventoryFound() throws Exception {
        Inventory mock = new Inventory();
        mock.setId(1l);

        when(inventoryService.detail(any())).thenReturn(mock);

        mockMvc.perform(get("/inventory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDetailInventoryNotFound() throws Exception {
        when(inventoryService.detail(any())).thenReturn(null);

        mockMvc.perform(get("/inventory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearch() throws Exception {
        when(inventoryService.search(any(),any())).thenReturn(dummyPage());

        mockMvc.perform(get("/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testCreateInventory() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                                buildModelData()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateInventoryItemIdNull() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        InventoryModel model = InventoryModel.builder()
                .itemId(null)
                .qty(null)
                .type(null)
                .build();
        
        mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryQtyNull() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        InventoryModel model = InventoryModel.builder()
                .itemId(1l)
                .qty(null)
                .type(null)
                .build();
        
        mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryQtyNegative() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        InventoryModel model = InventoryModel.builder()
                .itemId(1l)
                .qty(-1)
                .type(null)
                .build();
        
        mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryTypeNull() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        InventoryModel model = InventoryModel.builder()
                .itemId(1l)
                .qty(1)
                .type(null)
                .build();
        
        mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryTypeInappropriate() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        InventoryModel model = InventoryModel.builder()
                .itemId(1l)
                .qty(1)
                .type("X")
                .build();
        
        mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryItemNotExist() throws Exception {
        when(itemService.isExist(any())).thenReturn(false);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        InventoryModel model = InventoryModel.builder()
                .itemId(1l)
                .qty(1)
                .type("T")
                .build();
        
        mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryStockInsufficient() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(1);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        InventoryModel model = InventoryModel.builder()
                .itemId(1l)
                .qty(100)
                .type("W")
                .build();
        
        mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryStockInsufficientButTypeT() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(1);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        InventoryModel model = InventoryModel.builder()
                .itemId(1l)
                .qty(100)
                .type("T")
                .build();
        
        mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateInventory() throws Exception {
        when(inventoryService.detail(any())).thenReturn(buildEntity(0));
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        mockMvc.perform(put("/inventory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                                buildModelData()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateInventoryNotFound() throws Exception {
        when(inventoryService.detail(any())).thenReturn(null);
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        mockMvc.perform(put("/inventory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                                buildModelData()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        when(inventoryService.detail(any())).thenReturn(buildEntity(0));
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        mockMvc.perform(delete("/inventory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        when(inventoryService.detail(any())).thenReturn(null);
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNext("INVENTORY")).thenReturn("INVENTORY1");
        
        mockMvc.perform(delete("/inventory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
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
        List<Inventory> orders = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            orders.add(buildEntity(i));
        }
        return orders;
    }

    private Inventory buildEntity(int count) {
        Item item = buildItem(count);
        Inventory inventory = new Inventory();
        inventory.setType(count % 2 == 0 ? "W" : "T");
        inventory.setId(Long.valueOf(count));
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

    private InventoryModel buildModelData() {
        Inventory inventory = buildEntity(0);
        return InventoryMapper.entityToModel(inventory);
    }

}
