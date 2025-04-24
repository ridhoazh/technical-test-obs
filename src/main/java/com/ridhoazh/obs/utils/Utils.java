package com.ridhoazh.obs.utils;

import java.util.HashMap;
import java.util.Map;

// @formatter:off
/**
 * 🧠 Created by: Ridho Azhari Riyadi
 * 🗓️ Date: Apr 24, 2025
 * 💻 Auto-generated because Ridho too lazy to type this manually
 */
// @formatter:on

public interface Utils {

    public static Long removePrefixId(String id) {
        return Long.valueOf(id.replaceAll("\\D+", ""));
    }

    public static Map<String, Object> buildResponseMessage(String id,
            String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("message", message);
        return response;
    }

}
