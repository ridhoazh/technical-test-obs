package com.ridhoazh.obs.order.rest;

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
@JsonDeserialize(builder = AutoValue_OrderModel.Builder.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class OrderModel {

    @Nullable
    public abstract String orderNo();

    @Nullable
    public abstract Long itemId();

    @Nullable
    public abstract Integer price();

    @Nullable
    public abstract Integer qty();

    public static Builder builder() {
        return new AutoValue_OrderModel.Builder();

    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public static interface Builder {
        public abstract OrderModel build();

        public abstract Builder orderNo(String value);

        public abstract Builder itemId(Long value);

        public abstract Builder price(Integer value);

        public abstract Builder qty(Integer value);

    }
}
