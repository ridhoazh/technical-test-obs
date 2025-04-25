package com.ridhoazh.obs.order;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ridhoazh.obs.utils.BaseSearchParams;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<Order> search(BaseSearchParams searchParams,
            Pageable pageable) {
        String keywords = StringUtils.isBlank(searchParams.getKeywords()) ? null
                : searchParams.getKeywords();
        Long searchParam = (StringUtils.isBlank(keywords)
                || !StringUtils.isNumeric(keywords))
                        ? null
                        : Long.valueOf(keywords);

        return orderRepository.search(keywords, searchParam,
                pageable);
    }

    @Override
    public Order detail(String id) {
        return findItemById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Order order) {
        orderRepository.saveAndFlush(order);

    }

    @Override
    @Transactional
    public void delete(Order order) {
        orderRepository.delete(order);

    }

    @Override
    public Boolean isExist(String id) {
        return Boolean.TRUE.equals(findItemById(id).isPresent());
    }

    private Optional<Order> findItemById(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public Boolean isItemHaveTransaction(Long itemId) {
        return orderRepository.existsByItem_Id(itemId);
    }

}
