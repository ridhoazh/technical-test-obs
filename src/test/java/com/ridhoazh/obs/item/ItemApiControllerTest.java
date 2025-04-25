package com.ridhoazh.obs.item;

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
import com.ridhoazh.obs.inventory.InventoryService;
import com.ridhoazh.obs.item.rest.ItemApiController;
import com.ridhoazh.obs.item.rest.ItemModel;
import com.ridhoazh.obs.order.OrderService;
import com.ridhoazh.obs.sequence.SequenceGenerator;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

public class ItemApiControllerTest {
    private MockMvc mockMvc;

    private ObjectWriter objectWriter = new ObjectMapper().writer();

    @Mock
    private ItemService itemService;
    @Mock
    private SequenceGenerator sequenceGenerator;
    @Mock
    private OrderService orderService;
    @Mock
    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new ItemApiController(itemService, inventoryService,
                                orderService,
                                sequenceGenerator))
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void testDetailItemFound() throws Exception {
        Item mock = new Item();
        mock.setId(1l);

        when(itemService.detail(any(), any())).thenReturn(mock);

        mockMvc.perform(get("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDetailItemNotFound() throws Exception {
        when(itemService.detail(any(), any())).thenReturn(null);

        mockMvc.perform(get("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearch() throws Exception {
        when(itemService.search(any(),any())).thenReturn(dummyPage());

        mockMvc.perform(get("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testCreateItem() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("ITEM1");
        
        mockMvc.perform(post("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                                buildModelData()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateItemNameEmpty() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("ITEM1");
        ItemModel model = ItemModel.builder()
                .id(1l)
                .name("")
                .price(100)
                .build();
        mockMvc.perform(post("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateItemNameNull() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("ITEM1");
        ItemModel model = ItemModel.builder()
                .id(1l)
                .name(null)
                .price(100)
                .build();
        mockMvc.perform(post("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateItemPriceNull() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("INVENTORY1");
        
        ItemModel model = ItemModel.builder()
                .id(1l)
                .name("a")
                .price(null)
                .build();
        
        mockMvc.perform(post("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateItemPriceNegative() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("INVENTORY1");
        
        ItemModel model = ItemModel.builder()
                .id(1l)
                .name("a")
                .price(-4)
                .build();
        
        mockMvc.perform(post("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                        model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateItem() throws Exception {
        when(itemService.detail(any())).thenReturn(buildEntity(0));
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("INVENTORY1");
        
        mockMvc.perform(put("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                                buildModelData()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateItemNotFound() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("INVENTORY1");
        
        mockMvc.perform(put("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(
                                buildModelData()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        when(itemService.detail(any())).thenReturn(buildEntity(0));
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("INVENTORY1");
        
        mockMvc.perform(delete("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("INVENTORY1");
        
        mockMvc.perform(delete("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteButHaveOrder() throws Exception {
        when(itemService.detail(any())).thenReturn(buildEntity(0));
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("INVENTORY1");
        when(orderService.isItemHaveTransaction(any())).thenReturn(true);
        mockMvc.perform(delete("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteButHaveInventory() throws Exception {
        when(itemService.detail(any())).thenReturn(buildEntity(0));
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("INVENTORY1");
        when(orderService.isItemHaveTransaction(any())).thenReturn(true);
        mockMvc.perform(delete("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteButHaveOrderAndInventory() throws Exception {
        when(itemService.detail(any())).thenReturn(buildEntity(0));
        when(itemService.isExist(any())).thenReturn(true);
        when(sequenceGenerator.getNext(any())).thenReturn("INVENTORY1");
        when(orderService.isItemHaveTransaction(any())).thenReturn(true);
        mockMvc.perform(delete("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
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
        List<Item> orders = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            orders.add(buildEntity(i));
        }
        return orders;
    }

    private Item buildEntity(int count) {
        Item item = new Item();
        item.setId(Long.valueOf(count));
        item.setCurrentStock(count);
        item.setName("ITEM" + count);
        item.setPrice(count);
        return item;
    }

    private ItemModel buildModelData() {
        Item inventory = buildEntity(0);
        return ItemMapper.entityToModel(inventory);
    }

}
