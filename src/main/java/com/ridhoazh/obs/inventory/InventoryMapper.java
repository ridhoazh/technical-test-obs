package com.ridhoazh.obs.inventory;

import com.ridhoazh.obs.inventory.rest.InventoryModel;
import com.ridhoazh.obs.sequence.SequenceGenerator;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 24, 2025
 */
// @formatter:on

public interface InventoryMapper extends SequenceGenerator {

    static InventoryModel entityToModel(Inventory inventory) {
        return InventoryModel.builder()
                .id(inventory.getId())
                .itemId(inventory.getItem().getId())
                .qty(inventory.getQty())
                .type(inventory.getType())
                .build();
    }

    static Inventory modelToEntity(Inventory inventory,
            InventoryModel inventoryModel) {
        inventory.setQty(inventoryModel.qty());
        inventory.setType(inventoryModel.type());
        return inventory;
    }

}
