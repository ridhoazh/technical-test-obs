package com.ridhoazh.obs.item.rest;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ridhoazh.obs.exception.InvalidParameterException;
import com.ridhoazh.obs.item.Item;
import com.ridhoazh.obs.item.ItemMapper;
import com.ridhoazh.obs.item.ItemService;
import com.ridhoazh.obs.sequence.SequenceGenerator;
import com.ridhoazh.obs.utils.BaseSearchParams;
import com.ridhoazh.obs.utils.Utils;

import io.micrometer.common.util.StringUtils;

// @formatter:off
/**
 * 🧠 Created by: Ridho Azhari Riyadi
 * 🗓️ Date: Apr 24, 2025
 * 💻 Auto-generated because Ridho too lazy to type this manually
 */
// @formatter:on

@RestController
public class ItemApiController implements ItemApi {
    private final ItemService itemService;
    private final SequenceGenerator sequenceGenerator;

    public ItemApiController(ItemService itemService,
            SequenceGenerator sequenceGenerator) {
        this.itemService = itemService;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public ResponseEntity<Item> detailItem(
            @PathVariable(name = "id") Long id) {
        Item item = itemService.detail(id);
        if (item != null) {
            return new ResponseEntity<>(item,
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Page<Item>> searchItem(
            @ModelAttribute BaseSearchParams searchParams, Pageable pageable) {
        return new ResponseEntity<>(itemService.search(searchParams, pageable),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> createItem(
            @RequestBody ItemModel itemModel) {
        validate(itemModel);
        Item item = new Item();
        ItemMapper.modelToEntity(item, itemModel);
        item.setId(Utils.removePrefixId(sequenceGenerator.getNext("ITEM")));
        itemService.save(item);

        return new ResponseEntity<>(
                Utils.buildResponseMessage(item.getId().toString(),
                        "successfully created"),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateItem(
            @PathVariable(name = "itemId") Long itemId,
            @RequestBody ItemModel itemModel) {
        Item currentItem = itemService.detail(itemId);
        if (currentItem != null) {
            validate(itemModel);
            itemService.save(ItemMapper.modelToEntity(currentItem, itemModel));

            return new ResponseEntity<>(
                    Utils.buildResponseMessage(itemId.toString(),
                            "successfully updated"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                Utils.buildResponseMessage(itemId.toString(),
                        "not found"),
                HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleteItem(
            @PathVariable(name = "itemId") Long itemId) {
        Item currentItem = itemService.detail(itemId);
        if (currentItem != null) {
            itemService.delete(currentItem);
            return new ResponseEntity<>(
                    Utils.buildResponseMessage(itemId.toString(),
                            "successfully deleted"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                Utils.buildResponseMessage(itemId.toString(),
                        "not found"),
                HttpStatus.NOT_FOUND);
    }

    private void validate(ItemModel item) {
        if (StringUtils.isBlank(item.name())) {
            throw new InvalidParameterException(
                    "name",
                    "com.ridhoazh.obs.exception.value_is_null");
        }

        if (item.price() == null || item.price() < 0) {
            throw new InvalidParameterException(
                    "price",
                    "com.ridhoazh.obs.exception.value_is_null_or_below_zero");
        }
    }

}
