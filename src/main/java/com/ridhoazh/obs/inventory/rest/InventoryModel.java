package com.ridhoazh.obs.inventory.rest;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 23, 2025
 */
// @formatter:on

@AutoValue
@JsonDeserialize(builder = AutoValue_InventoryModel.Builder.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class InventoryModel {

    @Nullable
    public abstract Long id();

    @Nullable
    public abstract Long itemId();

    @Nullable
    public abstract Integer qty();

    @Nullable
    public abstract String type();

    public static Builder builder() {
        return new AutoValue_InventoryModel.Builder();

    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public static interface Builder {
        public abstract InventoryModel build();

        public abstract Builder id(Long value);

        public abstract Builder itemId(Long value);

        public abstract Builder qty(Integer value);

        public abstract Builder type(String value);

    }
}
