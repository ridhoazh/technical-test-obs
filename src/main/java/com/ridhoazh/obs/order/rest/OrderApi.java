package com.ridhoazh.obs.order.rest;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ridhoazh.obs.order.Order;
import com.ridhoazh.obs.utils.BaseSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 23, 2025
 */
// @formatter:on

public interface OrderApi {

    @GetMapping(value = "/order/{id}",
            produces = { "application/json" })
    ResponseEntity<Order> detailOrder(
            @PathVariable("id") String id);

    @GetMapping(value = "/orders",
            produces = { "application/json" })
    ResponseEntity<Page<Order>> searchOrder(
            @ModelAttribute BaseSearchParams searchParams,
            Pageable pageable);

    @PostMapping(value = "/order",
            produces = { "application/json" },
            consumes = { "application/json" })
    ResponseEntity<Map<String, Object>> createOrder(
            @RequestBody OrderModel orderModel);

    @PutMapping(value = "/order/{id}",
            produces = { "application/json" },
            consumes = { "application/json" })
    ResponseEntity<Map<String, Object>> updateOrder(
            @PathVariable(name = "id") String orderId,
            @RequestBody OrderModel orderModel);

    @DeleteMapping(value = "/order/{id}",
            produces = { "application/json" })
    ResponseEntity<Map<String, Object>> deleteOrder(
            @PathVariable("id") String orderId);

}
