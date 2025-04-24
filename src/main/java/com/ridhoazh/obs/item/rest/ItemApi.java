package com.ridhoazh.obs.item.rest;

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

import com.ridhoazh.obs.item.Item;
import com.ridhoazh.obs.utils.BaseSearchParams;

// @formatter:off
/**
 * 🧠 Created by: Ridho Azhari Riyadi
 * 🗓️ Date: Apr 23, 2025
 * 💻 Auto-generated because Ridho too lazy to type this manually
 */
// @formatter:on

public interface ItemApi {

    @GetMapping(value = "/item/{id}",
            produces = { "application/json" })
    ResponseEntity<Item> detailItem(
            @PathVariable Long id);

    @GetMapping(value = "/items",
            produces = { "application/json" })
    ResponseEntity<Page<Item>> searchItem(
            @ModelAttribute BaseSearchParams searchParams, Pageable pageable);

    @PostMapping(value = "/item",
            produces = { "application/json" },
            consumes = { "application/json" })
    ResponseEntity<Map<String, Object>> createItem(
            @RequestBody ItemModel itemModel);

    @PutMapping(value = "/item/{itemId}",
            produces = { "application/json" },
            consumes = { "application/json" })
    ResponseEntity<Map<String, Object>> updateItem(
            @PathVariable(name = "itemId") Long itemId,
            @RequestBody ItemModel itemModel);

    @DeleteMapping(value = "/item/{itemId}",
            produces = { "application/json" })
    ResponseEntity<Map<String, Object>> deleteItem(
            @PathVariable("itemId") Long itemId);

}
