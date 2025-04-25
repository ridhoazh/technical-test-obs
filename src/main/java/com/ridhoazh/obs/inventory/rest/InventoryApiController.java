package com.ridhoazh.obs.inventory.rest;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ridhoazh.obs.exception.InvalidParameterException;
import com.ridhoazh.obs.inventory.Inventory;
import com.ridhoazh.obs.inventory.InventoryMapper;
import com.ridhoazh.obs.inventory.InventoryService;
import com.ridhoazh.obs.item.Item;
import com.ridhoazh.obs.item.ItemService;
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
public class InventoryApiController implements InventoryApi {
    private final InventoryService inventoryService;
    private final SequenceGenerator sequenceGenerator;
    private final ItemService itemService;

    public InventoryApiController(InventoryService inventoryService,
            SequenceGenerator sequenceGenerator, ItemService itemService) {
        this.inventoryService = inventoryService;
        this.sequenceGenerator = sequenceGenerator;
        this.itemService = itemService;
    }

    @Override
    public ResponseEntity<Inventory> detailInventory(
            @PathVariable(name = "id") Long id) {
        Inventory item = inventoryService.detail(id);
        if (item != null) {
            return new ResponseEntity<>(item,
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Page<Inventory>> searchInventory(
            @ModelAttribute BaseSearchParams searchParams, Pageable pageable) {
        return new ResponseEntity<>(
                inventoryService.search(searchParams, pageable),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> createInventory(
            @RequestBody InventoryModel inventoryModel) {
        validate(inventoryModel);
        Item currentItem = itemService.detail(inventoryModel.itemId());
        Inventory inventory = new Inventory();
        inventory.setItem(currentItem);
        InventoryMapper.modelToEntity(inventory, inventoryModel);
        inventory.setId(
                Utils.removePrefixId(sequenceGenerator.getNext("INVENTORY")));
        inventoryService.save(inventory);

        return new ResponseEntity<>(
                Utils.buildResponseMessage(inventory.getId().toString(),
                        ValidationMessage.CREATED),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateInventory(
            @PathVariable(name = "id") Long id,
            @RequestBody InventoryModel inventoryModel) {
        Inventory currentInventory = inventoryService.detail(id);
        if (currentInventory != null) {
            validate(inventoryModel);
            Item currentItem = itemService.detail(inventoryModel.itemId());
            currentInventory.setItem(currentItem);
            inventoryService.save(
                    InventoryMapper.modelToEntity(currentInventory,
                            inventoryModel));

            return new ResponseEntity<>(
                    Utils.buildResponseMessage(id.toString(),
                            ValidationMessage.UPDATED),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                Utils.buildResponseMessage(id.toString(),
                        ValidationMessage.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleteInventory(
            @PathVariable(name = "id") Long id) {
        Inventory currentInventory = inventoryService.detail(id);
        if (currentInventory != null) {
            inventoryService.delete(currentInventory);
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

    private void validate(InventoryModel inventoryModel) {
        if (inventoryModel.itemId() == null) {
            throw new InvalidParameterException(
                    "itemId",
                    ValidationMessage.NULL);
        }

        if (inventoryModel.qty() == null || inventoryModel.qty() < 0) {
            throw new InvalidParameterException(
                    "qty",
                    ValidationMessage.NULL_OR_NEGATIVE);
        }

        if (StringUtils.isBlank(inventoryModel.type())) {
            throw new InvalidParameterException(
                    "type",
                    ValidationMessage.NULL);
        }

        if (!itemService.isExist(inventoryModel.itemId())) {
            throw new InvalidParameterException(
                    "itemId",
                    ValidationMessage.NOT_FOUND);

        }

        if (!StringUtils.equalsAny(inventoryModel.type(), "T", "W")) {
            throw new InvalidParameterException(
                    "type",
                    ValidationMessage.INAPPROPRIATE_VALUE);
        }

        if (StringUtils.equals("W", inventoryModel.type())) {
            Integer actualStock = inventoryService
                    .getActualStock(inventoryModel.itemId());
            int remaining = actualStock - inventoryModel.qty();
            if (remaining < 0) {
                throw new InvalidParameterException(
                        "qty",
                        ValidationMessage.STOCK_UNDER_ZERO);
            }
        }
    }

}
