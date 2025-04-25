package com.ridhoazh.obs.order.rest;

import com.ridhoazh.obs.order.Order;
import com.ridhoazh.obs.sequence.SequenceGenerator;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 24, 2025
 */
// @formatter:on

public interface OrderMapper extends SequenceGenerator {

    static OrderModel entityToModel(Order order) {
        return OrderModel.builder()
                .orderNo(order.getOrderNo())
                .itemId(order.getItemId())
                .qty(order.getQty())
                .price(order.getPrice())
                .build();
    }

    static Order modelToEntity(Order order, OrderModel orderModel) {
        order.setQty(orderModel.qty());
        order.setPrice(orderModel.price());
        return order;
    }

}
