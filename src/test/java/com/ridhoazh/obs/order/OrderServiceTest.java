package com.ridhoazh.obs.order;

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

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(
                orderRepository);
    }

    @Test
    void testSearch() throws Exception {
        PageRequest pageRequest = PageRequest.of(5, 10);
        BaseSearchParams params = new BaseSearchParams();
        params.setKeywords("A");
        when(orderRepository.search(any(), any(), any()))
                .thenReturn(dummyPage());
        orderService.search(params, pageRequest);
    }

    @Test
    void testSearchNumeric() throws Exception {
        PageRequest pageRequest = PageRequest.of(5, 10);
        BaseSearchParams params = new BaseSearchParams();
        params.setKeywords("1");
        when(orderRepository.search(any(), any(), any()))
                .thenReturn(dummyPage());
        orderService.search(params, pageRequest);
    }

    @Test
    void testSearchKeywordsNull() throws Exception {
        PageRequest pageRequest = PageRequest.of(5, 10);
        BaseSearchParams params = new BaseSearchParams();
        params.setKeywords(null);
        when(orderRepository.search(any(), any(), any()))
                .thenReturn(dummyPage());
        orderService.search(params, pageRequest);
    }

    @Test
    void testDetail() {
        when(orderRepository.findById(any())).thenReturn(
                Optional.of(buildEntity(0)));
        orderService.detail("1");
    }

    @Test
    void testSave() {
        orderService.save(buildEntity(0));
    }

    @Test
    void testDelete() {
        orderService.delete(buildEntity(0));
    }

    @Test
    void testIsExist() {
        when(orderRepository.findById(any())).thenReturn(
                Optional.of(buildEntity(0)));
        orderService.isExist("1");
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
}
