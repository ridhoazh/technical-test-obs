package com.ridhoazh.obs.order;

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
import com.ridhoazh.obs.item.Item;
import com.ridhoazh.obs.item.ItemService;
import com.ridhoazh.obs.item.rest.OrderApiController;
import com.ridhoazh.obs.order.rest.OrderMapper;
import com.ridhoazh.obs.order.rest.OrderModel;
import com.ridhoazh.obs.sequence.SequenceGenerator;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

public class OrderApiControllerTest {

    private MockMvc mockMvc;

    private ObjectWriter objectWriter = new ObjectMapper().writer();

    @Mock
    private OrderService orderService;
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
                        new OrderApiController(orderService, itemService,
                                inventoryService, sequenceGenerator))
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void testDetailOrder_found() throws Exception {
        Order mockOrder = new Order();
        mockOrder.setOrderNo("O1");

        when(orderService.detail("O1")).thenReturn(mockOrder);

        mockMvc.perform(get("/order/O1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDetailOrder_notFound() throws Exception {
        when(orderService.detail("O404")).thenReturn(null);

        mockMvc.perform(get("/order/O404")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchOrder() throws Exception {
        when(orderService.search(any(), any())).thenReturn(dummyPage());

        mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testCreateOrder() throws Exception {
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY")).thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");
        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectWriter.writeValueAsString(
                                buildModelData()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateOrderItemIdNull() throws Exception {
        OrderModel model = OrderModel.builder()
                .itemId(null)
                .orderNo(null)
                .price(null)
                .qty(null)
                .build();

        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY"))
                .thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectWriter.writeValueAsString(model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateOrderQtyNull() throws Exception {
        OrderModel model = OrderModel.builder()
                .itemId(1L)
                .price(null)
                .qty(null)
                .build();

        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY"))
                .thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectWriter.writeValueAsString(model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateOrderQtyNegative() throws Exception {
        OrderModel model = OrderModel.builder()
                .itemId(1L)
                .price(null)
                .qty(-1)
                .build();

        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY"))
                .thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectWriter.writeValueAsString(model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateOrderPriceNull() throws Exception {
        OrderModel model = OrderModel.builder()
                .itemId(1L)
                .price(null)
                .qty(1)
                .build();

        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY"))
                .thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectWriter.writeValueAsString(model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateOrderPriceNegative() throws Exception {
        OrderModel model = OrderModel.builder()
                .itemId(1L)
                .price(-1)
                .qty(1)
                .build();

        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY"))
                .thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectWriter.writeValueAsString(model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateOrderItemNotExist() throws Exception {
        OrderModel model = OrderModel.builder()
                .itemId(1L)
                .price(10)
                .qty(1)
                .build();

        when(itemService.isExist(any())).thenReturn(false);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY"))
                .thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectWriter.writeValueAsString(model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateOrderStockInsufficient() throws Exception {
        OrderModel model = OrderModel.builder()
                .itemId(1L)
                .price(10)
                .qty(1000)
                .build();

        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY"))
                .thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectWriter.writeValueAsString(model))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateOrder() throws Exception {
        when(orderService.detail(any())).thenReturn(buildEntity(0));
        when(itemService.isExist(0l)).thenReturn(true);
        when(itemService.isExist(1l)).thenReturn(true);
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY")).thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");
        mockMvc.perform(put("/order/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectWriter.writeValueAsString(
                                buildModelDataEdit()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateOrderNotFound() throws Exception {
        when(orderService.detail(any())).thenReturn(null);
        when(itemService.isExist(0l)).thenReturn(true);
        when(itemService.isExist(1l)).thenReturn(true);
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY")).thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");
        mockMvc.perform(put("/order/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(
                      objectWriter.writeValueAsString(
                              buildModelDataEdit()))
              .accept(MediaType.APPLICATION_JSON))
              .andDo(print())
              .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteOrder() throws Exception {
        when(orderService.detail(any())).thenReturn(buildEntity(0));
        when(itemService.detail(any())).thenReturn(buildItem(0));
        when(itemService.isExist(0l)).thenReturn(true);
        when(itemService.isExist(1l)).thenReturn(true);
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY")).thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");
        mockMvc.perform(delete("/order/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(
                      objectWriter.writeValueAsString(
                              buildModelDataEdit()))
              .accept(MediaType.APPLICATION_JSON))
              .andDo(print())
              .andExpect(status().isOk());
    }

    @Test
    void testDeleteOrderNotFound() throws Exception {
        when(orderService.detail(any())).thenReturn(null);
        when(itemService.detail(any())).thenReturn(null);
        when(itemService.isExist(0l)).thenReturn(true);
        when(itemService.isExist(1l)).thenReturn(true);
        when(itemService.isExist(any())).thenReturn(true);
        when(inventoryService.getActualStock(any())).thenReturn(10);
        when(sequenceGenerator.getNextBatch("INVENTORY")).thenReturn("INVENTORY1");
        when(sequenceGenerator.getNext(any())).thenReturn("O1");
        mockMvc.perform(delete("/order/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(
                      objectWriter.writeValueAsString(
                              buildModelDataEdit()))
              .accept(MediaType.APPLICATION_JSON))
              .andDo(print())
              .andExpect(status().isNotFound());
    }

    private Page<Order> dummyPage() {
        int pageParams = 5;
        int pageSizeParams = 10;

        PageRequest pageRequest = PageRequest.of(pageParams, pageSizeParams);
        int count = pageParams * pageSizeParams;
        return new PageImpl<>(buildEntityList(count),
                pageRequest, count);
    }

    private List<Order> buildEntityList(int count) {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            orders.add(buildEntity(i));
        }
        return orders;
    }

    private Order buildEntity(int count) {
        Item item = buildItem(count);
        Order order = new Order();
        order.setOrderNo("O" + count);
        order.setPrice(count);
        order.setQty(count);
        order.setItem(item);
        return order;
    }

    private Item buildItem(int count) {
        Item item = new Item();
        item.setId(Long.valueOf(count));
        item.setCurrentStock(count);
        item.setName("ITEM" + count);
        item.setPrice(count);
        return item;
    }

    private OrderModel buildModelDataEdit() {
        Order order = buildEntity(1);
        return OrderMapper.entityToModel(order);
    }

    private OrderModel buildModelData() {
        Order order = buildEntity(0);
        return OrderMapper.entityToModel(order);
    }

}