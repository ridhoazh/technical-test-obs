package com.ridhoazh.obs.item.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ridhoazh.obs.exception.InvalidParameterException;
import com.ridhoazh.obs.inventory.Inventory;
import com.ridhoazh.obs.inventory.InventoryService;
import com.ridhoazh.obs.item.Item;
import com.ridhoazh.obs.item.ItemService;
import com.ridhoazh.obs.order.Order;
import com.ridhoazh.obs.order.OrderService;
import com.ridhoazh.obs.order.rest.OrderApi;
import com.ridhoazh.obs.order.rest.OrderMapper;
import com.ridhoazh.obs.order.rest.OrderModel;
import com.ridhoazh.obs.sequence.SequenceGenerator;
import com.ridhoazh.obs.utils.BaseSearchParams;
import com.ridhoazh.obs.utils.Utils;
import com.ridhoazh.obs.utils.ValidationMessage;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

@RestController
public class OrderApiController implements OrderApi {
    private final OrderService orderService;
    private final ItemService itemService;
    private final InventoryService inventoryService;
    private final SequenceGenerator sequenceGenerator;

    public OrderApiController(OrderService orderService,
            ItemService itemService, InventoryService inventoryService,
            SequenceGenerator sequenceGenerator) {
        this.orderService = orderService;
        this.itemService = itemService;
        this.inventoryService = inventoryService;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public ResponseEntity<Order> detailOrder(
            @PathVariable("id") String id) {
        Order order = orderService.detail(id);
        if (order != null) {
            return new ResponseEntity<>(order,
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Page<Order>> searchOrder(
            @ModelAttribute BaseSearchParams searchParams,
            Pageable pageable) {
        return new ResponseEntity<>(
                orderService.search(searchParams, pageable),
                HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestBody OrderModel orderModel) {
        validate(orderModel);
        Item currentItem = itemService.detail(orderModel.itemId());
        Order order = new Order();
        order.setItem(currentItem);
        OrderMapper.modelToEntity(order, orderModel);
        order.setOrderNo(
                sequenceGenerator.getNext("O"));
        orderService.save(order);

        Inventory inventory = buildInventoryUpdateStock(
                currentItem, order, "W");
        inventoryService.stockUpdate(inventory);

        return new ResponseEntity<>(
                Utils.buildResponseMessage(order.getOrderNo().toString(),
                        ValidationMessage.CREATED),
                HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> updateOrder(
            @PathVariable(name = "id") String id,
            @RequestBody OrderModel orderModel) {
        Order currentOrder = orderService.detail(id);
        if (currentOrder == null) {
            return new ResponseEntity<>(
                    Utils.buildResponseMessage(id.toString(),
                            ValidationMessage.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
        Item currentItem = itemService.detail(currentOrder.getItemId());
        Item updateItem = itemService.detail(orderModel.itemId());

        validate(orderModel);

        // revert previous stock
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(buildInventoryUpdateStock(
                currentItem, currentOrder, "T"));

        currentOrder.setPrice(orderModel.price());
        currentOrder.setQty(orderModel.qty());
        currentOrder.setItem(currentItem);

        // creating new inventory
        inventories.add(buildInventoryUpdateStock(
                updateItem, currentOrder, "W"));

        orderService.save(currentOrder);
        inventoryService.stockUpdate(inventories);

        return new ResponseEntity<>(
                Utils.buildResponseMessage(id.toString(),
                        ValidationMessage.UPDATED),
                HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteOrder(
            @PathVariable("id") String id) {
        Order order = orderService.detail(id);
        if (order != null) {
            Item currentItem = itemService.detail(order.getItemId());
            if (currentItem != null) {
                Inventory inventory = buildInventoryUpdateStock(
                        currentItem, order, "T");
                inventoryService.stockUpdate(inventory);
            }
            orderService.delete(order);
            return new ResponseEntity<>(
                    Utils.buildResponseMessage(id.toString(),
                            ValidationMessage.DELETED),
                    HttpStatus.OK);

        }
        return new ResponseEntity<>(
                Utils.buildResponseMessage(id.toString(),
                        ValidationMessage.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    private void validate(OrderModel orderModel) {
        if (orderModel.itemId() == null) {
            throw new InvalidParameterException(
                    "itemId",
                    ValidationMessage.NULL);
        }

        if (orderModel.qty() == null || orderModel.qty() < 0 ||
                orderModel.price() == null || orderModel.price() < 0) {
            throw new InvalidParameterException(
                    "qty/price",
                    ValidationMessage.NULL_OR_NEGATIVE);
        }

        if (!itemService.isExist(orderModel.itemId())) {
            throw new InvalidParameterException(
                    "itemId",
                    ValidationMessage.NOT_FOUND);

        }

        Integer actualStock = inventoryService
                .getActualStock(orderModel.itemId());
        int remaining = actualStock - orderModel.qty();
        if (remaining < 0) {
            throw new InvalidParameterException(
                    "qty",
                    ValidationMessage.STOCK_INSUFFICIENT);
        }

    }

    @Transactional
    private Inventory buildInventoryUpdateStock(Item currentItem, Order order,
            String type) {
        Long id = Utils.removePrefixId(
                sequenceGenerator.getNextBatch("INVENTORY"));
        System.out.println("id nya: " + id);
        Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setItem(currentItem);
        inventory.setQty(order.getQty());
        inventory.setType(type);
        return inventory;
    }
}
