package com.ridhoazh.obs.item.rest;

import com.ridhoazh.obs.utils.BaseSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

public class ItemSearchParams extends BaseSearchParams {

    private boolean showRemainingStock;

    public boolean isShowRemainingStock() {
        return showRemainingStock;
    }

    public void setShowRemainingStock(boolean showRemainingStock) {
        this.showRemainingStock = showRemainingStock;
    }
}
