package com.ridhoazh.obs.item.rest;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

// @formatter:off
/**
 * 🧠 Created by: Ridho Azhari Riyadi
 * 🗓️ Date: Apr 23, 2025
 * 💻 Auto-generated because Ridho too lazy to type this manually
 */
// @formatter:on

@AutoValue
@JsonDeserialize(builder = AutoValue_ItemModel.Builder.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class ItemModel {

    @Nullable
    public abstract Long id();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract Integer price();

    public static Builder builder() {
        return new AutoValue_ItemModel.Builder();

    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public static interface Builder {
        public abstract ItemModel build();

        public abstract Builder id(Long value);

        public abstract Builder name(String value);

        public abstract Builder price(Integer value);

    }
}
