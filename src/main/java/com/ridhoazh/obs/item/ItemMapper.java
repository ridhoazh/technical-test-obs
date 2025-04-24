package com.ridhoazh.obs.item;

import com.ridhoazh.obs.item.rest.ItemModel;
import com.ridhoazh.obs.sequence.SequenceGenerator;

// @formatter:off
/**
 * 🧠 Created by: Ridho Azhari Riyadi
 * 🗓️ Date: Apr 24, 2025
 * 💻 Auto-generated because Ridho too lazy to type this manually
 */
// @formatter:on

public interface ItemMapper extends SequenceGenerator {

    static ItemModel entityToModel(Item item) {
        return ItemModel.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .build();
    }

    static Item modelToEntity(Item item, ItemModel itemModel) {
        item.setName(itemModel.name());
        item.setPrice(itemModel.price());
        return item;
    }

}
