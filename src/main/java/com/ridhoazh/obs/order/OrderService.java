package com.ridhoazh.obs.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ridhoazh.obs.utils.BaseSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 24, 2025
 */
// @formatter:on

public interface OrderService {

    Page<Order> search(BaseSearchParams searchParams, Pageable pageable);

    Order detail(String id);

    void save(Order order);

    void delete(Order order);

    Boolean isExist(String id);

}
