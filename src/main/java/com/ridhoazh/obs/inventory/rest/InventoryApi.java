package com.ridhoazh.obs.inventory.rest;

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

import com.ridhoazh.obs.inventory.Inventory;
import com.ridhoazh.obs.utils.BaseSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 23, 2025
 */
// @formatter:on

public interface InventoryApi {

    @GetMapping(value = "/inventory/{id}",
            produces = { "application/json" })
    ResponseEntity<Inventory> detailInventory(
            @PathVariable("id") Long id);

    @GetMapping(value = "/inventories",
            produces = { "application/json" })
    ResponseEntity<Page<Inventory>> searchInventory(
            @ModelAttribute BaseSearchParams searchParams,
            Pageable pageable);

    @PostMapping(value = "/inventory",
            produces = { "application/json" },
            consumes = { "application/json" })
    ResponseEntity<Map<String, Object>> createInventory(
            @RequestBody InventoryModel InventoryModel);

    @PutMapping(value = "/inventory/{id}",
            produces = { "application/json" },
            consumes = { "application/json" })
    ResponseEntity<Map<String, Object>> updateInventory(
            @PathVariable(name = "id") Long inventoryId,
            @RequestBody InventoryModel InventoryModel);

    @DeleteMapping(value = "/inventory/{id}",
            produces = { "application/json" })
    ResponseEntity<Map<String, Object>> deleteInventory(
            @PathVariable("id") Long inventoryId);

}
