package com.ridhoazh.obs.utils;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

public class ValidationMessage {
    public static String NULL = "com.ridhoazh.obs.exception.value_is_null";
    public static String NULL_OR_NEGATIVE = "com.ridhoazh.obs.exception.value_is_null_or_below_zero";
    public static String NOT_FOUND = "com.ridhoazh.obs.exception.not_found";
    public static String INAPPROPRIATE_VALUE = "com.ridhoazh.obs.exception.inappropriate_value";
    public static String CREATED = "Successfully created";
    public static String UPDATED = "Successfully updated";
    public static String DELETED = "Successfully deleted";
    public static String STOCK_UNDER_ZERO = "Sorry, stock can't be reduced to less than zero, please revise the quantity.";
    public static String STOCK_INSUFFICIENT = "Sorry, stock is insufficient, please revise the quantity.";

}
